package model.infra

import model.domain.Breed
import slick.jdbc.H2Profile.api._

class Breeds(tag: Tag) extends IdTable[Breed](tag, "breeds") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def caloriesPerDay = column[BigDecimal]("calories_per_day")

  def *  =  (id.?, name, caloriesPerDay) <> (Breed.tupled, Breed.unapply)
}

object Breeds {
  lazy val query = TableQuery[Breeds]
}