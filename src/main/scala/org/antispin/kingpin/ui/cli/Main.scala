package org.antispin.kingpin.ui.cli

import org.antispin.kingpin.model._
import scala.annotation.tailrec

object Main {

  def main(names: Array[String]) {
    if (names.size == 0) {
      println("USAGE: kingpin <player> [<player> ...]")
      sys.exit(1)
    } else {
      // Create lists of players and turns
      val players = names.map(Player.apply).toList
      val turns = List.fill(Game.TotalFrames)(players).flatten

      // Fold over turns with a function that handles I/O
      val m = turns.foldLeft(Match(players))(takeTurn)

      // Display final scoreboard and other game statistics
      ScoreBoard.lines(m).foreach(println)
      println()
      println(s"Team scored ${m.teamScore} points in total")
      println(s"Highest individual score ${m.highScore} points")
      println(s"Match won by ${m.leaders.mkString(", ")}")
    }
  }

  @tailrec
  def takeTurn(matchState: Match, p: Player): Match = {
    matchState(p).currentFrame match {
      case None => throw new IllegalStateException("Cannot take a turn in a completed game")
      case Some(currentFrame) => {
        ScoreBoard.lines(matchState).foreach(println)
        println()

        val nextMatchState = interact(matchState, p, currentFrame.remainingPins)

        nextMatchState(p).currentFrame match {
          // If this game has reached the end (no current frame) or we have
          // transitioned to the next frame then this player's turn is over;
          // terminate the recursion by returning the new Match
          case Some(UnstartedFrame) => nextMatchState
          case Some(UnstartedFinalFrame) => nextMatchState
          case None => nextMatchState

          // Player has more throws to make in their turn; continue turn over
          // updated Match state
          case _ => takeTurn(nextMatchState, p)
        }
      }
    }
  }

  @tailrec
  def interact(matchState: Match, p: Player, remainingPins: Int): Match = {
    print(s"${p.name}'s turn (0 - $remainingPins or '/' for all remaining pins): ")

    val nextMatchState = Console.in.readLine() match {
      case null => sys.exit(1)
      case "/" => matchState.recordThrow(p, remainingPins)
      case pinString => {
        try {
          val pins = pinString.toInt
          if (pins < 0 || pins > remainingPins) {
            println("Invalid pin count!")
            matchState
          } else {
            matchState.recordThrow(p, pins.toInt)
          }
        } catch {
          case nfe: NumberFormatException => {
            println("Must enter a number!")
            matchState
          }
        }
      }
    }

    if (matchState != nextMatchState) nextMatchState
    else interact(matchState, p, remainingPins)
  }

}
