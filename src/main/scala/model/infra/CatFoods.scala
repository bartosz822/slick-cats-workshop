package model.infra

import model.domain.CatFood
import slick.jdbc.H2Profile.api._

class CatFoods(tag: Tag) extends IdTable[CatFood](tag, "cat_foods") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def caloriesPerGram = column[BigDecimal]("calories_per_gram")

  def *  =  (id.?, name, caloriesPerGram) <> (CatFood.tupled, CatFood.unapply)
}

object CatFoods {
  lazy val query = TableQuery[CatFoods]
}