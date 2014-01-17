package edu.iastate.cs342.grading.git

import edu.iastate.cs342.grading.constants.Constants
import scala.sys.process._
import edu.iastate.cs342.grading.util.IO
import scala.sys.process.Process

private sealed trait GitCommand {
  val gitRepoPath: String = ""
  val git: String = Constants.ConfigValues.PathToGit
  val commandSequence: Seq[String]

  /**
   * @param gitRepoPath
   * 	path to the local git repository
   * @return
   * 	executes the command and returns any output generated by this command
   */
  def execute(): String = {
    val gitRepoFolderBasePath = IO.toFile(gitRepoPath)
    val proc = Process(commandSequence, gitRepoFolderBasePath)
    proc.!!
  }
}

private case class GitClone(fromWhereURL: String, override val gitRepoPath: String) extends GitCommand {
  override def execute(): String = commandSequence.!!
  override val commandSequence: Seq[String] = Seq(git, "clone", fromWhereURL, gitRepoPath)
}

private case class GitAdd(file: String, override val gitRepoPath: String) extends GitCommand {
  override val commandSequence: Seq[String] = Seq(git, "add", file)
}

private case class GitStatus(override val gitRepoPath: String) extends GitCommand {
  override val commandSequence: Seq[String] = Seq(git, "status")
}

class GitRepositoryExecutor private (val gitRepoPath: String) {
  def clone(formWhereURL: String) = {
    val gitClone = new GitClone(formWhereURL, gitRepoPath)
    val output = gitClone.execute()
    if (!output.contains("Checking connectivity... done")) throw new RuntimeException("clone failed: " + formWhereURL + "\n" + output)
    output
  }

  def add(file: String) = {
    val gitAdd = new GitAdd(file, gitRepoPath)
    gitAdd.execute()
  }

  def status() = {
    val gitStatus = new GitStatus(gitRepoPath)
    gitStatus.execute();
  }

  def deleteRepo() {
    IO.deleteFolder(gitRepoPath)
  }
}

object GitRepositoryExecutor {
  /**
   * @param gitRepoPath is the path on the file system where the repo can be found.
   * @return
   */
  def apply(gitRepoPath: String) = new GitRepositoryExecutor(gitRepoPath)
}