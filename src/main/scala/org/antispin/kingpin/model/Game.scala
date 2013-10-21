package org.antispin.kingpin.model

object Game {
  // Constant - total number of frames in a game
  val TotalFrames = 10

  // Create a new game with a full compliment of unstarted frames
  def apply(player: Player) = new Game(List.fill(TotalFrames - 1)(UnstartedFrame) :+ UnstartedFinalFrame)
}

/* A Game comprising a List of ten Frames.
 */
class Game(val frames: List[Frame]) {

  /* Obtain a list of individual frame scores by folding over the frame list
   * with a pattern matching function. A foldRight is used to facilitate use of the
   * efficient list cons operator instead of the more inefficient :+ required to
   * append to a list. 'None' is returned in place of a score when we encounter an
   * unstarted frame or one which is entitled to bonuses that cannot be computed yet.
   */
  lazy val frameScores: List[Option[Int]] = frames.tails.foldRight(Nil: List[Option[Int]]) { (f, s) =>
    f match {
      case Nil => s
      case (f1:Open) :: Nil => Some(f1.first + f1.second) :: s
      case (f1:UltimateStrike) :: Nil => Some(f1.first + f1.second + f1.third) :: s
      case (f1:UltimateSpare) :: Nil => Some(f1.first + f1.second + f1.third) :: s
      case (f1:Open) :: _ => Some(f1.first + f1.second) :: s
      case (f1:Strike) :: (f2:AtLeastTwoThrows) :: _ => Some(f1.first + f2.first + f2.second) :: s
      case (f1:Strike) :: (f2:Strike) :: (f3:AtLeastOneThrow) :: _ => Some(f1.first + f2.first + f3.first) :: s
      case (f1:Spare) :: (f2:AtLeastOneThrow) :: _ => Some(f1.first + f1.second + f2.first) :: s
      case _ => None :: s
    }
  }

  /* Sum of all frame scores. Note that this value can be misleading for incomplete
   * games - see the frameScores documentation for details.
   */
  lazy val totalScore: Int = frameScores.foldLeft(0) {
    case (total, Some(score)) => total + score
    case (total, None) => total
  }

  /* For each frame compute the total score from the start of the game up to and including the frame
   * under consideration. 'None' is returned in place of the running total in the case where it
   * cannot be computed - see the frameScores documentation for details.
   */
  lazy val runningTotals: List[Option[Int]] = {
    val (_, runningTotals) = frameScores.foldRight((totalScore, Nil: List[Option[Int]])) {
      case (frameScore, (runningTotal, runningTotals)) => frameScore match {
        case Some(actualScore) => (runningTotal - actualScore, Some(runningTotal) :: runningTotals)
        case None => (runningTotal, None :: runningTotals)
      }
    }
    runningTotals
  }

  /* Optionally obtain the 'current' frame (defined as the first incomplete frame in
   * the game) or None in the case that the game is finished.
   */
  lazy val currentFrame: Option[Frame] = {
    def findFirstIncompleteFrame(frames: List[Frame]): Option[Frame] = frames match {
      case Nil => None
      case (x:IncompleteFrame) :: xs => Some(x)
      case x :: xs => findFirstIncompleteFrame(xs)
    }
    findFirstIncompleteFrame(frames)
  }

  /* Obtain a new Game in which the new throw is recorded. Non tail-call direct
   * recursion is used as it's more readable than the accumulator passing version
   * and in this case the frame list length is both short and fixed.
   */
  def recordThrow(pins: Int): Game = {
    def updateFirstIncompleteFrame(frames: List[Frame]): List[Frame] = frames match {
        case Nil => throw new IllegalStateException("Game complete")
        case (x:IncompleteFrame) :: xs => x.recordThrow(pins) :: xs
        case (x:CompleteFrame) :: xs => x :: updateFirstIncompleteFrame(xs)
    }
    new Game(updateFirstIncompleteFrame(frames))
  }

}