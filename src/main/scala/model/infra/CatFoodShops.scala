package model.infra

import model.domain.CatFoodShop
import slick.jdbc.H2Profile.api._

class CatFoodShops(tag: Tag) extends IdTable[CatFoodShop](tag, "cat_food_shops") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def address = column[String]("address")

  def *  =  (id.?, name, address) <> (CatFoodShop.tupled, CatFoodShop.unapply)
}

object CatFoodShops {
  lazy val query = TableQuery[CatFoodShops]
}
