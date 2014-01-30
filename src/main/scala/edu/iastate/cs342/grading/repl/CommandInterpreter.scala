package edu.iastate.cs342.grading.repl

class CommandInterpreter extends CommandVisitor {

  override def visit(fail: Fail): CommandVisitorResult = {
    println("interpreted fail")
    CommandVisitorSuccess()
  }

  override def visit(help: HelpCommand): CommandVisitorResult = {
    println("interpreted help")
    CommandVisitorSuccess()
  }

  override def visit(exit: ExitCommand): CommandVisitorResult = {
    println("interpreted exit")
    CommandVisitorSuccess()
  }

  override def visit(grabAll: GrabAllCommand): CommandVisitorResult = {
    println("interpreted graball")
    CommandVisitorSuccess()
  }

  override def visit(gradeAll: GradeAllCommand): CommandVisitorResult = {
    println("interpreted grade all")
    CommandVisitorSuccess()
  }

  override def visit(addAll: AddAll): CommandVisitorResult = {
    println("interpreted add all")
    CommandVisitorSuccess()
  }

  override def visit(pushAll: PushAll): CommandVisitorResult = {
    println("interpreted push all")
    CommandVisitorSuccess()
  }
}