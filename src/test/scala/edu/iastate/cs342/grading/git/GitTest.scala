package edu.iastate.cs342.grading.git

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import edu.iastate.cs342.grading.util.IO
import scala.sys.process._

@RunWith(classOf[JUnitRunner])
class GitTest extends FunSuite {

  def cloneRepo(from: String, where: String) = {
    val gitRepoPath = IO.toAbsolute("./test-site/" + where)
    IO.deleteFolder(gitRepoPath)
    val repoExecutor = GitRepositoryExecutor(gitRepoPath)
    repoExecutor.clone(from)
    assert(IO.exists(gitRepoPath) === true, "Repository was not cloned")
    repoExecutor
  }

  test("test git clone") {
    val CloneRepoURL = "git@github.com:ComS342-ISU/grading-unit-test-clone.git"
    val repoExecutor = cloneRepo(CloneRepoURL, "test-git-clone")
    repoExecutor.deleteRepo()
  }

  test("test git status") {
    val CloneRepoURL = "git@github.com:ComS342-ISU/grading-unit-test-clone.git"
    val repoExecutor = cloneRepo(CloneRepoURL, "test-git-status")
    val status = repoExecutor.status().replace(" ", "")
    assert(status.contains("nothingtocommit,workingdirectoryclean"))
    repoExecutor.deleteRepo()
  }

  test("test git add") {
    val CloneRepoURL = "git@github.com:ComS342-ISU/grading-unit-test-clone.git"
    val repoExecutor = cloneRepo(CloneRepoURL, "test-git-add")
    val fileToAdd = "feedback.md"
    IO.createFile(IO.concatPath(repoExecutor.gitRepoPath, fileToAdd))
    repoExecutor.add(fileToAdd)
    val status = repoExecutor.status().replace(" ", "")
    assert(status.contains("newfile:" + fileToAdd), "git status does not indicate that file was added")
    repoExecutor.deleteRepo()
  }
}