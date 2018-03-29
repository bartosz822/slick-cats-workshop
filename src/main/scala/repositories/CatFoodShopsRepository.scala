package repositories

import model.domain.{ CatFoodPrice, CatFoodShop, PriceList }
import model.infra.{ CatFoodPrices, CatFoodShops }
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext

class CatFoodShopsRepository(
  catFoodsRepository: CatFoodsRepository
) extends IdRepository[CatFoodShop, CatFoodShops](CatFoodShops.query) {

  def findByName(name: String)(implicit ec: ExecutionContext): DBIO[Option[CatFoodShop]] = {
    query.filter(_.name === name).result.headOption
  }

  /**
    * Write a method that will compose PriceList for a shop with given Id
    *
    * Hint: you can try for {} yield {} syntax on DBIO
    * Hint#2: try DBIO.sequence
    * @param shopId id of shop for which prices should be found
    */
  def getPriceList(shopId: Long)(implicit ec: ExecutionContext): DBIO[PriceList] = {
    ???
  }

  private def getEntries(shopId: Long)(implicit ec: ExecutionContext): DBIO[Seq[PriceList.Entry]] = {
    ???
  }

  private def toEntry(catFoodPrice: CatFoodPrice)(implicit ec: ExecutionContext): DBIO[PriceList.Entry] = {
    ???
  }

}
