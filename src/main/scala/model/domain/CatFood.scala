package model.domain

case class CatFood(
  id: Option[Long],
  name: String,
  caloriesPerGram: BigDecimal
)
