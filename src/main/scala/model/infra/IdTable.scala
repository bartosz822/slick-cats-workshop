package model.infra

import slick.lifted.Rep
import slick.jdbc.H2Profile.api._

abstract class IdTable[Entity](tag: Tag, name: String) extends Table[Entity](tag, name) {
  def id: Rep[Long]
}
