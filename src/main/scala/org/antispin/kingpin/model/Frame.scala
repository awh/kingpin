package org.antispin.kingpin.model

sealed trait Frame {
  val TotalPins: Int = 10
  // Number of pins remaining in this frame
  val remainingPins: Int
}

sealed trait IncompleteFrame extends Frame {
  // Record a throw, returning a new Frame state
  def recordThrow(pins: Int): Frame
}

sealed trait CompleteFrame extends Frame {
  val remainingPins = 0
}

sealed trait AtLeastOneThrow {
  val first: Int
}

sealed trait AtLeastTwoThrows extends AtLeastOneThrow {
  val second: Int
}

sealed trait AtLeastThreeThrows extends AtLeastTwoThrows {
  val third: Int
}

case class Strike(first:Int) extends CompleteFrame with AtLeastOneThrow
case class Spare(first: Int, second: Int) extends CompleteFrame with AtLeastTwoThrows
case class Open(first: Int, second: Int) extends CompleteFrame with AtLeastTwoThrows
case class UltimateStrike(first: Int, second: Int, third: Int) extends CompleteFrame with AtLeastThreeThrows
case class UltimateSpare(first: Int, second: Int, third: Int) extends CompleteFrame with AtLeastThreeThrows

case object UnstartedFrame extends IncompleteFrame {
  val remainingPins = TotalPins
  def recordThrow(first: Int): Frame = {
    first match {
      case TotalPins => Strike(first)
      case n if 0 until TotalPins contains n => InProgressFrame(first)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }
}

case class InProgressFrame(first: Int) extends IncompleteFrame with AtLeastOneThrow {
  val remainingPins = TotalPins - first
  def recordThrow(second: Int): Frame = {
    second match {
      case n if n == remainingPins => Spare(first, second)
      case n if 0 until remainingPins contains n => Open(first, second)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }
}

case object UnstartedFinalFrame extends IncompleteFrame {
  val remainingPins = TotalPins
  def recordThrow(first: Int): Frame = {
    first match {
      case TotalPins => InProgressUltimateStrike(first)
      case n if 0 until TotalPins contains n => InProgressUltimateFrame(first)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }  
}

case class InProgressUltimateFrame(first: Int) extends IncompleteFrame with AtLeastOneThrow {
  val remainingPins = TotalPins - first
  def recordThrow(second: Int): Frame = {
    second match {
      case n if n == remainingPins => InProgressUltimateSpare(first, second)
      case n if 0 until remainingPins contains n => Open(first, second)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }
}

case class InProgressUltimateStrike(first: Int) extends IncompleteFrame with AtLeastOneThrow {
  val remainingPins = TotalPins
  def recordThrow(second: Int): Frame = {
    second match {
      case n if 0 to TotalPins contains n => InProgressUltimateStrikeBonus(first, second)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }
}

case class InProgressUltimateStrikeBonus(first: Int, second: Int) extends IncompleteFrame with AtLeastTwoThrows {
  val remainingPins = if(second == TotalPins) TotalPins else TotalPins - second
  def recordThrow(third: Int): Frame = {
    third match {
      case n if 0 to TotalPins contains n => UltimateStrike(first, second, third)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }
}

case class InProgressUltimateSpare(first: Int, second: Int) extends IncompleteFrame with AtLeastTwoThrows {
  val remainingPins = TotalPins
  def recordThrow(third: Int): Frame = {
    third match {
      case n if 0 to TotalPins contains n => UltimateSpare(first, second, third)
      case _ => throw new IllegalArgumentException("Score out of range")
    }
  }
}