package edu.iastate.cs342.grading.util

import scala.util.control.Breaks._
import java.io.IOException
import java.io.InputStream
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.BufferedWriter
import java.io.FileWriter
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.ByteArrayOutputStream
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

object IO {

  val separator: String = File.separator

  def toFile(path: String): File = FileUtils.getFile(path).getAbsoluteFile()
  
  def toAbsolute(path: String): String = FileUtils.getFile(path).getAbsolutePath()

  def createFolder(folderPath: String): String = {
    val folder = FileUtils.getFile(folderPath)
    folder.mkdirs()
    folder.setExecutable(true)
    folder.setReadable(true)
    folder.setWritable(true)

    if (!folder.canWrite() || !folder.canExecute()) {
      folder.delete();
      throw new RuntimeException("Could not create path specified: %".format(folder))
    } else folder.getAbsolutePath()
  }

  def rename(original: String, newName: String) {
    val file = FileUtils.getFile(original)
    val newFile = FileUtils.getFile(newName)
    assume(file.exists(), "Trying to rename a file that does not exists %s: ".format(file.getAbsoluteFile()))
    assume(!newFile.exists(), "Trying to rename to a file that exists %s:".format(newFile.getAbsoluteFile()))
    file.renameTo(newFile)

  }

  def listContent(rootPath: String): List[String] = {
    val folder = FileUtils.getFile(rootPath)
    assume(folder.exists(), "Trying to list the contents of a folder that does not exists")
    folder.listFiles().toList.map(_.getAbsolutePath)
  }

  def listFolders(rootPath: String): List[String] = {
    val root = FileUtils.getFile(rootPath)
    assume(root.exists(), "Trying to list the folders from a folder that does not exists")
    val allContent = root.listFiles()
    val allFolders = allContent filter (f => f.isDirectory())
    allFolders.toList.map(_.getAbsolutePath)
  }

  def listFiles(rootPath: String): List[String] = {
    val root = FileUtils.getFile(rootPath)
    assume(root.exists(), "Trying to list the folders from a folder that does not exists")
    val allContent = root.listFiles()
    val allFiles = allContent filterNot (f => f.isDirectory())
    allFiles.toList.map(_.getAbsolutePath)
  }

  def createFile(filePath: String): String = {
    val newFile = FileUtils.getFile(filePath)
    assume(newFile.createNewFile(), "could not create file: %".format(filePath))
    newFile.setExecutable(true)
    newFile.setReadable(true)
    newFile.setWritable(true)
    newFile.getCanonicalPath()
  }
  
  def concatPath(basePath: String, onePath: String, toConcat: String*): String = {
    val tempPath = FilenameUtils.concat(basePath, onePath)
    def recursivelyConcatenate(files: Seq[String]): String = {
      if (files.tail.isEmpty)
        FilenameUtils.concat(tempPath, files.head)
      else
        FilenameUtils.concat(recursivelyConcatenate(files.tail), files.head)
    }

    if (toConcat.isEmpty)
      tempPath
    else
      recursivelyConcatenate(toConcat.reverse)
  }

  def getFileName(path: String) = {
    path.substring(path.lastIndexOf(separator) + 1)
  }

  def exists(folderPath: String): Boolean = {
    FileUtils.getFile(folderPath).exists()
  }

  def existsAndEmptyFolder(folderPath: String): Boolean = {
    IO.exists(folderPath) && !IO.isEmpty(folderPath)
  }

  def existsAndEmptyFolder(root: String, path: String): Boolean = {
    val newFolder = IO.concatPath(root, path)
    IO.exists(newFolder) && !IO.isEmpty(newFolder)
  }

  def isEmpty(folderPath: String): Boolean = {
    val folder = FileUtils.getFile(folderPath)
    assume(folder.exists(), "trying to see that a folder that does not exists is empty")
    folder.list().isEmpty
  }

  def deleteFolder(folder: String) {
    FileUtils.deleteDirectory(FileUtils.getFile(folder))
  }

  def deleteFolder(folder: File) {
    FileUtils.deleteDirectory(folder)
  }

  def readLines(filePath: String): List[String] = {
    val result = FileUtils.readLines(FileUtils.getFile(filePath));
    result.toList
  }

  def writeToFile(data: Array[Byte], fileName: String) {
    val out = new FileOutputStream(fileName);
    out.write(data);
    out.flush();
    out.close();
  }

  def writeToFile(data: Array[Char], fileName: String) {
    val out = new BufferedWriter(new FileWriter(FileUtils.getFile(fileName)));
    out.write(data);
    out.flush();
    out.close();
  }

  def getByteArrayFromInputStream(input: InputStream, contentLength: Int): Array[Byte] = {
    assume(contentLength > 0, "Trying to read from an input stream with invalid content length, use the other method to do that")
    val data = new Array[Byte](contentLength)

    var bytesRead = 0;
    var offset = 0;
    breakable {
      while (offset < contentLength) {
        bytesRead = input.read(data, offset, data.length - offset);
        if (bytesRead == -1)
          break;
        offset += bytesRead;
      }
    }
    input.close();

    if (offset != contentLength) {
      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
    }
    data
  }

  def getByteArrayFromInputStream(input: InputStream): Array[Byte] = {
    var read: Int = 0
    var result = ListBuffer[Byte]()
    read = input.read()
    while (-1 != read) {
      result.append(read.toByte)
      read = input.read()
    }
    input.close()
    result.toArray
  }

}