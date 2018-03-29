package model.domain

case class CatFoodPrice(
  shopId: Long,
  foodId: Long,
  pricePerGram: BigDecimal
)
