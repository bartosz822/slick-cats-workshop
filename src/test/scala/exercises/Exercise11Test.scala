package exercises

import java.time.DayOfWeek

import cats.data.{EitherT, Validated}
import cats.implicits._
import instances.DbioInstances._
import services.ShoppingScheduleService
import services.ShoppingScheduleService.Schedule.Entry
import services.ShoppingScheduleValidator.Stars
import slick.dbio.DBIO
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global


class Exercise11Test extends WorkshopTest {
  import module._

  /**
    * Exercise 11 - Either vs Validated
    *
    * We have some shopping schedules to check.
    * For every condition out of 5 our validation service can either give us stars (they can be summed up since they are Semigroup) or List of errors.
    * When everything is fine with the schedule, your task is to just collect all the stars.
    * When there are problems, you should either return first encountered error (validateSchedule)
    * or collect all errors with the schedule (validateScheduleCollect)
    *
    * Hint: Check cats.data.Validated and Semigroup typeclass
    * https://bartosz822.github.io/slick-cats-workshop-presentation/#/10/19
    */

  def validateSchedule(schedule: ShoppingScheduleService.Schedule): EitherT[DBIO, List[String], Stars] = {
    ???
  }

  def validateScheduleCollect(schedule: ShoppingScheduleService.Schedule): DBIO[Validated[List[String], Stars]] = {
    ???
  }

  "validate schedule" should "recognize valid schedule" in fixture { c =>
    import c._
    for {
      result <- validateSchedule(fairSchedule).value
    } yield {
      result.right.value shouldEqual Stars(5)
    }
  }

  "validate schedule" should "fail fast on overloaded weekend schedule" in fixture { c =>
    import c._
    for {
      result <- validateSchedule(weekendSchedule).value
    } yield {
      result.left.value should contain oneElementOf List("You can't go shopping on Sunday", "On some day load is too heavy")
    }
  }

  "validate schedule" should "fail fast on no food at shop schedule" in fixture { c =>
    import c._
    for {
      result <- validateSchedule(noFoodAtShopSchedule).value
    } yield {
      result.left.value should contain oneElementOf List("Food is not available at shop", "On some day load is too heavy")
    }
  }

  "validate schedule collect" should "recognize valid schedule" in fixture { c =>
    import c._
    for {
      result <- validateScheduleCollect(fairSchedule)
    } yield {
      result.toOption.value shouldEqual Stars(5)
    }
  }

  "validate schedule collect" should "collect errors on overloaded weekend schedule" in fixture { c =>
    import c._
    for {
      result <- validateScheduleCollect(weekendSchedule)
    } yield {
      Seq("You can't go shopping on Sunday", "On some day load is too heavy") should contain theSameElementsAs result.swap.toOption.value
    }
  }

  "validate schedule collect" should "collect errors on no food at shop schedule" in fixture { c =>
    import c._
    for {
      result <- validateScheduleCollect(noFoodAtShopSchedule)
    } yield {
      Seq("Food is not available at shop", "On some day load is too heavy") should contain theSameElementsAs result.swap.toOption.value
    }
  }

  private def fixture(action: (FixtureContext) => DBIO[Any]): Unit = {
    rollbackWithTestData {
      for {
        eukan <- catFoodsRepository.findByName("Eukan").map(_.get)
        maxFood <- catFoodShopsRepository.findByName("MaxFood").map(_.get)
        grumpyCat <- catFoodShopsRepository.findByName("GrumpyCat").map(_.get)
        fairSchedule = ShoppingScheduleService.Schedule(maxFood, eukan, Seq(
          Entry(DayOfWeek.MONDAY, 400),
          Entry(DayOfWeek.THURSDAY, 700)
        ))
        weekendSchedule = ShoppingScheduleService.Schedule(maxFood, eukan, Seq(
          Entry(DayOfWeek.SATURDAY, 1200),
          Entry(DayOfWeek.SUNDAY, 300)
        ))
        noFoodAtShopSchedule = ShoppingScheduleService.Schedule(grumpyCat, eukan, Seq(
          Entry(DayOfWeek.SATURDAY, 1200),
          Entry(DayOfWeek.MONDAY, 300)
        ))
        context = FixtureContext(fairSchedule, weekendSchedule, noFoodAtShopSchedule)
        _ <- action(context)
      } yield ()
    }
  }

  case class FixtureContext(
    fairSchedule: ShoppingScheduleService.Schedule,
    weekendSchedule: ShoppingScheduleService.Schedule,
    noFoodAtShopSchedule: ShoppingScheduleService.Schedule
  )

}
