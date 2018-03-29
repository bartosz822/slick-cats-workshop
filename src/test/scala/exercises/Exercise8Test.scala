package exercises

import cats.data.OptionT
import cats.data.OptionT._
import cats.implicits._
import instances.DbioInstances.dbioMonad
import model.domain.Cat
import slick.dbio.DBIO
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global


class Exercise8Test extends WorkshopTest {
  import module._
  /**
    * Exercise 8 - Using OptionT
    *
    * In this exercise you're faced with a fairly simple task.
    * There are two lists of Cats names, one that contains names of existing cats
    * and the other that has some typos. What we want to achieve is fetch all cats, but only if
    * all names are correct.
    *
    * hint: https://bartosz822.github.io/slick-cats-workshop-presentation/#/10/12
    */

  def findCatByName(name: String): OptionT[DBIO, Cat] = ???

  "findCatByName" should "find cat if it's name is correct" in fixture { c =>
    import c._
    for {
      result1 <- namesWithErrors.traverse(findCatByName).getOrElse(List.empty)
      result2 <- goodNames.traverse(findCatByName).getOrElse(List.empty)
    } yield {
      result1 shouldBe empty
      result2.length shouldBe 4
    }
  }

  private def fixture(action: (FixtureContext) => DBIO[Any]): Unit = {
    rollbackWithTestData {
      val context = new FixtureContext {}
      action(context)
    }
  }

  sealed trait FixtureContext {

    val namesWithErrors = List(
      "Midnight",
      "Harley",
      "Peper",
      "Bailey",
      "Zigy",
      "Boo",
      "Mac",
    )

    val goodNames = List(
      "Bella",
      "Molly",
      "Shadow",
      "Midnight",
    )
  }

}
