package exercises

import java.time.DayOfWeek

import cats.implicits._
import cats.mtl.implicits._
import instances.DbioInstances._
import model.domain.{Breed, CatFoodShop, PriceList}
import model.infra.CatFoodPrices
import services.WeatherService.Forecast
import services.{ShoppingScheduleService, WeatherService}
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api._
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global

class Exercise10Test extends WorkshopTest {
  import module._

  /**
    * Exercise 10 - Using DBIO composition for scheduling cat food shopping
    *
    * Maybe that's strange, but cats from our db need to eat something.
    * Let's schedule shopping of cat food for next week. Luckily, some logic needed for this task is already implemented.
    *
    * You can use following services: CheapestFoodPickerService, ShoppingScheduleService and WeatherService
    *
    * Your task is to get schedule that you can pin on your fridge, taking in account that:
    * - you got to satisfy calories needs of all cats in your DB for the whole week
    * - you should consider all cat food shops
    * - you want to go to the shop only on days with good weather
    * - you want to go to the shop on all days with good weather to not carry too much at one time
    *
    * Hint: check methods DBIO.from and EmptyOps.filterA from cats-mtl
    */

  val location = "Cystersów 20A"

  def getBestPossibleSchedule: DBIO[ShoppingScheduleService.Schedule] = {
    ???
  }

  "getBestPossibleSchedule" should "compose best possible schedule" in rollbackWithTestData {
    import DayOfWeek._
    for {
      scheduleOne <- getBestPossibleSchedule
      _ <- ruinPlansByRaisingPrice(scheduleOne)
      scheduleTwo <- getBestPossibleSchedule
    } yield {
      scheduleOne.shop.name shouldEqual "GrumpyCat"
      scheduleOne.food.name shouldEqual "Superfull"
      scheduleOne.entries.map(_.dayOfWeek) should contain theSameElementsAs Seq(WEDNESDAY, THURSDAY, FRIDAY)
      scheduleOne.entries.map(_.amount) should contain only BigDecimal(36350.88)

      scheduleTwo.shop.name shouldEqual "CatFoodShop"
      scheduleTwo.food.name shouldEqual "Meat100"
      scheduleTwo.entries.map(_.dayOfWeek) should contain theSameElementsAs (DayOfWeek.values().toSet - THURSDAY)
      scheduleTwo.entries.map(_.amount) should contain only BigDecimal(19185.19)
    }
  }

  private def ruinPlansByRaisingPrice(schedule: ShoppingScheduleService.Schedule) = {
    CatFoodPrices.query
      .filter(_.shopId === schedule.shop.id.get)
      .filter(_.foodId === schedule.food.id.get)
      .map(_.price).update(1.0)
  }

}