package exercises

import model.domain.Breed
import util.WorkshopTest

import scala.concurrent.ExecutionContext.Implicits.global

class Exercise2Test extends WorkshopTest {
  import module._
  /**
    * Exercise 2 - Write a repository
    *
    * As you've learn how to write queries, now you can write a repository.
    * In this exercise you are supposed to implement methods inside BreedsRepository (repositories.BreedsRepository)
    * so that all test cases below are successful.
    *
    * hint: https://bartosz822.github.io/slick-cats-workshop-presentation/#/8/7
    */

  behavior of "BreedsRepository"

  it should "save and find breed by id" in rollbackWithTestData {
    for {
      id <- breedsRepository.save(Breed(id = None, name = "Andalusian", caloriesPerDay = 540))
      andalusian <- breedsRepository.findById(id)
    } yield {
      andalusian.value should have (
        'id (Some(id)),
        'name ("Andalusian"),
        'caloriesPerDay (540)
      )
    }
  }

  it should "delete breed by id" in rollbackWithTestData {
    for {
      id <- breedsRepository.save(Breed(id = None, name = "Andalusian", caloriesPerDay = 540))
      _ <- breedsRepository.delete(id)
      andalusian <- breedsRepository.findById(id)
    } yield {
      andalusian shouldEqual None
    }
  }

  it should "find breed by name" in rollbackWithTestData {
    for {
      breeds <- breedsRepository.findByName("Ocicat")
    } yield {
      breeds.get.name shouldBe "Ocicat"
    }
  }

  it should "search breeds by name" in rollbackWithTestData {
    for {
      breeds <- breedsRepository.search("at")
    } yield {
      breeds.map(_.name) should contain theSameElementsAs Seq("Ocicat", "Norwegian Forest Cat")
    }
  }

}
