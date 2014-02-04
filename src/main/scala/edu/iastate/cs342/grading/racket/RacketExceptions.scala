package edu.iastate.cs342.grading.racket

class RacketCompilationError(message: String, val out: List[String], val err: List[String]) extends Exception(message)
class RacketRuntimeError(message: String, val out: List[String], val err: List[String]) extends Exception(message)
class RacketCannotOpenFileError(message: String, val out: List[String], val err: List[String]) extends Exception(message)