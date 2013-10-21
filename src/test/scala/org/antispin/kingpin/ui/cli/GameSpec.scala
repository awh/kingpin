package org.antispin.kingpin.ui.cli

import org.scalatest._
import org.antispin.kingpin.model._

class GameSpec extends FlatSpec {

  "A Game" should "compute maximum score" in {
    assert(List.fill(12)(10).foldLeft(Game(Player("Test")))((g, s) => g.recordThrow(s)).totalScore === 300)
  }

  "A Game" should "compute minimum score" in {
    assert(List.fill(20)(0).foldLeft(Game(Player("Test")))((g, s) => g.recordThrow(s)).totalScore === 0)
  }

  "A Game" should "compute running totals" in {
    val expected = List(Some(30), Some(60), Some(90), Some(120), Some(150), Some(180), Some(210), Some(240), Some(270), Some(300))
    val actual = List.fill(12)(10).foldLeft(Game(Player("Test")))((g, s) => g.recordThrow(s)).runningTotals
    assert(expected === actual)
  }

  "A Game" should "compute individual frame scores" in {
    val expected = List(Some(30), Some(30), Some(30), Some(30), Some(30), Some(30), Some(30), Some(30), Some(30), Some(30))
    val actual = List.fill(12)(10).foldLeft(Game(Player("Test")))((g, s) => g.recordThrow(s)).frameScores
    assert(expected === actual)
  }

}
