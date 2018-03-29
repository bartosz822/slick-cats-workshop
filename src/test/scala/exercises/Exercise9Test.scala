package exercises

import cats.data.EitherT
import instances.DbioInstances.dbioMonad
import model.domain.Cat
import slick.dbio.DBIO
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global

class Exercise9Test extends WorkshopTest {
  import module._

  /**
    * Exercise 9 - Using EitherT
    *
    * Did you know that cats can be young or old?
    * Now you do, someone even has written a `matchCat` function
    * so that it could be easily determined. Implement a matchCat function
    * so that when matching multiple cats in a for comprehension if even one cat
    * is old, Left is returned, otherwise a List of young strong cats.
    *
    * hint: https://bartosz822.github.io/slick-cats-workshop-presentation/#/10/13
    */

  case class OldCat(cat: Cat)
  case class YoungCat(cat: Cat)

  def matchCat(cat: Cat): Either[OldCat, YoungCat] = {
    if(cat.age > 3){
      Left(OldCat(cat))
    } else {
      Right(YoungCat(cat: Cat))
    }
  }

  def matchCat(cat: DBIO[Cat]): EitherT[DBIO, OldCat, YoungCat] = ???


  "matching multiple cats" should "return left if one of cats is old" in fixture { c =>
    import c._
    val result = for {
      youngBella <- matchCat(bella)
      youngMolly <- matchCat(molly)
    } yield {
      List(youngBella, youngMolly)
    }
    result.isLeft.map(_ shouldBe true) >>
    result.value
  }

  "matching multiple cats" should "return list of cats if they are all young" in fixture { c =>
    import c._
    val result = for {
      youngBella <- matchCat(bella)
      youngGizmo <- matchCat(gizmo)
    } yield {
      List(youngBella, youngGizmo)
    }
    result.isRight.map(_ shouldBe true) >>
    result.value
  }



  private def fixture(action: (FixtureContext) => DBIO[Any]): Unit = {
    rollbackWithTestData {
      val context = new FixtureContext {}
      action(context)
    }
  }

  sealed trait FixtureContext {

    val bella = catsRepository.findByName("Bella").map(_.get)
    val molly = catsRepository.findByName("Molly").map(_.get)
    val gizmo = catsRepository.findByName("Gizmo").map(_.get)

  }
}
