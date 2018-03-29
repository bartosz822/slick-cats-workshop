package services

import model.domain.{CatFood, CatFoodShop, PriceList}

class CheapestFoodPickerService {

  /** This method picks pair of cat food and cat food shop with best possible price / calorie ratio
    *
    * @param priceLists price lists of shops that should be considered
    * @return cat food and cat food shop (returned food can be bought in cat food shop with best price)*/
  def pickCheapestFood(priceLists: Seq[PriceList]): (CatFood, CatFoodShop) = {
    val (_, foodAndShop) = priceLists.flatMap { list =>
      list.prices.map { entry =>
        val caloriePrice = entry.pricePerGram / entry.catFood.caloriesPerGram
        (caloriePrice, (entry.catFood, list.shop))
      }
    }.minBy { case (caloriePrice, _) => caloriePrice }
    foodAndShop
  }

}
