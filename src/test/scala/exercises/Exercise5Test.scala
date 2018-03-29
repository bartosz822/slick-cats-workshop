package exercises

import cats.implicits._
import model.domain.CatFood
import model.infra.CatFoods
import instances.DbioInstances.dbioMonad
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api._
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global

class Exercise5Test extends WorkshopTest {

  /**
    * Exercise 5 - sequence for the win
    *
    * Some nasty programmer has written foodWithMoreCalories function that looks in db
    * for food that has more calories per gram, but only if it has some defined threshold
    * (no idea why). Can you use this function to find more energetic cat food without changing
    * this function?
    *
    * hint https://bartosz822.github.io/slick-cats-workshop-presentation/#/10/16
    */

  def foodWithMoreCalories(threshold: Option[BigDecimal]): Option[DBIO[Seq[CatFood]]] = {
    threshold.map(value => CatFoods.query.filter(_.caloriesPerGram > value).result)
  }

  def getBetterFood(catFood: CatFood): DBIO[Option[Seq[CatFood]]] = ???

  "foodWithMoreCalories" should "find catfood with more calories per gram" in fixture { c =>
    import c._
    for {
     food <- someFood
     betterFood <- getBetterFood(food)
    } yield {
      betterFood shouldBe defined
      betterFood.get.forall(_.caloriesPerGram > food.caloriesPerGram) shouldBe true
    }
  }

  private def fixture(action: (FixtureContext) => DBIO[Any]): Unit = {
    rollbackWithTestData {
      val context = new FixtureContext {}
      action(context)
    }
  }

  sealed trait FixtureContext {
    def someFood: DBIO[CatFood] = CatFoods.query.result.headOption.map(_.get)
  }

}
