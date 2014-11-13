package org.scrabble.suggester

import org.scrabble.core.Scrabble

object Suggester {

  /**
   *
   * @param args - arg1 - substring to lookup, arg2 - number of results requested
   * @return - returns an ordered list of highest scoring words that match substring
   */
  def main(args: Array[String]) {
    val s = new Scrabble
    val res = s.query(args(0),args(1).toInt)
    res.map(println(_))
   }
    
}

// vim: set ts=2 sw=2 et:
