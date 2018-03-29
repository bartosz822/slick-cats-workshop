package exercises

import model.domain.Cat
import model.infra.{ Breeds, Cats }
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api._
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global

class Exercise1Test extends WorkshopTest {

  /**
    * Exercise 1 - Play with slick queries
    *
    * In this exercise you will learn some basic ways of fetching data with use of slick queries.
    * Test cases below have method left for you to implement. Good luck!
    *
    * hint: https://bartosz822.github.io/slick-cats-workshop-presentation/#/8/7
    */

  /** find all cats that are older than 5*/
  def findOldCats: DBIO[Seq[Cat]] = {
    ???
  }

  /** find all male cats that are older than 5*/
  def findOldMaleCats: DBIO[Seq[Cat]] = {
    ???
  }

  /** find all persian cats*/
  def findPersianCats: DBIO[Seq[Cat]] = {
    ???
  }

  /** find how many calories are needed to feed all old cats*/
  def findCaloricNeedsForOldCats: DBIO[BigDecimal] = {
    ???
  }

  /** find how many calories are needed for cats grouped by their breed*/
  def findCaloricNedsForBreeds: DBIO[Seq[(String, BigDecimal)]] = {
    ???
  }

  "findOldCats" should "return cats with age greater than 5" in rollbackWithTestData {
    for {
      foundCats <- findOldCats
    } yield {
      foundCats.map(_.name) should contain theSameElementsAs Seq("Shadow", "Bailey", "Marley", "Boo")
    }
  }

  "findOldMaleCats" should "return male cats with age greater than 5" in rollbackWithTestData {
    for {
      foundCats <- findOldMaleCats
    } yield {
      foundCats.map(_.name) should contain theSameElementsAs Seq("Shadow", "Bailey", "Marley")
    }
  }

  "findPersianCats" should "return persian cats" in rollbackWithTestData {
    for {
      foundCats <- findPersianCats
    } yield {
      foundCats.map(_.name) should contain theSameElementsAs Seq("Gizmo", "Cali", "Noodle")
    }
  }

  "findCaloricNeedsForOldCats" should "sum of daily caloric needs for old cats" in rollbackWithTestData {
    for {
      caloriesSum <- findCaloricNeedsForOldCats
    } yield {
      caloriesSum shouldBe 2480
    }
  }

  "findCaloricNeedsForBreeds" should "sum of daily caloric needs for old cats" in rollbackWithTestData {
    for {
      caloriesSum <- findCaloricNedsForBreeds
    } yield {
      caloriesSum should contain theSameElementsAs Seq(
        "Abyssinian" -> 1000,
        "Devon Rex" -> 800,
        "Maine Coon" -> 2280,
        "Norwegian Forest Cat" -> 1220,
        "Ocicat" -> 1080,
        "Persian" -> 1860,
        "Russian Blue" ->1180,
        "Siamese" -> 1120,
        "Ukrainian Levkoy" -> 1300
      )
    }
  }

}
