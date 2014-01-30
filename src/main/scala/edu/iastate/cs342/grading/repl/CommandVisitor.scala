package edu.iastate.cs342.grading.repl

sealed trait CommandVisitorResult
case class CommandVisitorFail(val msg: String) extends CommandVisitorResult
case class CommandVisitorSuccess() extends CommandVisitorResult

trait CommandVisitor {
  final def visit(comm: Command): CommandVisitorResult = {
    comm match {
      case e: ExitCommand => this.visit(e)
      case f: Fail => this.visit(f)
      case g: GrabAllCommand => this.visit(g)
      case gr: GradeAllCommand => this.visit(gr)
      case aa: AddAll => this.visit(aa)
      case pa: PushAll => this.visit(pa)
      case h: HelpCommand => this.visit(h)
    }
  }

  def visit(fail: Fail): CommandVisitorResult

  def visit(help: HelpCommand): CommandVisitorResult

  def visit(exit: ExitCommand): CommandVisitorResult

  def visit(grabAll: GrabAllCommand): CommandVisitorResult

  def visit(gradeAll: GradeAllCommand): CommandVisitorResult

  def visit(addAll: AddAll): CommandVisitorResult

  def visit(pushAll: PushAll): CommandVisitorResult

}
