package model.infra

import model.domain.{ Cat, Sex }
import slick.jdbc.H2Profile.api._

class Cats(tag: Tag) extends IdTable[Cat](tag, "cats") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def breedId = column[Long]("breed_id")
  def sex = column[Sex.Value]("sex")
  def age = column[Int]("age")

  def breedFk = foreignKey("breed_fk", breedId, Breeds.query)(_.id)

  def *  =  (id.?, name, breedId, sex, age) <> (Cat.tupled, Cat.unapply)
}

object Cats {
  lazy val query = TableQuery[Cats]
}