package edu.iastate.cs342.grading.racket

import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.command.SystemCommand
import edu.iastate.cs342.grading.RulesAndAssumptions
import scala.sys.process.Process
import scala.sys.process.ProcessLogger
import scala.actors.Actor
import scala.actors.TIMEOUT

private sealed trait RacketCommand extends SystemCommand {
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
      if (compilationError.isDefined) throw new RacketCompilationError(errorMessage, out, err)
      else if (cannotOpenInputFile.isDefined) throw new RacketCannotOpenFileError(errorMessage, out, err)
      else throw new RacketRuntimeError(errorMessage, out, err)
    }
  }
}

//FIXME: Windows compatibility issues
/*
 *FIXME: the messageToAddStringBuilder is a lazy way of communicating that the process was killed
 * best way to go about solving this would be to make the RacketExecutor an actor as well. 
 */
class TerminationMonitor(val fileRun: String) extends Actor {
  private class PsAuxCommand() extends SystemCommand {
    override val rootFolder = "."
    override val fullCommandToExecute = "ps"
    override val commandSequence: Seq[String] = Seq(fullCommandToExecute, "aux")
  }

  private class KillCommand(val pid: String) extends SystemCommand {
    override val rootFolder = "."
    override val fullCommandToExecute = "kill"
    override val commandSequence: Seq[String] = Seq(fullCommandToExecute, "-9", pid)
  }

  override def act() {
    receiveWithin(RulesAndAssumptions.TimeoutOfRacketPrograms) {
      case TerminationMonitor.Stop => { println("stopping %s".format(fileRun)) }
      case TIMEOUT => {
        val ps = new PsAuxCommand
        val output = ps.execute._1.filter(s => s.contains(fileRun))
        if (output.length == 0) {
          // FIXME: this branch is intentionally left empty. It is only taken because of what I deem to be a bug.
          // roughly 4/45 times this actor never receives the Stop message even though it is *definitely* sent.
          // so we just ignore this case.
        } else {
          if (output.length > 1) {
            System.err.println("TerminationMonitor found more than one matching process. Terminating only one of them THIS SHOULD NEVER HAPPEN.")
          }
          System.err.println("Trying to kill process for: " + fileRun)
          val upToPID = output(0).dropWhile(c => !c.isDigit)
          val afterPIDIndex = upToPID.indexWhere(c => !c.isDigit)
          val pid = upToPID.substring(0, afterPIDIndex)
          val kill = new KillCommand(pid)
          kill.execute
          System.err.println("Killed with no mercy: %s".format(output(0)))
        }
      }
    }
  }
}

object TerminationMonitor {
  case object Stop
}

class RacketExecutor private (val rootFolder: String) {
  def run(fileToRun: String): (List[String], List[String]) = {
    val racketRun = new RacketRun(fileToRun, rootFolder)
    val terminationMonitor = new TerminationMonitor(fileToRun)
    terminationMonitor.start
    val resultTemp = racketRun.execute()

    terminationMonitor ! TerminationMonitor.Stop

    (resultTemp._1, resultTemp._2)
  }

  def deleteFolder() {
    IO.deleteFolder(rootFolder)
  }
}

object RacketExecutor {
  def apply(rootFolder: String) = new RacketExecutor(rootFolder)
}