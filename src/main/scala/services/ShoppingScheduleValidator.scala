package services

import java.time.DayOfWeek

import cats.Semigroup
import cats.implicits._
import repositories.CatFoodPricesRepository
import services.ShoppingScheduleValidator.Stars
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

object ShoppingScheduleValidator {
  case class Stars(count: Int)

  implicit def starsSemigroup: Semigroup[Stars] = (x: Stars, y: Stars) => Stars(x.count + y.count)
}

class ShoppingScheduleValidator(
  pricesRepository: CatFoodPricesRepository
) {

  /** @param shoppingSchedule schedule to check
    * @return Errors or star as a reward */
  def checkOverload(shoppingSchedule: ShoppingScheduleService.Schedule): Either[List[String], Stars] = {
    shoppingSchedule.entries.map(_.amount).forall(_ < 1000)
      .orError("On some day load is too heavy")
  }

  /** @param shoppingSchedule schedule to check
    * @return Errors or star as a reward */
  def checkFrequency(shoppingSchedule: ShoppingScheduleService.Schedule) = {
    val goingOutDays = shoppingSchedule.entries.map(_.dayOfWeek)
    val goingOutSequence = DayOfWeek.values.map(goingOutDays.contains).toList
    (goingOutSequence, goingOutSequence.tail).mapN(_ && _).reduce(_ || _)
      .orError("You can't go out two days in a row!")
  }

  /** @param shoppingSchedule schedule to check
    * @return Errors or star as a reward */
  def checkShoppingOnSunday(shoppingSchedule: ShoppingScheduleService.Schedule) = {
    (!shoppingSchedule.entries.map(_.dayOfWeek).contains(DayOfWeek.SUNDAY))
      .orError("You can't go shopping on Sunday")
  }

  /** @param shoppingSchedule schedule to check
    * @return Dbio action returning errors or star as a reward */
  def canBuyFoodAtShop(shoppingSchedule: ShoppingScheduleService.Schedule)(implicit ec: ExecutionContext) = {
    pricesRepository.findByFoodAndShop(shoppingSchedule.food.id.get, shoppingSchedule.shop.id.get)
      .map(_.isDefined)
      .orError("Food is not available at shop")
  }

  /** @param shoppingSchedule schedule to check
    * @return Dbio action returning errors or star as a reward */
  def isPriceLowEnough(shoppingSchedule: ShoppingScheduleService.Schedule)(implicit ec: ExecutionContext) = {
    pricesRepository.findByFoodAndShop(shoppingSchedule.food.id.get, shoppingSchedule.shop.id.get)
      .map(_.forall(_.pricePerGram * shoppingSchedule.entries.map(_.amount).sum <= 50))
      .orError("Cost exceeds your weekly budget")
  }

  private implicit class ErrorMessageOps(cond: Boolean) {
    def orError(message: String): Either[List[String], Stars] = {
      if (cond) Right(Stars(1)) else Left(List(message))
    }
  }

  private implicit class DbioErrorMessageOps(cond: DBIO[Boolean]) {
    def orError(message: String)(implicit ec: ExecutionContext): DBIO[Either[List[String], Stars]] = {
      cond.map((c: Boolean) => c.orError(message))
    }
  }
}
