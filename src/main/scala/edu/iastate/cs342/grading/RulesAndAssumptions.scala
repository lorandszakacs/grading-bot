package edu.iastate.cs342.grading

import edu.iastate.cs342.grading.constants.Constants
import edu.iastate.cs342.grading.util.IO

/**
 * @author lorand
 * 	This object contains functions that handle computation that is heavily dependent on
 *  convention.
 *
 *  This is a temporary class that will help us explicitly state otherwise implicit assumptions.
 *  Therefore it will be an amalgam of computation from different independent modules.
 *
 *  This class should be slowly factored out until it disappears entirely.
 */
object RulesAndAssumptions {

  /**
   * Currently, the format of a repository URL is the following:
   * https://github.com/ComS342-ISU/hw-answers-netID
   */
  def createRepoURLFromStudentID(netID: String) = "https://github.com/ComS342-ISU/hw-answers-" + netID

  /**
   * Repositories will be saved to a subfolder of the folder pointed to in the application.conf file
   */
  def createRepoFileSystemLocationFromStudentID(netID: String) = IO.concatPath(Constants.ConfigValues.PathWhereToDownload, netID)

  val StudentSolutionFolder = "my-solutions"

  val GradingTestName = "grading-test.rkt"

  val DefaultStudentsFile = "students.txt"
    
  val DefaultHomeworkInfoFile = "homework.info"
}