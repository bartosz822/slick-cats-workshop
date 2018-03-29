package model.infra

import model.domain.CatFoodPrice
import slick.jdbc.H2Profile.api._

class CatFoodPrices(tag: Tag) extends Table[CatFoodPrice](tag, "cat_food_prices") {

  def shopId = column[Long]("shop_id")
  def foodId = column[Long]("food_id")
  def price = column[BigDecimal]("price", O.SqlType("decimal(39, 20)"))

  def shopFk = foreignKey("shop_fk", shopId, CatFoodShops.query)(_.id)
  def foodFk = foreignKey("food_fk", foodId, CatFoods.query)(_.id)

  def *  =  (shopId, foodId, price) <> (CatFoodPrice.tupled, CatFoodPrice.unapply)
}

object CatFoodPrices {
  lazy val query = TableQuery[CatFoodPrices]
}
