# Setup
  - install sbt (Simple Build Tool) version 0.1.3 or later. You can download it from [here](http://www.scala-sbt.org/release/docs/Getting-Started/Setup ).
  - copy the contents of the sbt folder found in [my repository here](https://github.com/lorandszakacs/config/tree/master/sbt). If it's a fresh install then you should not have any conflicts. Otherwise simply append the two plugins in the file to your already exising **plugins.sbt** file
  - the two recommended plugins are [sbteclipse](https://github.com/typesafehub/sbteclipse) and [sbt-start-script](https://github.com/sbt/sbt-start-script)
  - **recommended:** download the ScalaIDE based on Eclipse from [here](http://scala-ide.org/download/current.html).
  - installing scala is optional. sbt will automatically download a scala version for you, but if you want to use ScalaIDE then it is required.
  - make a local copy of the **build.sbt.template** file and rename the copy to **build.sbt**.
  - you will have to edit two things. The **javaHome** variable, make sure the path specified is the one to your local Java jdk. Optionally, you can uncomment **scalaHome** and make it point to your local Scala installation. If you leave this commented then sbt will download scala for you.
  - to test that everything went smoothly run `$sbt` in a terminal in the folder containing the project, wait for sbt to finish downloads. If an error occurs during downloading it might be a good idea to try again. Once `sbt` is started up just evaluate `compile`.
  - **The program relies on one very important assumption: that authentification to `github` is set up using `ssh keys`, as described in this [guide](https://help.github.com/articles/generating-ssh-keys). The program makes `git ...` system calls and was not written for interaction.**

### Editing and running with ScalaIDE
  - run the `eclipse` once you have started `sbt`. This will create all necessary files describing an Eclipse project. Now you can simply go to the Eclipse menu and use the `File -> Import -> Existing Projects Into Workspace...` feature to import the project directly. The `main` method can be found in `/grading/src/main/scala/edu/iastate/cs342/grading/Main.scala` 

# Running
You have three options of running the program:
  - from the ScalaIDE as described above
  - from sbt directly. Once you `compile` in `sbt` you can simply evaluate `run` to start your program. This approach has issues with the REPL loop of the program. You can easily input commands, but you cannot delete any incorrect commands, you have to evaluate them and then re-type them correctly.
  - generate a start script with `sbt`. From `sbt` simply evaluate `start-script` and this will generate a start script at the indicated location. You can then use the script to run the program.

**Before you can run the program correctly** you have to do one last thing: in the `grading/src/main/resources/` folder you will find a file called `application.conf.template`. Create a local copy, rename the copy to `application.conf` and edit it as indicated in the file. The paths **should** include the name of the executable file as well. Do not leave any spaces after the `=` sign for each property. The grading bot will use `path-to-git` and `path-to-racket` to make system calls to these two, and it will download all git repositories to the `path-where-to-download`.

# How to grade

  - 
