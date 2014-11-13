package org.scrabble.core

/**
 * Created by gregkarlin on 11/6/14.
 */
object Score {
  val scoreMap = Map (
    "a" -> 1,
    "b" -> 3,
    "c" -> 3,
    "d" -> 2,
    "e" -> 1,
    "f" -> 4,
    "g" -> 2,
    "h" -> 4,
    "i" -> 1,
    "j" -> 8,
    "k" -> 5,
    "l" -> 1,
    "m" -> 3,
    "n" -> 1,
    "o" -> 1,
    "p" -> 3,
    "q" -> 10,
    "r" -> 1,
    "s" -> 1,
    "t" -> 1,
    "u" -> 1,
    "v" -> 4,
    "w" -> 4,
    "x" -> 8,
    "y" -> 4,
    "z" -> 10
  )

  def calculateScore(word: String) = word.foldLeft(0)((a: Int, b: Char) => a + scoreMap(b.toString))

}
