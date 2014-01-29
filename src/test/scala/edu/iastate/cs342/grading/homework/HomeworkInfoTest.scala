package edu.iastate.cs342.grading.homework

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import edu.iastate.cs342.grading.util.IO
import scala.sys.process._

@RunWith(classOf[JUnitRunner])
class HomeworkInfoTest extends FunSuite with BeforeAndAfterEach {
  val testFilePath = "src/test/resources/homework-test/homework-test.info"

  test("Homework Info parsing") {
    val hwInfo = HomeworkInfo(testFilePath)

    val expectedName = "homework-test"
    assert(hwInfo.homeworkName === expectedName)
    
    val expectedFeedbackFileName = "feedback.md"
    assert(hwInfo.feedbackFileName === expectedFeedbackFileName)

    val expectedSuites = List(("part-one-test-suite", 35), ("18-series-a-test-suite", 15), ("18-series-b-test-suite", 15))
    assert(hwInfo.testSuites === expectedSuites)
    val expectedImports = List("rackunit", "\"hw01-tests.rkt\"", "\"test-infrastructure.rkt\"")
    assert(hwInfo.imports === expectedImports)

  }

  test("Homework Info grading-test contents") {
    val hwInfo = HomeworkInfo(testFilePath)
    val expectedValue = "#lang racket\n" +
      "(#%require rackunit)\n" +
      "(#%require \"hw01-tests.rkt\")\n" +
      "(#%require \"test-infrastructure.rkt\")\n" +
      "(define results (list\n" +
      "(test part-one-test-suite)\n" +
      "(test 18-series-a-test-suite)\n" +
      "(test 18-series-b-test-suite)))\n" +
      "(print \"results: \")\n" +
      "(print results)"
    assert(hwInfo.gradingTestContents === expectedValue)
  }
}