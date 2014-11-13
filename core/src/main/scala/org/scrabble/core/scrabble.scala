package org.scrabble.core

import java.io.{File, FileWriter}
import java.nio.file.{Paths,Files}
/**
 *
 */

object Scrabble{
  case class ScoredWord(word: String, score: Int)
}

class Scrabble {

  import Scrabble._
  var words = scala.collection.immutable.Vector[String]()

  /**
   *
   * @param word - Scrabble dictionary word to be indexed
   * @return Iterable containing all iterations of word in gaddag format
   */
  private def gaddag(word: String): Iterable[String] = List.range(1,word.size+1).map( index => word.take(index).reverse + ">" + word.drop(index))

  /**
   *
   * @param word - Word to be stored
   * @param depth - Depth into word in which to create an independent file
   * @return - Return file path to store word group
   */
  private def getFilePath(word: String, depth: Int) = word.split("_")(0).slice(0, depth).mkString("/")

  /**
   * Checks if file directory exists, if not it creates one
   *
   * @param fullFileName - The full file path
   *
   */
  private def createDirIfNotExists(fullFileName: String) {
    val file = new File(fullFileName)
    val parentDir = file.getParentFile
    if (null != parentDir)
    {
      parentDir.mkdirs()
    }
  }

  /**
   * Writes partition of words to file seperated by new lines
   *
   * @param wordGroup - Partitioned Group of words to write to file
   *
   */
  def writeWordsToFile(wordGroup: Vector[String], depth: Int) {
    val fileName = getFilePath(wordGroup(0),depth)
    val fullFileName = s"../.index/$fileName.txt"
    createDirIfNotExists(fullFileName)
    val fw = new FileWriter(fullFileName)
    wordGroup.map(word => fw.write(word + "\n"))
    fw.close()
  }

  /**
   * Indexes Scrabble dictionary to queryable text files
   *
   * @param words - Official Scrabble words to be added to fil
   *
   */
  def serializeWords(words: Vector[String]) {
    val gWords = words.map { word =>
      val score = Score.calculateScore(word).toString
      gaddag(word).map(word => s"${word}_$score")
    }.flatten.sorted
    (1 to 3).map { depth =>
      gWords.groupBy(_.slice(0, depth)).values.map {  wordGroup =>
        writeWordsToFile(wordGroup,depth)
      }
    }
  }

  /**
   *
   * @param filePath - file to load words from
   * @return - Returns Vector of all words within file
   */
  def loadWords(filePath: String) = {
    if (Files.exists(Paths.get(filePath))) {
      val file = scala.io.Source.fromFile(filePath)
      file.getLines.toVector
    }
    else Vector[String]()
  }

  /**
   *
   * @param query - Sub String to search
   * @return vector of words containing substring within them
   */
  def lookup(query: String) =  {
    val prefix = query.reverse
    val fileName = getFilePath(prefix,3)
    val words = loadWords(s"../.index/$fileName.txt")
    val lowBound = words.indexWhere(_.slice(0,prefix.size)==prefix)
    val upperBound = words.lastIndexWhere(_.slice(0,prefix.size)==prefix) + 1
    words.slice(lowBound,upperBound)
  }

  /**
   *
   * @param word - word to be scored
   * @return Case class containing word associated with scrabble score
   */
  def scoreWord(word: String) = {
    val wordScore = word.split("_")
    ScoredWord( wordScore(0), wordScore(1).toInt)
  }

  /**
   *
   * @param word - Stored word (gaddag format) with score attached by _
   * @return Case class containing actual word and score
   */
  def transformWord(word: String) = {
    val wordScore = word.split("_")
    val splitWord = wordScore(0).split(">")
    val suffix = if (splitWord.size > 1) splitWord(1) else ""
    val transformedWord = (splitWord(0).reverse + suffix).toString
    ScoredWord( transformedWord, wordScore(1).toInt)
  }

  /**
   *
   * @param words Scored words
   * @return Sorted Vector of scored words
   */
  def sortWords(words: Vector[ScoredWord]) = {
    words.sortBy(_.score).reverse
  }

  /**
   *
   * @param query - Substring to search for available words
   * @param limit - Number of words to return
   * @return - The maximum number requested/existing words which contain the queried substring
   */
  def query(query: String, limit: Int) =
  {
    val results = scala.collection.mutable.Set[String]()
    sortWords(lookup(query).map(transformWord(_))).takeWhile {word =>
      results.add(word.word)
      results.size < limit
    }
    results
  }
}