# Setup
  - install sbt (Simple Build Tool) version 0.13.0 or later. You can download it from [here](http://www.scala-sbt.org/release/docs/Getting-Started/Setup ).
  - copy the contents of the sbt folder found in [my repository here](https://github.com/lorandszakacs/config/tree/master/sbt) to the folder where you installed your sbt. If it's a fresh install then you should not have any conflicts. Otherwise simply append the two plugins in the file to your already exising `plugins.sbt` file
  - the two recommended plugins are [sbteclipse](https://github.com/typesafehub/sbteclipse) and [sbt-start-script](https://github.com/sbt/sbt-start-script)
  - **recommended:** download the ScalaIDE based on Eclipse from [here](http://scala-ide.org/download/current.html).
  - installing `scala` is optional. sbt will automatically download a scala version for you, but if you want to use ScalaIDE then it is required.
  - make a local copy of the `grading/build.sbt.template` file and rename the copy to `build.sbt`.
  - you will have to edit two things withing `build.sbt`. The `javaHome` variable, make sure the path specified is the one to your local Java jdk. Optionally, you can uncomment `scalaHome` and make it point to your local Scala installation. If you leave this commented then sbt will download scala for you.
  - to test that everything went smoothly run `$sbt` in a terminal in the folder containing the project, wait for `sbt` to finish any downloads. If an error occurs during downloading it might be a good idea to simply restart `sbt`. Once `sbt` is started up it will behave as an special REPL with its own commands. You should probably check out this useful sbt [cheatsheet](http://www.stateofmind.fr/wiki/scala/sbt_cheatsheet/)
  - **The grading bot relies on one very important assumption: that authentification to `github` is set up using `ssh keys`, as described in this [guide](https://help.github.com/articles/generating-ssh-keys). The program makes `git ...` system calls and was not written to account for further interaction.**

### Editing and running with ScalaIDE
  - run the `eclipse` once you have started `sbt`. This will create all necessary files describing an Eclipse project. Now you can simply go to the Eclipse menu and use the `File -> Import -> Existing Projects Into Workspace...` feature to import the project directly. The `main` method can be found in `/grading/src/main/scala/edu/iastate/cs342/grading/Main.scala` 

# Running
You have three options of running the program:
  - from the ScalaIDE as described above
  - from sbt directly. Once you `compile` in `sbt` you can simply evaluate `run` to start your program. This approach has issues with the REPL loop of the program. You can easily input commands, but you cannot delete any incorrect commands, you have to evaluate them and then re-type them correctly.
  - generate a start script with `sbt`. From `sbt` simply evaluate `start-script` and this will generate a start script at the indicated location. You can then use the script to run the program.

**Before you can run the program correctly** you have to do one last thing: in the `grading/src/main/resources/` folder you will find a file called `application.conf.template`. Create a local copy, rename the copy to `application.conf` and edit it as indicated in the file. The paths **should** include the name of the executable file as well. Do not leave any spaces after the `=` sign for each property. The grading bot will use `path-to-git` and `path-to-racket` to make system calls to these two, and it will download all git repositories to the `path-where-to-download`.

# How to grade
There are two important files (**this is a temporary solution**) that you need to keep in the `grading/` folder:
  - `students.txt` this file contains the names and netIDs of the students you want to run the batch process for. The format is the standard `csv` one, as indicated in the file.
  - `homework.info` contains the metainformation that a describes the homework to be graded. Information on how to format such a file can be found in `homework.info.template` file. Please contact [Lori](https://github.com/lorandszakacs) for any further information. The name of the homework that is to be graded can be found in this file, e.g. `homework01`.  

Additionally, you should delete any previous contents from the 

Once you ensure that these two files exists, start the program. The program is a REPL loop that can take a fixed number of commands. To grade a homework you evaluate, in order, the following:
  - `> load-students` this will load the contents of the `students.txt` file. You can always reevaluate this command to reload the contents of the file.
  - `> load-homework-info` this will load the meta information about the homework and use it to create a file called `grading-test.rkt` in the folder corresponding to the homework to be graded.
  - `grab-all` this will download the `hw-answers-netID` repositories for each students to a folder named `$netID` at the path specified in the `application.conf` file.
  - `> grade-all` will create the `netID/homeworkXX/grading-test.rkt` file and run it. Its output and some formated information is dumped in the `netID/homeworkXX/feedback.md` file. Currently if one testcase fails in a test suite the student receives 0 points for that test-suite. We will have to figure out a reasonable way to automatically weigh each test case. The `grading-test.rkt` file will **not** be automatically deleted.
  - manually go through each student's assignments and write any feedback and/or deduct/add points to the homework in the `feedback.md` file.
  - evaluate `> add-and-commit-all` this will run a `git add feedback.md` for each student repository. It will **not** add the `grading-test.rkt` file, so just ignore it.
  - evaluate `> push-all` to push all commits to the student's repositories. It will use the following commit message "Added feedback.md for homework$XX." 
  - `exit` to exit the program.

All of the above commands can be run independently, but, for instance, `grade-all` requires that all repositories be present in the download folder.


