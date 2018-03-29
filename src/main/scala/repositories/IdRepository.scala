package repositories

import model.infra.IdTable
import slick.jdbc.H2Profile.api._
import slick.lifted.{ Compiled, Rep, TableQuery }

import scala.concurrent.ExecutionContext

abstract class IdRepository[Entity, Table <: IdTable[Entity]](val query: TableQuery[Table]) {

  protected def byId = Compiled { (id: Rep[Long]) => query.filter(_.id === id) }

  def save(entity: Entity)(implicit ec: ExecutionContext): DBIO[Long] = query.returning(query.map(_.id)) += entity
  def findById(id: Long)(implicit ec: ExecutionContext): DBIO[Option[Entity]] = byId(id).result.headOption
  def findAll(implicit ec: ExecutionContext): DBIO[List[Entity]] = query.result.map(_.toList)
  def deleteById(id: Long)(implicit ec: ExecutionContext): DBIO[Unit] = byId(id).delete >> DBIO.successful(())
}
