package edu.iastate.cs342.grading.homework

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.git.GitRepositoryExecutor

@RunWith(classOf[JUnitRunner])
class HomeworkExecutorTest extends FunSuite with BeforeAndAfterEach {

  //https://github.com/ComS342-ISU/hw-answers-homeworkexecutortest
  val student = Student("test,Test,homeworkexecutortest")
  val homeworkInfo = HomeworkInfo("src/test/resources/homework-test/homework-test.info")
  val homeworkInfoIncorrect = HomeworkInfo("src/test/resources/homework-test/homework-test-failed-suite.info")
  val homeworkInfoNotSubmitted = HomeworkInfo("src/test/resources/homework-test/homework-test-not-submitted.info")
  val homeworkInfoEmptySubmission = HomeworkInfo("src/test/resources/homework-test/homework-test-empty-submission.info")

  override def beforeEach() {
    IO.deleteFolder(student.repoFolderPath)
    val gitRepoExec = GitRepositoryExecutor(student.repoFolderPath)
    gitRepoExec.clone(student.repoURL)
  }
  override def afterEach() {
    IO.deleteFolder(student.repoFolderPath)
  }

  test("test grading correct homework") {
    val hwExec = new HomeworkExecutor(student, homeworkInfo)
    val report = hwExec.gradeHomework
    assert(IO.exists(hwExec.feedbackFilePath))
  }

  test("test grading failed suite") {
    val hwExec = new HomeworkExecutor(student, homeworkInfoIncorrect)
    val report = hwExec.gradeHomework
    assert(IO.exists(hwExec.feedbackFilePath))
  }

  test("test grading on homework that is not submitted") {
    val hwExec = new HomeworkExecutor(student, homeworkInfoNotSubmitted)
    val report = hwExec.gradeHomework
    assert(IO.exists(hwExec.feedbackFilePath))
  }

  test("test grading on homework that was submitted empty") {
    val hwExec = new HomeworkExecutor(student, homeworkInfoEmptySubmission)
    val report = hwExec.gradeHomework
    assert(IO.exists(hwExec.feedbackFilePath))
  }
}