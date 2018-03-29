package services

import java.time.DayOfWeek

import model.domain.{CatFood, CatFoodShop}
import services.ShoppingScheduleService.Schedule

import scala.math.BigDecimal.RoundingMode

object ShoppingScheduleService {
  case class Schedule(shop: CatFoodShop, food: CatFood, entries: Seq[Schedule.Entry])

  object Schedule {
    case class Entry(dayOfWeek: DayOfWeek, amount: BigDecimal)
  }
}

class ShoppingScheduleService {

  /** This method creates schedule of visits in given shop for buying cat food
    *
    * @param shop shop for which schedule will be made
    * @param food food that will be bought
    * @param requiredAmount amount that should be distributed between days with good weather
    * @param goodWeatherDays days in which shop will be visited
    * @return Created Schedule*/
  def getSchedule(
    shop: CatFoodShop,
    food: CatFood,
    requiredAmount: BigDecimal,
    goodWeatherDays: Seq[DayOfWeek]
  ): ShoppingScheduleService.Schedule = {
    val entries = goodWeatherDays.map { day =>
      Schedule.Entry(day, round(requiredAmount / goodWeatherDays.size))
    }
    Schedule(shop, food, entries)
  }

  private def round(amount: BigDecimal) = {
    amount.setScale(2, RoundingMode.UP)
  }

}
