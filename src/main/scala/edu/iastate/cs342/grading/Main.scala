package edu.iastate.cs342.grading

import scala.sys.process._
import edu.iastate.cs342.grading.constants.Constants

object Main {

  def main(args: Array[String]): Unit = {
    val targetFolder = Constants.ConfigValues.PathWhereToDownload
    val urlToRepo = "git@github.com:ComS342-ISU/test-student1-repo.git"
    val command = Constants.ConfigValues.PathToGit
    val temp = Seq(command, "clone", urlToRepo, targetFolder)
    println(Constants.ConfigValues.PathWhereToDownload)
    println(Constants.ConfigValues.PathToGit)
    temp.!
  }

}