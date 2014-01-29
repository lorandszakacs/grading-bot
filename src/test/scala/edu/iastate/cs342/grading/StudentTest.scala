package edu.iastate.cs342.grading

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import edu.iastate.cs342.grading.homework.Student

@RunWith(classOf[JUnitRunner])
class StudentTest extends FunSuite {

  test("Student string parsing") {
    val firstName = "Brynden"
    val lastName = "Rivers"
    val netID = "bloodraven"
    val input = "%s,%s,%s".format(firstName, lastName, netID)

    val stud = Student(input)
    assert(stud.firstName === firstName)
    assert(stud.lastName === lastName)
    assert(stud.netID === netID)
  }
}