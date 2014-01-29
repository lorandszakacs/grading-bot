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
      IO.copyFolder(racketFolder, testFolder)
    }
    copyFiles()
  }
  override def afterEach() {
    def deleteFiles() {
      IO.deleteFolder(testFolder)
    }
    deleteFiles();
  }

  test("test run racket cannot open file error") {
    
  }
  test("test run racket compiler error test") {
    val racketExecutor = RacketExecutor(racketFolder)
    intercept[RacketCompilationError] {
      racketExecutor.run("test-file-compilation-error.rkt")
    }
  }

  test("test run racket correct test") {
    val racketExecutor = RacketExecutor(racketFolder)
    val output = racketExecutor.run("test-file-correct.rkt")
    assert(true === false, "FIXME")
  }

  test("test run racket runtime-error test") {
    val racketExecutor = RacketExecutor(racketFolder)
    intercept[RacketRuntimeError] {
      racketExecutor.run("test-file-runtime-error.rkt")
    }
  }
}