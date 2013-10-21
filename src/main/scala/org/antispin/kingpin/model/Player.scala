package org.antispin.kingpin.model

object Player {
  def apply(name: String) = new Player(name)
}

class Player(val name: String) {
  override def toString = name
}
