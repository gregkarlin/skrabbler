package org.scrabble.profiler

/**
 * Created by gregkarlin on 11/6/14.
 */
object Profile {

  /**
   * Indexes words for efficient scrabble lookup
   *
   * @param args - args(0) - file name containing words to be indexed
   */
  def main(args: Array[String]) {
    val pT = new PerformanceTest
    pT.runSuite(args(0).toInt)
  }

}