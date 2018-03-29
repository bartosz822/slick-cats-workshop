package model.domain

case class PriceList(
  shop: CatFoodShop,
  prices: Seq[PriceList.Entry]
)

object PriceList {
  case class Entry(
    catFood: CatFood,
    pricePerGram: BigDecimal
  )
}
