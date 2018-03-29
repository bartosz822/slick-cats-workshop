package repositories

import model.domain.CatFood
import model.infra.CatFoods
import slick.dbio.DBIO
import slick.lifted.{ Compiled, Rep }
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext

class CatFoodsRepository extends IdRepository[CatFood, CatFoods](CatFoods.query) {

  private lazy val byNameQuery = Compiled { name: Rep[String] =>
    query.filter(_.name === name)
  }

  def findByName(name: String)(implicit ec: ExecutionContext): DBIO[Option[CatFood]] = {
    byNameQuery(name).result.headOption
  }

}
