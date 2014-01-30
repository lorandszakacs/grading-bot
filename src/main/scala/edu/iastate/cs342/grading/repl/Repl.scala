package edu.iastate.cs342.grading.repl

class Repl {
  def start(): Unit = {
    val interpreter = new CommandInterpreter();
    println("type -help for instructions")
    while (true) {
      print("> ")
      val input = Console.readLine
      val command = CommandParser.apply(input);
      command match {
        case ExitCommand() => return
        case _ => {
          val result = interpreter.visit(command)
          result match {
            case CommandVisitorFail(msg) => println(msg)
            case _ => Unit
          }
        }
      }
    }
  }
}