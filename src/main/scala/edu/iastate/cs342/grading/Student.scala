package edu.iastate.cs342.grading

import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.util.IO

case class Student private (val firstName: String, val lastName: String, val netID: String, val repoURL: String) extends Equals {
  val fullName = firstName + " " + lastName

  lazy val repoFolderPath = RulesAndAssumptions.createRepoFileSystemLocationFromStudentID(netID)

  def canEqual(other: Any) = {
    other.isInstanceOf[edu.iastate.cs342.grading.Student]
  }

  override def equals(other: Any) = {
    other match {
      case that: edu.iastate.cs342.grading.Student => that.canEqual(Student.this) && firstName == that.firstName && lastName == that.lastName && netID == that.netID
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime * (prime * (prime + firstName.hashCode) + lastName.hashCode) + netID.hashCode
  }

  override lazy val toString = fullName + " " + netID
}

object Student {
  /**
   * @param lineFromFile this "constructor" expects a string formatted like a line from the students.txt file
   * The only supported format so far is:
   * firstName,lastName,netID
   *
   */
  def apply(lineFromFile: String) = {
    val values = lineFromFile.split(",")
    assert(values.length == 3, "Read an Invalid line from the students file. Please ensure that the format is: firstName,lastName,netID")
    val firstName = values(0)
    val lastName = values(1)
    val netID = values(2)
    val repoURL = RulesAndAssumptions.createRepoURLFromStudentID(netID)
    new Student(firstName, lastName, netID, repoURL)
  }
}

