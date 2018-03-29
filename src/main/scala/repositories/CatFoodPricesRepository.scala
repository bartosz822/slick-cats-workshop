package repositories

import model.domain.CatFoodPrice
import model.infra.CatFoodPrices
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api._
import slick.lifted.{ Compiled, Rep }

import scala.concurrent.ExecutionContext

class CatFoodPricesRepository {

  private val query = CatFoodPrices.query
  private val byCatFoodId = Compiled { (id: Rep[Long]) => query.filter(_.foodId === id) }
  private val byShopId = Compiled { (id: Rep[Long]) => query.filter(_.shopId === id) }
  private val byFoodAndShopId = Compiled { (foodId: Rep[Long], shopId: Rep[Long]) =>
    query.filter(_.shopId === shopId).filter(_.foodId === foodId)
  }

  def findByFoodAndShop(foodId: Long, shopId: Long)(implicit ec: ExecutionContext): DBIO[Option[CatFoodPrice]] = {
    byFoodAndShopId(foodId, shopId).result.headOption
  }

  def findForFood(foodId: Long)(implicit ec: ExecutionContext): DBIO[Seq[CatFoodPrice]] = {
    byCatFoodId(foodId).result
  }

  def findForShop(shopId: Long)(implicit ec: ExecutionContext): DBIO[Seq[CatFoodPrice]] = {
    byShopId(shopId).result
  }

}
