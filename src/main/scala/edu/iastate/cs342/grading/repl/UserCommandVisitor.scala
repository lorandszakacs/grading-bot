package edu.iastate.cs342.grading.repl

sealed trait UserCommandVisitorResult
case class UserCommandVisitorFail(val msg: String) extends UserCommandVisitorResult
case class UserCommandVisitorSuccess() extends UserCommandVisitorResult

trait UserCommandVisitor {
  final def visit(comm: UserCommand): UserCommandVisitorResult = {
    comm match {
      case e: ExitCommand => UserCommandVisitor.this.visit(e)
      case f: Fail => UserCommandVisitor.this.visit(f)
      case ls: LoadStudents => UserCommandVisitor.this.visit(ls)
      case lhi: LoadHomeworkInfo => UserCommandVisitor.this.visit(lhi)
      case g: GrabAllCommand => UserCommandVisitor.this.visit(g)
      case gr: GradeAllCommand => UserCommandVisitor.this.visit(gr)
      case aa: AddAndCommitAllCommand => UserCommandVisitor.this.visit(aa)
      case pa: PushAll => UserCommandVisitor.this.visit(pa)
      case h: HelpCommand => UserCommandVisitor.this.visit(h)
    }
  }

  def visit(fail: Fail): UserCommandVisitorResult

  def visit(help: HelpCommand): UserCommandVisitorResult

  def visit(exit: ExitCommand): UserCommandVisitorResult

  def visit(loadStudents: LoadStudents): UserCommandVisitorResult

  def visit(loadHomeworkInfo: LoadHomeworkInfo): UserCommandVisitorResult

  def visit(grabAll: GrabAllCommand): UserCommandVisitorResult

  def visit(gradeAll: GradeAllCommand): UserCommandVisitorResult

  def visit(addAll: AddAndCommitAllCommand): UserCommandVisitorResult

  def visit(pushAll: PushAll): UserCommandVisitorResult

}
