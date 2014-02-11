package edu.iastate.cs342.grading.homework

import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.git.GitRepositoryExecutor
import edu.iastate.cs342.grading.racket.RacketExecutor
import edu.iastate.cs342.grading.RulesAndAssumptions
import edu.iastate.cs342.grading.racket.RacketRuntimeError
import edu.iastate.cs342.grading.racket.RacketCannotOpenFileError
import edu.iastate.cs342.grading.racket.RacketCompilationError
import edu.iastate.cs342.grading.git.GitCloneFailed

private object FeedbackFile {
  final val DefaultNoAnswerString = "N/A"
}

private class FeedbackFile(
  val student: Student,
  val homework: HomeworkInfo,
  val results: List[String],
  val out: List[String],
  val err: List[String]) {

  private val TopLevelTemplate =
    //here we should put netID and homework-name
    "# Feedback file for `%s` submitted by `%s %s %s`\n\n" +
      "# TA/Instructor feedback: \n\nHomework has not been manually inspected yet, please standby.\n\n" +
      "##### Final score after manual inspection: **todo**\n\n" +
      //after the summary we introduce the TestSuiteSummaryTemplate String
      "# Summary of results: \n%s\n\n\n" +
      "# Standard output of program: \n%s\n\n\n" +
      "# Standard error of program (includes failed tests report): \n%s\n\n\n"

  private val TestSuiteSummaryEntryTemplate =
    "test suite: %s\n" +
      "    failed tests:    %s\n" +
      "    test score:      %s\n" +
      "    adjusted score:  **todo**\n" +
      "    max possible:    %s\n"

  private val TestSuiteSummaryTemplate =
    "%s\n" +
      "##### Total score from grading bot: %s"

  /**
   * a string containing a feedback file content build according to this template
   */
  lazy val fileContent: String = {
    val suitesAndResults = homework.testSuites.zip(results)
    var totalScore = 0
    val testResultSummary = suitesAndResults map { pair =>
      val suite = pair._1
      val failedTests = pair._2
      val possibleScore = suite.scoreValue
      val obtainedScore =
        if (!failedTests.equals(0.toString))
          0
        else { totalScore += possibleScore; possibleScore }

      TestSuiteSummaryEntryTemplate.format(
        suite.name,
        failedTests,
        obtainedScore.toString,
        suite.scoreValue.toString)
    }
    val testSummary = TestSuiteSummaryTemplate.format(testResultSummary.mkString("\n"), totalScore.toString)

    TopLevelTemplate.format(
      homework.homeworkName,
      student.netID,
      student.firstName,
      student.lastName,
      testSummary,
      out.mkString("\n"),
      err.mkString("\n"))
  }
}

class HomeworkExecutor(val student: Student, val homework: HomeworkInfo) {

  val GradingTestName = "grading-test.rkt"
  val CompiledFolderName = "compiled"

  val targetHomeworkPath = IO.concatPath(student.repoFolderPath, homework.homeworkName, RulesAndAssumptions.StudentSolutionFolder)
  val feedbackFilePath = IO.concatPath(targetHomeworkPath, homework.feedbackFileName)
  val feedbackFileRelativePath = IO.concatPath(homework.homeworkName, RulesAndAssumptions.StudentSolutionFolder, homework.feedbackFileName)
  val gradingTestFilePath = IO.concatPath(targetHomeworkPath, GradingTestName)

  def grabHomework() {
    val gitRepoExec = GitRepositoryExecutor(student.repoFolderPath)
    try {
      gitRepoExec.clone(student.repoURL)
    } catch {
      case e: Exception => {
        val error = "failed to clone repository for student: " + student.toString + "\nReason:\n" + e.getMessage() + "\n\n"
        System.err.println(error)
      }
    }
  }

  def gradeHomework(): String = {
    def cleanUpUselessFiles() {
      //since the grading test isn't pushed we might as well keep it there for now.
      IO.deleteFolder(IO.concatPath(targetHomeworkPath, CompiledFolderName))
    }

    def writeReport(out: List[String], err: List[String]): String = {
      val resultsOfGrading: List[String] = {
        val resultLine = out.filter(s => s.contains(homework.ResultMarker))
        if (resultLine.length == 1) {
          val filteredResults = (resultLine(0).dropWhile(c => c != '\''))
          val resultsAsStrings = filteredResults.split(" ").map(s => s.filterNot(c => ((c == '\'') || (c == '(') || (c == ')'))))
          resultsAsStrings.toList
        } else {
          List.fill(homework.testSuites.length)(FeedbackFile.DefaultNoAnswerString)
        }
      }
      val feedbackFile = new FeedbackFile(student, homework, resultsOfGrading, out, err)
      IO.writeToFile(feedbackFile.fileContent, feedbackFilePath)
      feedbackFile.fileContent
    }

    def writeGradingTest() {
      IO.writeToFile(homework.gradingTestContents, gradingTestFilePath)
    }

    val racketOutput = try {
      val submittedHomework: Boolean = {
        //TODO: write a more sophisticated check
        IO.exists(targetHomeworkPath)
      }
      
      if (submittedHomework) {
        writeGradingTest()
        val racketExecutor = RacketExecutor(targetHomeworkPath)
        racketExecutor.run(gradingTestFilePath)
      } else {
        System.err.println("Student: " + student.toString + "\t has not submitted homework.")
        (List(), List())
      }
    } catch {
      //FIXME: better error handling.
      case rre: RacketRuntimeError => (rre.out, rre.err)
      case rcof: RacketCannotOpenFileError => (rcof.out, rcof.err)
      case rce: RacketCompilationError => (rce.out, rce.err)
      case e: Exception => {
        val error = "failed to run racket program for student: " + student.toString + "\nReason:\n" + e.getMessage() + "\n\n"
        System.err.println(error)
        (List(e.getMessage), List(e.getMessage))
      }
    }
    val report = writeReport(racketOutput._1, racketOutput._2)
    cleanUpUselessFiles()
    report
  }

  def addAndCommitFeedbackFile() {
    val defaultMessage = "Added %s file for %s".format(homework.feedbackFileName, homework.homeworkName)
    this.addAndCommitFeedbackFile(defaultMessage)
  }

  def addAndCommitFeedbackFile(commitMessage: String) {
    val gitRepoExec = GitRepositoryExecutor(student.repoFolderPath)
    try {
      gitRepoExec.add(feedbackFileRelativePath)
      gitRepoExec.commit("Added %s file for %s".format(homework.feedbackFileName, homework.homeworkName))
    } catch {
      case e: Exception => {
        val error = "failed to add the feedbackfile for student: " + student.toString + "\nReason:\n" + e.getMessage() + "\n\n"
        System.err.println(error)
      }
    }
  }

  def pushFeedbackFile() {
    val gitRepoExec = GitRepositoryExecutor(student.repoFolderPath)
    try {
      gitRepoExec.push()
    } catch {
      case e: Exception => {
        val error = "failed to push results for student: " + student.toString + "\nReason:\n" + e.getMessage() + "\n\n"
        System.err.println(error)
      }
    }
  }
}