package edu.iastate.cs342.grading.repl

import scala.util.parsing.combinator.RegexParsers

sealed trait Command {
  def instructions: String
  def command: String
  def man: String = this.command + " " + this.instructions
}

private object Command {
  object Keywords {
    val GrabAll = "grab-all"
    val GradeAll = "grade-all"
    val AddAll = "add-all"
    val PushAll = "push-all"
    val Help = "help"
    val Exit = "exit"
  }
}

case class Fail(val msg: String) extends Command {
  override val instructions: String = ""
  override val command: String = ""
}

case class GrabAllCommand() extends Command {
  override val instructions: String = ""
  override val command: String = Command.Keywords.GrabAll
}

case class GradeAllCommand(homeworkName: String) extends Command {
  override val instructions: String = ""
  override val command: String = Command.Keywords.GradeAll
}

case class AddAll() extends Command {
  override val instructions: String = ""
  override val command: String = Command.Keywords.AddAll
}

case class PushAll() extends Command {
  override val instructions: String = ""
  override val command: String = Command.Keywords.PushAll
}

case class HelpCommand() extends Command {
  override val instructions: String = ""
  override val command: String = Command.Keywords.Help
}

case class ExitCommand() extends Command {
  override val instructions: String = ""
  override val command: String = Command.Keywords.Exit
}

object CommandParser extends RegexParsers {
  override def skipWhitespace = true;

  def homeworkNamePattern: Parser[String] = "[a-zA-Z][a-zA-Z0-9]*".r

  def grabAll: Parser[Command] = Command.Keywords.GrabAll ^^ {
    case _ => GrabAllCommand()
  }

  def gradeAll: Parser[Command] = Command.Keywords.GradeAll ~ homeworkNamePattern ^^ {
    case _ ~ homeworkName => GradeAllCommand(homeworkName)
  }

  def addAll: Parser[Command] = Command.Keywords.AddAll ^^ { _ => AddAll() }

  def pushAll: Parser[Command] = Command.Keywords.PushAll ^^ { _ => PushAll() }

  def help: Parser[Command] = Command.Keywords.Help ^^ { _ => HelpCommand() }

  def exit: Parser[Command] = Command.Keywords.Exit ^^ { _ => ExitCommand() }

  def command = (grabAll | gradeAll | addAll | pushAll | help | exit)

  def apply(input: String): Command = parseAll(command, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => Fail(failure.msg)
  }

}
