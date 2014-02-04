package edu.iastate.cs342.grading.command

import edu.iastate.cs342.grading.util.IO
import scala.sys.process.Process
import scala.sys.process.ProcessLogger
import scala.sys.process.ProcessBuilder
import scala.collection.mutable

trait SystemCommand {
  /**
   * Denotes the folder in which this command should be executed in.
   * e.d. user/download/homework
   */
  val rootFolder: String

  /**
   * For portability reasons we choose to provide the full path of a system command.
   * e.g. instead of > git status we run > /full-path-to-git/git status
   */
  val fullCommandToExecute: String

  /**
   * Specifies the actual full command sequence to be executed.
   * @return the value of the stdout of the process
   * @throws exceptions thrown in the @link{determineErrors} method
   */
  val commandSequence: Seq[String]

  protected def createProcess: ProcessBuilder = {
    val folderWhereToExecute = IO.toFile(rootFolder + "/")
    Process(commandSequence, folderWhereToExecute)
  }

  /**
   * @return a pair of List[String] where the first element is the
   * output lines corresponding to the output, the second one is the
   * lines corresponding to the standard error of the command
   */
  def execute(): (List[String], List[String]) = {
    val proc = createProcess
    val out = new mutable.ListBuffer[String]()
    val err = new mutable.ListBuffer[String]()
    val logger = ProcessLogger(
      (o: String) => out.append(o),
      (e: String) => err.append(e))

    proc ! logger
    val outList = out.toList
    val errList = err.toList
    determineErrors(outList, errList)
    (outList, errList)
  }

  /**
   * @param out contains the output of stdout of the process
   * @param err contains the output to stderr of the process
   *
   * This method should throw exceptions
   */
  def determineErrors(out: List[String], err: List[String]) {}

}