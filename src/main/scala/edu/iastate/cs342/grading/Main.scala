package edu.iastate.cs342.grading

import scala.sys.process._
import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.git.GitClone
import edu.iastate.cs342.grading.repl.Repl

object Main {

  def main(args: Array[String]): Unit = {
    println("Welcome to 342 git/racket grading bot.")
    val repl = new Repl()
    repl.start()
  }

}