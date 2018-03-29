package repositories

import model.domain.Cat
import model.infra.Cats
import slick.jdbc.H2Profile.api._
import slick.lifted.{ Compiled, Rep }

import scala.concurrent.ExecutionContext

class CatsRepository extends IdRepository[Cat, Cats](Cats.query) {

  private lazy val findByNameQuery = Compiled { name: Rep[String] =>
    query.filter(_.name === name)
  }

  def findByName(name: String)(implicit ec: ExecutionContext): DBIO[Option[Cat]] = {
    findByNameQuery(name).result.headOption
  }

}
