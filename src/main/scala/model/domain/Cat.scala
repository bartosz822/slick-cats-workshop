package model.domain
import slick.jdbc.H2Profile.api._


object Sex extends Enumeration {
  type Sex = Value
  val Male, Female = Value

  implicit val mapper = MappedColumnType.base[Sex, Int](
    enum => enum.id,
    int => Sex(int)
  )

}



case class Cat(
  id: Option[Long],
  name: String,
  breedId: Long,
  sex: Sex.Value,
  age: Int
)
