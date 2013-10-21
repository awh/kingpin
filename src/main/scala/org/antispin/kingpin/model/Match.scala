package org.antispin.kingpin.model

object Match {
  // Create a new Match containing a new Game for each of the specified Players
  def apply(players: List[Player]) = new Match(players, players.map(p => (p, Game(p))).toMap)
}

/* A Match consists of a number of individual games, one per player. This class
 * is immutable - any operations which appear to make modifications return a new
 * Match instance encoding the new state.
 */
class Match(val players: List[Player], val games: Map[Player, Game]) {

  // Highest score of any game(s) in the match
  lazy val highScore: Int = games.values.map(_.totalScore).max

  // Team score - sum of all players scores
  lazy val teamScore: Int = games.values.map(_.totalScore).sum

  // List of all players who achieved the high score
  lazy val leaders: List[Player] = games.filter(pg => pg._2.totalScore == highScore).keys.toList

  // Obtain the game of a specific player
  def apply(p: Player): Game = games(p)

  // Return a new Match in which the player's throw is recorded
  def recordThrow(p: Player, pins: Int): Match = new Match(players, games.updated(p, games(p).recordThrow(pins)))

}
