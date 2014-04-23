package edu.iastate.cs342.grading.repl

import scala.util.parsing.combinator.RegexParsers
import edu.iastate.cs342.grading.RulesAndAssumptions

sealed trait UserCommand {
  def instructions: String
  def command: String
  def man: String = UserCommand.this.command + " " + UserCommand.this.instructions
}

private object UserCommand {
  object Keywords {
    val GrabAll = "grab-all"
    val GradeAll = "grade-all"
    val AddAndCommitAll = "add-and-commit-all"
    val PushAll = "push-all"
    val LoadStudents = "load-students"
    val Report = "create-report"
    val LoadHomeworkInfo = "load-homework-info"
    val Help = "help"
    val Exit = "exit"

  }

}

case class Fail(val msg: String) extends UserCommand {
  override val instructions: String = ""
  override val command: String = ""
}

case class LoadStudents(val fileName: String) extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.LoadStudents
}

case class LoadHomeworkInfo(val fileName: String) extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.LoadHomeworkInfo
}

case class GrabAllCommand() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.GrabAll
}

case class GradeAllCommand() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.GradeAll
}

case class AddAndCommitAllCommand() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.AddAndCommitAll
}

case class PushAll() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.PushAll
}

case class ReportCommand() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.Report
}

case class HelpCommand() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.Help
}

case class ExitCommand() extends UserCommand {
  override val instructions: String = ""
  override val command: String = UserCommand.Keywords.Exit
}

object CommandParser extends RegexParsers {
  override def skipWhitespace = true;

  def homeworkNamePattern: Parser[String] = "[a-zA-Z][a-zA-Z0-9]*".r
  def fileNamePattern: Parser[String] = "^[\\w,\\s-]+\\.[A-Za-z]".r

  def loadStudents: Parser[UserCommand] = UserCommand.Keywords.LoadStudents ~ fileNamePattern.? ^^ {
    case _ ~ fileName => {
      fileName match {
        case None => LoadStudents(RulesAndAssumptions.DefaultStudentsFile)
        case Some(fn) => LoadStudents(fn)
      }
    }
  }

  def loadHomeworkInfo: Parser[UserCommand] = UserCommand.Keywords.LoadHomeworkInfo ~ fileNamePattern.? ^^ {
    case _ ~ fileName => {
      fileName match {
        case None => LoadHomeworkInfo(RulesAndAssumptions.DefaultHomeworkInfoFile)
        case Some(fn) => LoadHomeworkInfo(fn)
      }
    }
  }

  def grabAll: Parser[UserCommand] = UserCommand.Keywords.GrabAll ^^ { _ => GrabAllCommand() }

  def gradeAll: Parser[UserCommand] = UserCommand.Keywords.GradeAll ^^ { _ => GradeAllCommand() }

  def addAndCommitAll: Parser[UserCommand] = UserCommand.Keywords.AddAndCommitAll ^^ { _ => AddAndCommitAllCommand() }

  def pushAll: Parser[UserCommand] = UserCommand.Keywords.PushAll ^^ { _ => PushAll() }

  def report: Parser[UserCommand] = UserCommand.Keywords.Report ^^ { _ => ReportCommand() }

  def help: Parser[UserCommand] = UserCommand.Keywords.Help ^^ { _ => HelpCommand() }

  def exit: Parser[UserCommand] = UserCommand.Keywords.Exit ^^ { _ => ExitCommand() }

  def command = (loadStudents | loadHomeworkInfo | grabAll | gradeAll | addAndCommitAll | pushAll | report | help | exit)

  def apply(input: String): UserCommand = parseAll(command, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => Fail(failure.msg)
  }

}
