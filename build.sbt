import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

name := "download-and-grade"

version := "0.1"

scalaVersion := "2.10.2"

//The path to the java environment
javaHome := Some(file("/Library/Java/JavaVirtualMachines/jdk1.7.0_25.jdk/Contents/Home/"))

//comment this out entirely if you want sbt to simply download the version specified instead
//of using the one installed on your file system.
//scalaHome := Some(file("/home/user/scala/trunk/"))

//you can get these from maven by creating a string:
//groupID % artifactID % revision
libraryDependencies ++= Seq( 
    "commons-io" % "commons-io" % "2.4",
    "com.typesafe" % "config" % "1.0.2",
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
    "com.novocode" % "junit-interface" % "0.8" % "test->default"
)

scalaSource in Compile <<= baseDirectory(_ / "src/main/scala")

scalaSource in Test <<= baseDirectory(_ / "src/test/scala")

// set the main class for packaging the main jar
// 'run' will still auto-detect and prompt
// change Compile to Test to set it for the test jar
mainClass in (Compile, packageBin) := Some("edu.iastate.cs342.grading.Main")

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
mainClass in (Compile, run) := Some("edu.iastate.cs342.grading.Main")

// disable printing timing information, but still print [success]
showTiming := true

// disable printing a message indicating the success or failure of running a task
showSuccess := true

// Execute tests in the current project serially
// Tests from other projects may still run concurrently.
parallelExecution in Test := false

//memory
javaOptions += "-Xmx1G"

//scala compiler options
scalacOptions += "-deprecation"

// append several options to the list of options passed to the Java compiler
javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

// specifies that all tests will be executed in a single external JVM.
fork in Test := true

// define the statements initially evaluated when entering 'console', 'console-quick', or 'console-project'
initialCommands := """
  import edu.iastate.cs342.grading._
"""

// set the location of the JDK to use for compiling Java code.
// if 'fork' is true, this is used for 'run' as well
//javaHome := Some(file(JavaJDKPath))

// Use Scala from a directory on the filesystem instead of retrieving from a repository
//scalaHome := Some(file("/home/user/scala/trunk/"))

//eclipse project settings:
EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17)

EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala