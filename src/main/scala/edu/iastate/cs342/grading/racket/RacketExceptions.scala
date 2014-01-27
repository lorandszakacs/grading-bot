package edu.iastate.cs342.grading.racket

class RacketCompilationError(message: String) extends Exception(message)
class RacketRuntimeError(message: String) extends Exception(message)
class RacketCannotOpenFileError(message: String) extends Exception(message)