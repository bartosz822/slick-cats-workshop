package services

import cats.data.Validated
import model.domain._
import cats.implicits._

object CatDietVerificationService {

  case class Context(
    getBreedForCat: Long => Breed,
    getPriceForFood: Long => Option[BigDecimal],
    isInShop: Long => Boolean,
    maxFoodPrice: BigDecimal,
    shop: CatFoodShop
  )
}

class CatDietVerificationService {

   private val maxFoodPrice = 15

  /**
    *  Verifies if given list of meals is adequate as a daily diet for a cat
    *
    * @param cat fat for which diet should be validated
    * @param meals list of meals for one day
    * @param getBreedForCat function that returns breed of a cat with given ID
    */

  def verifyDailyDiet(cat: Cat, meals: List[Meal], priceList: PriceList)(
     getBreedForCat: Long => Breed
   ): Either[String, Unit] = {
    List(
      isEnoughCalories(cat, meals, priceList)(getBreedForCat),
      isPriceOk(cat, meals, priceList),
      meals.map(_.catFood).map(food => isAvailableInShop(food, priceList)).combineAll
    ).combineAll.toEither
  }

  private def isEnoughCalories(cat: Cat, meals: List[Meal], priceList: PriceList)(
    getBreedForCat: Long => Breed
  ): Validated[String, Unit] = {
    Validated.cond(
      meals.map(meal => meal.grams * meal.catFood.caloriesPerGram).sum > getBreedForCat(cat.id.get).caloriesPerDay,
      (),
      "Not enough calories"
    )
  }

  private def isPriceOk(cat: Cat, meals: List[Meal], priceList: PriceList): Validated[String, Unit] = {
    Validated.cond(
      meals.map(meal => meal.grams * getPriceForFood(meal.catFood, priceList)).sum < maxFoodPrice,
      (),
      "This diet is too costly"
    )
  }

  private def getPriceForFood(food: CatFood, priceList: PriceList): BigDecimal = {
    priceList.prices.find(_.catFood == food).map(_.pricePerGram).getOrElse(BigDecimal(0))
  }

  private def isAvailableInShop(catFood: CatFood, priceList: PriceList): Validated[String, Unit] = {
    Validated.cond(
      priceList.prices.map(_.catFood).contains(catFood),
      (),
      s"${catFood.name} is not available to buy"
    )
  }

}
