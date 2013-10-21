import org.antispin.kingpin.model._
import org.antispin.kingpin.model.InProgressUltimateFrame
import org.antispin.kingpin.model.Open
import org.antispin.kingpin.model.Spare
import org.antispin.kingpin.model.Strike
import org.antispin.kingpin.ui.cli.ScoreBoard
import org.scalatest._

class ScoreBoardSpec extends FlatSpec {

  "A ScoreBoard" should "render Strike" in {
    assert(ScoreBoard.renderFrame(Strike(10)) === "X  ")
  }

  "A ScoreBoard" should "render Spare" in {
    assert(ScoreBoard.renderFrame(Spare(0, 10)) === "0 /")
    assert(ScoreBoard.renderFrame(Spare(1, 9)) === "1 /")
    assert(ScoreBoard.renderFrame(Spare(2, 8)) === "2 /")
    assert(ScoreBoard.renderFrame(Spare(3, 7)) === "3 /")
    assert(ScoreBoard.renderFrame(Spare(4, 6)) === "4 /")
    assert(ScoreBoard.renderFrame(Spare(5, 5)) === "5 /")
    assert(ScoreBoard.renderFrame(Spare(6, 4)) === "6 /")
    assert(ScoreBoard.renderFrame(Spare(7, 3)) === "7 /")
    assert(ScoreBoard.renderFrame(Spare(8, 2)) === "8 /")
    assert(ScoreBoard.renderFrame(Spare(9, 1)) === "9 /")
  }

  "A ScoreBoard" should "render Open" in {
    assert(ScoreBoard.renderFrame(Open(0, 0)) === "0 0")
  }

  "A ScoreBoard" should "render InProgressFrame" in {
    assert(ScoreBoard.renderFrame(InProgressFrame(0)) === "0  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(1)) === "1  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(2)) === "2  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(3)) === "3  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(4)) === "4  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(5)) === "5  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(6)) === "6  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(7)) === "7  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(8)) === "8  ")
    assert(ScoreBoard.renderFrame(InProgressFrame(9)) === "9  ")
  }

  "A ScoreBoard" should "render InProgressFinalFrame" in {
    assert(ScoreBoard.renderFrame(InProgressUltimateFrame(3)) === "3    ")
  }

  "A ScoreBoard" should "render FinalStrike" in {
    assert(ScoreBoard.renderFrame(UltimateStrike(10, 10, 10)) === "X X X")
    assert(ScoreBoard.renderFrame(UltimateStrike(10, 9, 10)) === "X 9 X")
    assert(ScoreBoard.renderFrame(UltimateStrike(10, 10, 9)) === "X X 9")
    assert(ScoreBoard.renderFrame(UltimateStrike(10, 9, 9)) === "X 9 9")
  }

  "A ScoreBoard" should "render FinalSpare" in {
    assert(ScoreBoard.renderFrame(UltimateSpare(0, 10, 10)) === "0 / X")
    assert(ScoreBoard.renderFrame(UltimateSpare(0, 10, 9)) === "0 / 9")
  }


}
