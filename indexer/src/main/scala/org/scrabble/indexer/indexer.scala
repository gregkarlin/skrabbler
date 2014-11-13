package org.scrabble.indexer

import org.scrabble.core.Scrabble
import scala.sys.process._

/**
 * Created by gregkarlin on 11/6/14.
 */
object Indexer {

  /**
   * Indexes words for efficient scrabble lookup
   *
   * @param args - args(0) - file name containing words to be indexed
   */
  def main(args: Array[String]) {
    val s = new Scrabble
    val words = s.loadWords(args(0))
    s.serializeWords(words)
  }

}
