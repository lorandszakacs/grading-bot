package edu.iastate.cs342.grading.constants
import com.typesafe.config.ConfigFactory

object Constants {
  object ConfigValues {
    private object PropertyKeys {
      val PathWhereToDownload = "grading.path-where-to-download"
      val PathToGit = "grading.path-to-git"
      val PathToRacket = "grading.path-to-racket"
    }
    private val conf = ConfigFactory.load()
    val PathWhereToDownload = conf.getString(PropertyKeys.PathWhereToDownload)
    val PathToGit = conf.getString(PropertyKeys.PathToGit)
    val PathToRacket = conf.getString(PropertyKeys.PathToRacket)
  }
}