package edu.iastate.cs342.grading.git

class GitRepositoryNotFoundException(message: String) extends Exception(message)
class GitCloneFailed(message:String) extends Exception(message)