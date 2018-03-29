package model.domain

case class Breed(
  id: Option[Long],
  name: String,
  caloriesPerDay: BigDecimal
)
