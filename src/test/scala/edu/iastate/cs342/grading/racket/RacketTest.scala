package edu.iastate.cs342.grading.racket

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import edu.iastate.cs342.grading.util.IO
import scala.sys.process._

@RunWith(classOf[JUnitRunner])
class RacketTest extends FunSuite with BeforeAndAfterEach {

  val racketFolder = IO.toAbsolute("./src/test/resources/racket/")
  val testFolder = IO.toAbsolute("./test-site/racket-test")

  override def beforeEach() {
    def copyFiles() {
      IO.copy(racketFolder, testFolder)
    }
    copyFiles()
  }
  override def afterEach() {
    def deleteFiles() {
      IO.deleteFolder(testFolder)
    }
    deleteFiles();
  }

  test("test run racket compiler error test") {
    val racketExecutor = RacketHomeworkExecutor(racketFolder)
    intercept[RacketCompilationError] {
      racketExecutor.run("test-file-compilation-error.rkt")
    }
  }

  test("test run racket correct test") {
    val racketExecutor = RacketHomeworkExecutor(racketFolder)
    val output = racketExecutor.run("test-file-correct.rkt")
    assert(output.contains("1 success(es) 0 failure(s) 0 error(s) 1 test(s) run"), output)
  }

  test("test run racket runtime-error test") {
    val racketExecutor = RacketHomeworkExecutor(racketFolder)
    intercept[RacketRuntimeError] {
      racketExecutor.run("test-file-runtime-error.rkt")
    }
  }
}