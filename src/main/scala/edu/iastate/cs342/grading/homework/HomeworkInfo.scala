package edu.iastate.cs342.grading.homework

import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.RulesAndAssumptions

class TestSuiteInfo private (val name: String, val scoreValue: Int) extends Equals {
  override lazy val toString = "%s = %d points".format(name, scoreValue)

  def canEqual(other: Any) = {
    other.isInstanceOf[edu.iastate.cs342.grading.homework.TestSuiteInfo]
  }

  override def equals(other: Any) = {
    other match {
      case that: edu.iastate.cs342.grading.homework.TestSuiteInfo => that.canEqual(TestSuiteInfo.this) && name == that.name && scoreValue == that.scoreValue
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime * (prime + name.hashCode) + scoreValue.hashCode
  }
}

private object TestSuiteInfo {
  def apply(testSuiteLine: String) = {
    val split = testSuiteLine.split(" ")
    new TestSuiteInfo(split(0), split(1).toInt)
  }

  def apply(name: String, scoreValue: Int) = {
    new TestSuiteInfo(name, scoreValue)
  }
}

private object FileToCopy {
  def apply(fileToCopyLine: String): String = {
    RulesAndAssumptions.createFilesToCopyPath(fileToCopyLine.drop(2))
  }
}

class HomeworkInfo private (
  val testSuites: List[TestSuiteInfo],
  val imports: List[String],
  val filesToCopy: List[String],
  val homeworkName: String,
  val feedbackFileName: String,
  val expectedFiles: List[String]) {

  /**
   * example:
   * #lang racket
   *
   * (#%require rackunit)
   * (#%require "hw01-tests.rkt")
   * (#%require "test-infrastructure.rkt")
   *
   * (define results
   * (list (test part-one-test-suite)
   * (test 18-series-a-test-suite)
   * (test 18-series-b-test-suite)))
   *
   * (print "results: ")
   * (print results)
   */
  lazy val gradingTestContents = {
    val importLines = imports.map(s => "(#%%require %s)".format(s)).mkString("\n")
    val testLines = testSuites.map(t => "(test %s)".format(t.name)).mkString("\n")
    "#lang racket\n" +
      importLines + "\n" +
      "(define results (list\n" +
      testLines + "))\n" +
      "(print \"%s\")\n".format(HomeworkInfo.ResultMarker) +
      "(print results)"
  }
}

/**
 * @author lorand
 * FIXME:
 * This should be rewritten to use a regex parser similar to how the REPL is built.
 */
object HomeworkInfo {
  final val ResultMarker = "###&&&***results: "

  private val CommentMarker = "#"
  private val TestSuiteMarker = "*"
  private val HomeworkNameMarker = "homework-name: "
  private val ExpectedFilesMarker = "expected-files: "
  private val ImportsMarker = "imports: "
  private val FeedbackFileNameMarker = "feedback-file-name: "
  private val FileToCopyMarker = "+"

  def apply(filePath: String): HomeworkInfo = {
    val lines = IO.readLines(filePath).filterNot(_.startsWith(CommentMarker))
    val homeworkNameLines = lines.filter(_.startsWith(HomeworkNameMarker))
    assert(homeworkNameLines.length == 1, "There should only be one homework name.")
    val homeworkName = homeworkNameLines(0).split(HomeworkNameMarker)(1)
    assert(homeworkName.length > 0, "Did not find any test suite names in the homeworkinfo")

    val expectedFilesLines = lines.filter(_.startsWith(ExpectedFilesMarker));
    assert(expectedFilesLines.length == 1, "There should be only one line of expectedfiles")
    val expectedFiles = expectedFilesLines(0).drop(ExpectedFilesMarker.length).split(" ").toList

    val testSuiteLines = lines.filter(_.startsWith(TestSuiteMarker))
    val trimmedTestSuiteLines = testSuiteLines.map(s => s.drop(2))
    val testSuites = trimmedTestSuiteLines.map(TestSuiteInfo(_)).toList

    val importLines = lines.filter(_.startsWith(ImportsMarker))
    assert(importLines.length == 1, "There should be only one line of Imports")
    val imports = importLines(0).drop(ImportsMarker.length).split(" ").toList

    val feedBackFileLines = lines.filter(_.startsWith(FeedbackFileNameMarker))
    assert(feedBackFileLines.length == 1, "There should be only one line of feedback file name.")
    val feedbackFileName = feedBackFileLines(0).drop(FeedbackFileNameMarker.length)

    val filesToCopyLines = lines.filter(_.startsWith(FileToCopyMarker))
    val filesToCopy = filesToCopyLines.map(FileToCopy(_))
    new HomeworkInfo(testSuites, imports, filesToCopy, homeworkName, feedbackFileName, expectedFiles)
  }
}