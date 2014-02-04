package edu.iastate.cs342.grading.homework

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.git.GitRepositoryExecutor

@RunWith(classOf[JUnitRunner])
class HomeworkGradingFlowTest extends FunSuite with BeforeAndAfterEach {

  //https://github.com/ComS342-ISU/hw-answers-homeworkexecutortest
  val student = Student("test,Test,homeworkexecutortest")
  val homeworkInfo = HomeworkInfo("src/test/resources/homework-test/homework-test.info")
  val homeworkInfoIncorrect = HomeworkInfo("src/test/resources/homework-test/homework-test-failed-suite.info")

  override def beforeEach() {
    IO.deleteFolder(student.repoFolderPath)
  }
  override def afterEach() {
    IO.deleteFolder(student.repoFolderPath)
  }

  test("test grading flow") {
    val hwExec = new HomeworkExecutor(student, homeworkInfo)
    hwExec.grabHomework()
    hwExec.gradeHomework()
    hwExec.addAndCommitFeedbackFile()
    hwExec.pushFeedbackFile()
    assert(IO.exists(hwExec.feedbackFilePath))
  }

  test("test grading failed suite") {
    val hwExec = new HomeworkExecutor(student, homeworkInfoIncorrect)
    hwExec.gradeHomework
    assert(IO.exists(hwExec.feedbackFilePath))
  }
}