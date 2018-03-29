package exercises

import util.WorkshopTest
import scala.concurrent.ExecutionContext.Implicits.global

class Exercise3Test extends WorkshopTest {
  import module._
  /**
    * Exercise 3 - Composing DBIO
    *
    * In this exercise you're supposed to use repositories that've been created before and compose a price list.
    * Go to the CatFoodShopsRepository (repositories.CatFoodShopsRepository) where you will find further instructions.
    *
    * hint: https://bartosz822.github.io/slick-cats-workshop-presentation/#/8/12
    */

  behavior of "CatFoodShopsRepository"

  it should "compose price list" in rollbackWithTestData {
    for {
      shop <- catFoodShopsRepository.findByName("HappyCat")
      priceList <- catFoodShopsRepository.getPriceList(shop.value.id.value)
    } yield {
      priceList.shop shouldEqual shop.value
      priceList.prices.map(entry => entry.catFood.name -> entry.pricePerGram) should contain theSameElementsAs Seq(
        "Supreme" -> 0.0229,
        "Ubix" -> 0.0238,
        "Premium" -> 0.0252,
        "Borg" -> 0.0190,
        "XFood" -> 0.0280,
        "Superfull" -> 0.0129,
        "FooChips" -> 0.0205,
        "Uber" -> 0.0117,
        "Meat100" -> 0.0191,
        "Durum" -> 0.0123,
        "Eukan" -> 0.0138,
        "KitKat" -> 0.0288,
        "Elm" -> 0.0228,
        "Abc" ->  0.0192
      )
    }
  }

}
