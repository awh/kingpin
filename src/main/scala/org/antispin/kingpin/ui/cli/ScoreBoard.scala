package org.antispin.kingpin.ui.cli

import org.antispin.kingpin.model._

object ScoreBoard {

  // Render the specified match state as a multiline ASCII scoreboard representation.
  def lines(m: Match): List[String] =
    "         |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |   10  | Total " ::
      m.players.flatMap(p => renderGame(p, m(p)))

  // Render an individual Frame in a Player's throws line on the scoreboard
  def renderFrame(f: Frame) = f match {
    case InProgressFrame(first) => s"$first  "
    case Strike(_) => "X  "
    case Spare(first, _) => s"$first /"
    case Open(first, second) => s"$first $second"
    case InProgressUltimateFrame(first) => s"$first    "
    case InProgressUltimateStrike(_) => s"X    "
    case InProgressUltimateStrikeBonus(_, 10) => s"X X  "
    case InProgressUltimateStrikeBonus(_, second) => s"X $second  "
    case InProgressUltimateSpare(first, _) => s"$first /  "
    case UltimateStrike(_, 10, 10) => "X X X"
    case UltimateStrike(_, 10, third) => s"X X $third"
    case UltimateStrike(_, second, 10) => s"X $second X"
    case UltimateStrike(_, second, third) => s"X $second $third"
    case UltimateSpare(first, _, 10) => s"$first / X"
    case UltimateSpare(first, _, third) => s"$first / $third"
    case UnstartedFrame => "   "
    case UnstartedFinalFrame => "     "
  }

  // Render a Player's throws line on the scoreboard.
  def renderThrows(p: Player, g: Game) =
    f"${p.name}%8s | " + g.frames.map(renderFrame).mkString(" | ") + " |"

  // Render a Player's running totals.
  def renderScores(g: Game) = {
    val runningTotals = g.runningTotals.map {
      case Some(score) => f"$score%3d"
      case None => "   "
    }
    " " * 8 + " | " + runningTotals.mkString(" | ") + f"   |   ${g.totalScore}%3d"
  }

  // Render a divider followed by a Player's throws and running totals.
  def renderGame(p: Player, g: Game) =
    "---------+-----+-----+-----+-----+-----+-----+-----+-----+-----+-------+------" ::
    renderThrows(p, g) ::
    renderScores(g) :: Nil

}
