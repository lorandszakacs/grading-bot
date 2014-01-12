//machine specific file paths

import sbt._

object GradingBuild extends Build {
  override lazy val javaHome = Some(file("/Library/Java/JavaVirtualMachines/jdk1.7.0_25.jdk/Contents/Home/"))

  //comment this out entirely if you want sbt to simply download the version specified instead
  //of using the one installed on your file system.
  override lazy val scalaHome := Some(file("/home/user/scala/trunk/"))
}