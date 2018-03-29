package exercises

import cats.kernel.Eq
import cats.laws.discipline.MonadTests
import cats.laws.discipline.SemigroupalTests.Isomorphisms
import cats.tests.CatsSuite
import instances.DbioInstances._
import org.scalacheck.Arbitrary
import slick.dbio.DBIO
import slick.jdbc.H2Profile.backend.Database

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class Exercise4Test extends CatsSuite {

  /**
    * Exercise 4 - Writing a monad instance for DBIO
    *
    * In this exercise you're supposed to write an instance of Monad for DBIO.
    * Implement missing functions inside DbioInstances (instances.DbioInstances).
    * Tests below will automatically check if monadic laws are fulfilled by your implementation.
    *
    * hint: https://bartosz822.github.io/slick-cats-workshop-presentation/#/10/9
    */

  private implicit val db: Database = Database.forConfig("catsDb")

  private implicit def arbitraryDbio[A: Arbitrary]: Arbitrary[DBIO[A]] = {
    Arbitrary(Arbitrary.arbitrary[A].map(DBIO.successful))
  }

  private implicit def dbioEq[A]: Eq[DBIO[A]] = (x: DBIO[A], y: DBIO[A]) => {
    Await.result(db.run((x zip y).map { case (a, b) => a == b }), Duration.Inf)
  }

  private implicit val iso: Isomorphisms[DBIO] = Isomorphisms.invariant[DBIO]

  checkAll("DBIO", MonadTests[DBIO].monad[Int, Int, String])

}
