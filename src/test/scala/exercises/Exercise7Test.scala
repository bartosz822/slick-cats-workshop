package exercises

import cats.implicits._
import instances.DbioInstances.dbioMonad
import model.domain.{ Breed, Cat, Meal }
import slick.dbio.DBIO
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global

class Exercise7Test extends WorkshopTest {
  import module._

  /**
    * Exercise 7 - Using cats for DBIO composition
    *
    * In this exercise you're faced with a problem.
    * There is a CatDietVerificationService that verifies if given diet
    * is adequate for a cat. This service requires some specific functions that need to be passed
    * as a context to work properly. Write functions in test case below that will enable this service to work properly.
    *
    * hint https://bartosz822.github.io/slick-cats-workshop-presentation/#/10/16
    */

  behavior of "CatDietVerificationService"

  /**
    * Here you should make a function that will prepare function for getting breed of a cat
    *
    * hint use 'traverse[DBIO, (Long, Breed)]'
    *
    */

  def prepareGetBreedForCat(cats: List[Cat]): DBIO[Long => Breed] = ???

  it should "correctly verify diets for cats" in fixture { c =>
    import c._

    for {
      (cats, diets) <- fixtureData
      shop <- catFoodShop
      getBreedForCat <- prepareGetBreedForCat(cats)
      priceList <- catFoodShopsRepository.getPriceList(shop.id.get)
    } yield {
      val  validationResult = diets.map{
        case (cat, meals) => catDietVerificationService.verifyDailyDiet(cat, meals, priceList)(getBreedForCat)
      }
      validationResult should contain theSameElementsAs Seq(
        Right(()),
        Left("Not enough calories"),
        Left("This diet is too costly")
      )
    }
  }

  private def fixture(action: (FixtureContext) => DBIO[Any]): Unit = {
    rollbackWithTestData {
      val context = new FixtureContext {}
      action(context)
    }
  }

  sealed trait FixtureContext {

    val catFoodShop = catFoodShopsRepository.findByName("MaxFood").map(_.get)
    val maxFoodPrice = BigDecimal(15)

    val fixtureData = for {
      bella <- catsRepository.findByName("Bella").map(_.get)
      molly <- catsRepository.findByName("Bella").map(_.get)
      shadow <- catsRepository.findByName("Shadow").map(_.get)
      eukam <- catFoodsRepository.findByName("Eukan").map(_.get)
      borg <- catFoodsRepository.findByName("Borg").map(_.get)
      durum <- catFoodsRepository.findByName("Durum").map(_.get)
    } yield {
      val cats = List(bella, molly, shadow)
      val diets = List(
        bella -> List(Meal(eukam, 200), Meal(durum, 600)),
        molly -> List(Meal(durum, 300)),
        shadow -> List(Meal(durum, 2000), Meal(borg, 100), Meal(eukam, 150))
      )
      (cats, diets)
    }
  }

}
