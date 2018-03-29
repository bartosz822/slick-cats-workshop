package util

import data.TestData
import module.MainModule
import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Try}
import slick.jdbc.H2Profile.api._

object RollbackException extends Exception

trait WorkshopTest
  extends FlatSpec
  with Matchers
  with OptionValues
  with EitherValues {

  val module = new MainModule

  private lazy val db = Database.forConfig("catsDb")

  def rollback(action: DBIO[Any]): Unit = {
    val out = (action >> DBIO.failed(RollbackException)).transactionally
    Try(Await.result(db.run(out), Duration.Inf)) match {
      case Failure(RollbackException) => ()
      case Failure(other) => throw other
      case _ => ???
    }
  }

  def rollbackWithTestData(action: DBIO[Any]): Unit = {
    rollback {
      TestData.insertTestData >> action
    }
  }

}
