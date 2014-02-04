package edu.iastate.cs342.grading.repl

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandParserTest extends FunSuite with BeforeAndAfterEach {

  val interpreter = new UserCommandInterpreter()

  test("command - load students") {
    val result = CommandParser("load-students")
    println(result)
    
    val result2 = CommandParser("load-homework-info")
    println(result2)
  }
}