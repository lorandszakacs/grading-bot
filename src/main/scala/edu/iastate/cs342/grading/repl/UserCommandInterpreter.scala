package edu.iastate.cs342.grading.repl

import edu.iastate.cs342.grading.homework.Student
import edu.iastate.cs342.grading.util.IO
import edu.iastate.cs342.grading.homework.HomeworkInfo
import edu.iastate.cs342.grading.git.GitRepositoryExecutor
import edu.iastate.cs342.grading.homework.HomeworkExecutor
import edu.iastate.cs342.grading.homework.HomeworkExecutor

class UserCommandInterpreter extends UserCommandVisitor {

  override def visit(fail: Fail): UserCommandVisitorResult = {
    println("invalid command: " + fail.msg + "+ \ntry typing: help")
    UserCommandVisitorSuccess()
  }

  override def visit(help: HelpCommand): UserCommandVisitorResult = {
    //TODO: implement help
    println("TODO, implement help.")
    UserCommandVisitorSuccess()
  }

  override def visit(exit: ExitCommand): UserCommandVisitorResult = {
    throw new RuntimeException("should never get here")
  }

  //FIXME: find a better way to do this:
  private var _homeworkInfo: Option[HomeworkInfo] = None
  override def visit(loadHomeworkInfo: LoadHomeworkInfo): UserCommandVisitorResult = {
    try {
      val tempHomeworkInfo = HomeworkInfo(loadHomeworkInfo.fileName)
      println("Homework info loaded:")
      println("name-of-homework: " + tempHomeworkInfo.homeworkName)
      println("name-of-feedback: " + tempHomeworkInfo.feedbackFileName)
      println("tests: \n\t" + tempHomeworkInfo.testSuites.mkString("\n\t"))
      this._homeworkInfo = Some(tempHomeworkInfo)
      UserCommandVisitorSuccess()
    } catch {
      case e: Exception => UserCommandVisitorFail(e.getMessage)
    }
  }

  //FIXME: find a better way to do this
  private var _students: Option[List[Student]] = None
  override def visit(loadStudents: LoadStudents): UserCommandVisitorResult = {
    try {
      val studentLines = IO.readLines(loadStudents.fileName)
      val tempStudents = studentLines.map(Student(_)).toList
      println("Loaded the following students: \n")
      println("\t" + tempStudents.mkString("\n\t"))
      println("\t Total: " + tempStudents.length)
      this._students = Some(tempStudents)
      UserCommandVisitorSuccess()
    } catch {
      case e: Exception => UserCommandVisitorFail(e.getMessage)
    }
  }

  def checkBeforeVisit(interpretCommand: (List[Student], HomeworkInfo) => UserCommandVisitorResult): UserCommandVisitorResult = {
    (_students, _homeworkInfo) match {
      case (None, None) => UserCommandVisitorFail("`homework info` and `students` not loaded. Please do so before trying again.")
      case (_, None) => UserCommandVisitorFail("`homework info` not loaded. Please do so before trying again.")
      case (None, _) => UserCommandVisitorFail("`students` not loaded. Please do so before trying again.")
      case (Some(students), Some(homeworkInfo)) => interpretCommand(students, homeworkInfo)
    }
  }

  override def visit(grabAll: GrabAllCommand): UserCommandVisitorResult = {
    def helper(students: List[Student], homeworkInfo: HomeworkInfo): UserCommandVisitorResult = {
      val hwExecs = students map { s => new HomeworkExecutor(s, homeworkInfo) }
      hwExecs foreach { he =>
        println("cloning %s ".format(he.student.repoURL))
        he.grabHomework
      }
      println("done grabbing.")
      UserCommandVisitorSuccess()
    }
    checkBeforeVisit(helper)
  }

  override def visit(gradeAll: GradeAllCommand): UserCommandVisitorResult = {
    def helper(students: List[Student], homeworkInfo: HomeworkInfo): UserCommandVisitorResult = {
      val hwExecs = students map { s => new HomeworkExecutor(s, homeworkInfo) }
      hwExecs foreach { s =>
        println("grading: " + s.student.toString)
        s.gradeHomework
      }
      println("done grading.")
      UserCommandVisitorSuccess()
    }
    checkBeforeVisit(helper)
  }

  override def visit(addAll: AddAndCommitAllCommand): UserCommandVisitorResult = {
    def helper(students: List[Student], homeworkInfo: HomeworkInfo): UserCommandVisitorResult = {
      val hwExecs = students map { s => new HomeworkExecutor(s, homeworkInfo) }
      hwExecs foreach { he =>
        println("adding `%s` for: %s ".format(he.homework.feedbackFileName, he.student.netID))
        he.addAndCommitFeedbackFile
      }
      println("done adding.")
      UserCommandVisitorSuccess()
    }
    checkBeforeVisit(helper)
  }

  override def visit(pushAll: PushAll): UserCommandVisitorResult = {
    def helper(students: List[Student], homeworkInfo: HomeworkInfo): UserCommandVisitorResult = {
      val hwExecs = students map { s => new HomeworkExecutor(s, homeworkInfo) }
      hwExecs foreach { he =>
        println("pushing for: %s".format(he.student.netID))
        he.pushFeedbackFile
      }
      println("done pushing.")
      UserCommandVisitorSuccess()
    }
    checkBeforeVisit(helper)
  }
}