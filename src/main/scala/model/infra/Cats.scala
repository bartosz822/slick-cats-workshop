package model.infra

import model.domain.Cat
import slick.jdbc.H2Profile.api._

class Cats(tag: Tag)
  extends Table[Cat](tag, "CATS") {

  // This is the primary key column:
  def id: Rep[Int] = column[Int]("CAT_ID", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("CAT_NAME")
  def sex: Rep[String] = column[String]("SEX")
  def age: Rep[Int] = column[Int]("AGE")

  // Every table needs a * projection with the same type as the table's type parameter
  def *  =  (id.?, name, sex, age) <> (Cat.tupled, Cat.unapply)
}

object Cats {
  // the base query for the Cats table
  lazy val query = TableQuery[Cats]
}