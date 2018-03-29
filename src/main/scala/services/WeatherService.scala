package services

import java.time.DayOfWeek

import services.WeatherService.Forecast

import scala.concurrent.Future
import cats.syntax.apply._
import cats.instances.list._

object WeatherService {
  object Forecast extends Enumeration {
    val Good = Value(1, "good")
    val Bad = Value(2, "bad")
  }
}

class WeatherService {

  def weekDays: List[DayOfWeek] = DayOfWeek.values().toList

  /** This method connects with external weather service and checks if weather will be good or bad
    *
    * @param weekDay week day for which we want to check weather
    * @param startAddress starting point of route for which we want to check weather
    * @param endAddress end point of route for which we want to check weather
    * @return Future[Forecast.Value] - forecast for given day and route*/
  def getWeatherOnRoute(weekDay: DayOfWeek, startAddress: String, endAddress: String): Future[Forecast.Value] = {
    val weatherFactor = (startAddress.toList, endAddress.toList).mapN(_ ^ _).sum
    Future.successful(Forecast(((weatherFactor >> weekDay.getValue) % 2) + 1))
  }

}
