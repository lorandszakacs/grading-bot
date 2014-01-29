package edu.iastate.cs342.grading.racket

import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.util.IO
import scala.sys.process.Process
import scala.sys.process.ProcessLogger
import edu.iastate.cs342.grading.command.Command

private sealed trait RacketCommand extends Command {
  val fullCommandToExecute: String = Constants.ConfigValues.PathToRacket
}

private class RacketRun(fileToRun: String, override val rootFolder: String) extends RacketCommand {
  val CannotOpenInputFileMarkers: String = "cannot open input file"
  val CompilationErrorMarker: String = "does-not-compile:"
  override val commandSequence: Seq[String] = Seq(fullCommandToExecute, fileToRun)

  override def determineErrors(out: List[String], err: List[String]) {
    if (!err.isEmpty) {
      val compilationError = err.find(s => s.contains(CompilationErrorMarker))
      val cannotOpenInputFile = err.find(s => s.contains(CannotOpenInputFileMarkers))
      val errorMessage = err.mkString(" ")
      if (compilationError.isDefined) throw new RacketCompilationError(errorMessage)
      else if (cannotOpenInputFile.isDefined) throw new RacketCannotOpenFileError(errorMessage)
      else throw new RacketRuntimeError(errorMessage)
    }
  }
}

class RacketExecutor private (val rootFolder: String) {
  def run(fileToRun: String): (List[String], List[String]) = {
    val racketRun = new RacketRun(fileToRun, rootFolder)
    racketRun.execute()
  }

  def deleteFolder() {
    IO.deleteFolder(rootFolder)
  }
}

object RacketExecutor {
  def apply(rootFolder: String) = new RacketExecutor(rootFolder)
}