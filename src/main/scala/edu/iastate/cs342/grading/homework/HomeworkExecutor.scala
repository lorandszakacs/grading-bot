package edu.iastate.cs342.grading.homework

import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.git.GitRepositoryExecutor
import edu.iastate.cs342.grading.racket.RacketExecutor
import edu.iastate.cs342.grading.RulesAndAssumptions
import edu.iastate.cs342.grading.racket.RacketRuntimeError
import edu.iastate.cs342.grading.racket.RacketCannotOpenFileError
import edu.iastate.cs342.grading.racket.RacketCompilationError
import edu.iastate.cs342.grading.git.GitCloneFailed

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

  def gradeHomework() {
    def cleanUpUselessFiles() {
      //since the grading test isn't pushed we might as well keep it there for now.
      IO.deleteFolder(IO.concatPath(targetHomeworkPath, CompiledFolderName))
    }

    def writeReport(out: List[String], err: List[String]) {
      def parseResults: String = {
        val resultLine = out.filter(s => s.contains(homework.ResultMarker))
        if (resultLine.length == 1) {
          val filteredResults = (resultLine(0).dropWhile(c => c != '\''))
          val resultsAsStrings = filteredResults.split(" ").map(s => s.filterNot(c => ((c == '\'') || (c == '(') || (c == ')'))))
          val results = resultsAsStrings.map(_.toInt)
          val temp = results.length
          val zipped = homework.testSuites.zip(results)
          //FIXME: remove this mutable variable, I was in a rush when I added it
          var score = 0
          val returnVal = zipped.map(p => {
            val testName = p._1.name
            val possibleScore = p._1.scoreValue
            val failedTests = p._2
            val obtainedScore = if (failedTests != 0) 0 else { score += possibleScore; possibleScore }
            "testSuiteName: %s\n".format(testName) +
              "\tfailedTests: %d\n".format(failedTests) +
              "\tscore: %d\n".format(obtainedScore)
          }).toList
          returnVal.mkString("\n") + "##### Total score: " + score + "\n"
        } else {
          out.mkString("\n")
        }
      }
      val toDump = out.mkString("\n") + err.mkString("\n")
      val toWrite = new StringBuffer
      toWrite.append("# TA/Instructor feedback: \n\n\n")
      toWrite.append("# Sumary of results: \n")
      toWrite.append(parseResults)
      toWrite.append("\n\n\n# Test output: \n")
      toWrite.append(toDump)
      IO.writeToFile(toWrite.toString, feedbackFilePath)
    }
    def writeGradingTest() {
      IO.writeToFile(homework.gradingTestContents, gradingTestFilePath)
    }

    if (IO.exists(targetHomeworkPath)) {
      writeGradingTest()
      val racketExecutor = RacketExecutor(targetHomeworkPath)

      //FIXME: better error handling.
      val racketOutput = try {
        racketExecutor.run(gradingTestFilePath)
      } catch {
        case rre: RacketRuntimeError => (rre.out, rre.err)
        case rcof: RacketCannotOpenFileError => (rcof.out, rcof.err)
        case rce: RacketCompilationError => (rce.out, rce.err)
        case e: Exception => {
          val error = "failed to clone repository for student: " + student.toString + "\nReason:\n" + e.getMessage() + "\n\n"
          System.err.println(error)
          (List(e.getMessage), List(e.getMessage))
        }
      }
      writeReport(racketOutput._1, racketOutput._2)
      cleanUpUselessFiles()
    } else {
      //FIXME: create a log file to write allthese kinds of things.
      System.err.println("Student: " + student.toString + "\t has not submitted homework.")
    }

  }

  def addAndCommitFeedbackFile() {
    val gitRepoExec = GitRepositoryExecutor(student.repoFolderPath)
    try {
      gitRepoExec.add(feedbackFileRelativePath)
      gitRepoExec.commit("Added feedback.md file for " + homework.homeworkName)
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