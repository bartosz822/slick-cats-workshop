import model.domain.Cat
import model.infra.Cats
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object BasicExample extends App {

  val db = Database.forConfig("catsDb")
  val cats = Cats.query
  try {
    Await.result(db.run(DBIO.seq(
      // create the schema
      cats.schema.create,

      setup(),

      // print the cats (select * from CATS)
      cats.result.map(println)
    )), Duration.Inf)
  } finally db.close

  def setup() = {
    DBIO.seq(
      cats += Cat(
        id = None,
        name = "Filemon",
        sex = "M",
        age = 1
      ),
      cats += Cat(
        id = None,
        name = "Bonifacy",
        sex = "M",
        age = 5
      )
    )
  }

}
