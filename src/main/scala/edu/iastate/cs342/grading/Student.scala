package edu.iastate.cs342.grading

import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.util.IO

case class Student(val firstName: String, val lastName: String, val email: String, val repoURL: String) extends Equals {
  val fullName = firstName + " " + lastName

  lazy val filePath = IO.concatPath(Constants.ConfigValues.PathWhereToDownload, lastName)

  def canEqual(other: Any) = {
    other.isInstanceOf[edu.iastate.cs342.grading.Student]
  }

  override def equals(other: Any) = {
    other match {
      case that: edu.iastate.cs342.grading.Student => that.canEqual(Student.this) && firstName == that.firstName && lastName == that.lastName && email == that.email
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime * (prime * (prime + firstName.hashCode) + lastName.hashCode) + email.hashCode
  }

  override val toString = fullName + " " + email
}