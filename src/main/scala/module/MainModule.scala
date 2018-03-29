package module

import repositories._
import services._

class MainModule {

  val breedsRepository = new BreedsRepository
  val catsRepository = new CatsRepository
  val catFoodsRepository = new CatFoodsRepository
  val catFoodPricesRepository = new CatFoodPricesRepository
  val catFoodShopsRepository = new CatFoodShopsRepository(catFoodsRepository)

  val catDietVerificationService = new CatDietVerificationService
  val cheapestFoodPickerService = new CheapestFoodPickerService
  val shoppingScheduleService = new ShoppingScheduleService
  val weatherService = new WeatherService
  val shoppingScheduleValidator = new ShoppingScheduleValidator(catFoodPricesRepository)

}
