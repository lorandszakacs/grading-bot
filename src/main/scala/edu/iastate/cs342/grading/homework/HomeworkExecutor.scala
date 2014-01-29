package edu.iastate.cs342.grading.homework

import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.git.GitRepositoryExecutor
import edu.iastate.cs342.grading.racket.RacketExecutor
import edu.iastate.cs342.grading.RulesAndAssumptions

class HomeworkExecutor(val student: Student, val homework: HomeworkInfo) {
  val repoPath = student.repoFolderPath
  val targetHomeworkPath = IO.concatPath(repoPath, homework.homeworkName, RulesAndAssumptions.StudentSolutionFolder)

  def gradeHomework() = {
    val gitRepoExec = GitRepositoryExecutor(repoPath)
    gitRepoExec.clone(student.repoURL)

    val gradingTestFilePath = writeGradingTest()
    val racketExecutor = RacketExecutor(targetHomeworkPath)
    val racketOutput = racketExecutor.run(gradingTestFilePath)
  }

  private def writeGradingTest(): String = {
    "path-to-the-file"
  }

  private def createReport(out: List[String], err: List[String]) = {

  }
}