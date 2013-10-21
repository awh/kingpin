import org.antispin.kingpin.model._
import org.antispin.kingpin.ui.cli.ScoreBoard
val newGame = Game(Player("Adam"));


val game = List.fill(7)(10).foldLeft(newGame)((g, s) => g.recordThrow(s)).recordThrow(7).recordThrow(3).recordThrow(2)

