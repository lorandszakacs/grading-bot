package edu.iastate.cs342.grading.git

import edu.iastate.cs342.grading.constants.Constants
import scala.sys.process._
import edu.iastate.cs342.grading.util.IO
import scala.sys.process.Process
import edu.iastate.cs342.grading.command.Command

private sealed trait GitCommand extends Command {
  final override val fullCommandToExecute: String = Constants.ConfigValues.PathToGit
  final override lazy val commandSequence: Seq[String] = Seq(fullCommandToExecute) ++ gitArgumentsSeq
  protected val gitArgumentsSeq: Seq[String]
}

private case class GitClone(fromWhereURL: String, toWhere: String) extends GitCommand {
  private val CloneSuccededMarker = "Checking connectivity... done"

  override val rootFolder = toWhere
  override val gitArgumentsSeq: Seq[String] = Seq("clone", fromWhereURL, rootFolder)
  override def createProcess: ProcessBuilder = Process(commandSequence)

  override def determineErrors(out: List[String], err: List[String]) {
    val cloneFailed = !(out.find(s => s.contains(CloneSuccededMarker)).isDefined)
    if (cloneFailed) throw new GitCloneFailed(err.mkString("\n"))
  }
}

private case class GitAdd(file: String, override val rootFolder: String) extends GitCommand {
  override val gitArgumentsSeq: Seq[String] = Seq("add", file)
}

private case class GitStatus(override val rootFolder: String) extends GitCommand {
  override val gitArgumentsSeq: Seq[String] = Seq("status")
}

private case class GitCommit(override val rootFolder: String, val message: String) extends GitCommand {
  override val gitArgumentsSeq: Seq[String] = Seq("commit", "-m", message)
}

private case class GitPush(override val rootFolder: String) extends GitCommand {
  override val gitArgumentsSeq: Seq[String] = Seq("push")
}

class GitRepositoryExecutor private (val gitRepoPath: String) {

  def clone(formWhereURL: String) = {
    val gitClone = new GitClone(formWhereURL, gitRepoPath)
    val result = gitClone.execute()
    val out = result._1
    val err = result._2
    result
  }

  def add(file: String) = {
    val gitAdd = new GitAdd(file, gitRepoPath)
    gitAdd.execute()
  }

  def commit(message: String) = {
    val gitCommit = new GitCommit(gitRepoPath, message)
    gitCommit.execute()
  }

  def push() = {
    val gitPush = new GitPush(gitRepoPath)
    gitPush.execute()
  }

  def status() = {
    val gitStatus = new GitStatus(gitRepoPath)
    gitStatus.execute()
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