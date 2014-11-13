package org.scrabble.profiler

import org.scrabble.core.Score
import scala.util.Random
import sys.process._
import scalax.chart.api._
/**
 * Created by gregkarlin on 11/5/14.
 */
object PerformanceTest
{
  case class SampleResult(score: Int, limit: Int, length: Int, time: Long)
}

class PerformanceTest {

  import PerformanceTest._

  val filePath =  "../resources/gaddag.txt"
  val sampleResults = scala.collection.mutable.MutableList[SampleResult]()

  /**
   *
   * @param filePath - The file with all gaddag representations of acceptable scrabble words
   * @return vector of queryable words
   */

  def getFileLines(filePath: String) = {
    val file = scala.io.Source.fromFile(filePath)
    file.getLines.toVector
  }

  /**
   *
   * Times the execution of given query
   *
   * @param score - The score associated with a given query
   * @param limit - The limit associated with a given query
   * @param length - The length of the word associate with a given query
   * @param block - The block of code actually being executed
   */
  def time[R](score: Int, limit: Int, length: Int)(block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    val time = (t1 - t0) / 1000000
    sampleResults += SampleResult(score,limit,length,time)
    result
  }

  /**
   *
   * Creates a specified number of sample queries to test the query suggester
   *
   * @param words - list of word queries
   * @param qty - number of word queries to generate
   */
  def getSampleQueries(words: Vector[String], qty: Int)   {
    (1 to qty).map { i =>
      val word = Random.shuffle(words.toList).head
      val prefix = word.split(">")(0)
      val randomPrefix = prefix.take(Random.nextInt(word.size)+ 1)
      val score = Score.calculateScore(randomPrefix)
      val limit = Random.nextInt(20) + 1
      val length = prefix.size
      val command =s"./scrabble-suggester $randomPrefix $limit"
      time(score,limit,length){ command ! }
    }
  }

  /**
   *
   * Chart generater using scalax chart
   *
   * @param name - chart file name
   * @param data - relatonship data
   *
   */
  def makeChart(name: String,data: scala.collection.mutable.MutableList[(Int,Long)]) {
    val chart = XYLineChart(data)
    chart.saveAsPNG(s"../performance-charts/$name-chart.png")
  }

  /**
   * Generates charts showing relationship between (complexity, limit, size) to procwessing time
   */
  def makeCharts {
    val complexityRelation = sampleResults.sortBy(_.score).map( res => (res.score, res.time))
    val limitRelation = sampleResults.sortBy(_.score).map( res => (res.limit, res.time))
    val sizeRelation = sampleResults.sortBy(_.score).map( res => (res.length, res.time))
    makeChart("complexity", complexityRelation)
    makeChart("limit", limitRelation)
    makeChart("size", sizeRelation)
  }

  /**
   * Given a test qty: Generates a number of graphs displaying the various relationships (word size, complexity, result size) to processing time
   *
   * @param testCaseQty - Number of tests to run
   *
   */
  def runSuite(testCaseQty: Int) {
    val words = getFileLines(filePath)
    getSampleQueries(words,testCaseQty)
    makeCharts
  }

}
