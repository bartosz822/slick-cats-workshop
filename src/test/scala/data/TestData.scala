package data

import model.domain.Sex._
import model.domain._
import model.infra._
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global

object TestData {

  def insertTestData: DBIO[Unit] = {
    for {
      _ <- createTables
      getBreedId <- getBreeds
      _ <- getCats(getBreedId)
      getShopId <- getShops
      getFoodId <- getFoods
      _ <- savePrices(getFoodId, getShopId)
    } yield ()
  }

  private lazy val createTables: DBIO[Unit] = {
    MTable.getTables flatMap { tables =>
      if (tables.isEmpty){
        DBIO.seq(
          Breeds.query.schema.create,
          Cats.query.schema.create,
          CatFoodShops.query.schema.create,
          CatFoods.query.schema.create,
          CatFoodPrices.query.schema.create
        )
      } else {
        DBIO.successful(())
      }
    }
  }

  private def getBreeds = {
    val breeds = Seq(
      Breed(id = None, "Abyssinian", 500),
      Breed(id = None, "Devon Rex", 400),
      Breed(id = None, "Maine Coon", 760),
      Breed(id = None, "Norwegian Forest Cat", 610),
      Breed(id = None, "Ocicat", 540),
      Breed(id = None, "Persian", 620),
      Breed(id = None, "Russian Blue", 590),
      Breed(id = None, "Siamese", 560),
      Breed(id = None, "Ukrainian Levkoy", 650)
    )
    (Breeds.query ++= breeds) >> Breeds.query.result.map(_.map(breed => breed.name -> breed.id.get).toMap)
  }

  private def getCats(getBreedId: String => Long) = {
    val cats = Seq(
      Cat(id = None, "Bella", getBreedId("Ocicat"), Female, 3),
      Cat(id = None, "Molly", getBreedId("Siamese"), Female, 4),
      Cat(id = None, "Shadow", getBreedId("Norwegian Forest Cat"), Male, 7),
      Cat(id = None, "Gizmo", getBreedId("Persian"), Female, 1),
      Cat(id = None, "Midnight", getBreedId("Russian Blue"), Female, 1),
      Cat(id = None, "Harley", getBreedId("Ukrainian Levkoy"), Male, 0),
      Cat(id = None, "Pepper", getBreedId("Devon Rex"), Female, 4),
      Cat(id = None, "Bailey", getBreedId("Maine Coon"), Male, 7),
      Cat(id = None, "Pumpkin", getBreedId("Maine Coon"), Male, 5),
      Cat(id = None, "Cali", getBreedId("Persian"), Male, 3),
      Cat(id = None, "Snickers", getBreedId("Devon Rex"), Male, 4),
      Cat(id = None, "Marley", getBreedId("Abyssinian"), Male, 6),
      Cat(id = None, "Ziggy", getBreedId("Siamese"), Male, 2),
      Cat(id = None, "Boo", getBreedId("Norwegian Forest Cat"), Female, 6),
      Cat(id = None, "Noodle", getBreedId("Persian"), Male, 2),
      Cat(id = None, "Leo", getBreedId("Russian Blue"), Male, 1),
      Cat(id = None, "Panda", getBreedId("Abyssinian"), Female, 0),
      Cat(id = None, "Yuki", getBreedId("Ukrainian Levkoy"), Female, 1),
      Cat(id = None, "Ash", getBreedId("Maine Coon"), Male, 2),
      Cat(id = None, "Mac", getBreedId("Ocicat"), Male, 1)
    )
    (Cats.query ++= cats) >> Cats.query.result.map(_.map(cat => cat.name -> cat.id.get).toMap)
  }

  private def getShops = {
    val shops = Seq(
      CatFoodShop(id = None, "MaxFood", "Karmelicka 21"),
      CatFoodShop(id = None, "All4Pet", "Os. Oświecenia 43"),
      CatFoodShop(id = None, "LovelyPet", "Mickiewicza 12"),
      CatFoodShop(id = None, "CatCatCats", "Lubicz 43/1"),
      CatFoodShop(id = None, "CatFoodShop", "Mogilska 43"),
      CatFoodShop(id = None, "HappyCat", "Mikołajska 5"),
      CatFoodShop(id = None, "GrumpyCat", "Piłsudskiego 12")
    )
    (CatFoodShops.query ++= shops) >> CatFoodShops.query.result.map(_.map(shop => shop.name -> shop.id.get).toMap)
  }

  private def getFoods = {
    val foods = Seq(
      CatFood(id = None, "Eukan", 0.8),
      CatFood(id = None, "Durum", 0.78),
      CatFood(id = None, "Borg", 0.9),
      CatFood(id = None, "Elm", 0.82),
      CatFood(id = None, "KitKat", 0.76),
      CatFood(id = None, "Meat100", 0.72),
      CatFood(id = None, "XFood", 0.84),
      CatFood(id = None, "Ubix", 0.83),
      CatFood(id = None, "FooChips", 0.88),
      CatFood(id = None, "Superfull", 0.76),
      CatFood(id = None, "Premium", 0.79),
      CatFood(id = None, "Supreme", 0.7),
      CatFood(id = None, "Uber", 0.81),
      CatFood(id = None, "Abc", 0.87)
    )
    (CatFoods.query ++= foods) >> CatFoods.query.result.map(_.map(food => food.name -> food.id.get).toMap)
  }

  private def savePrices(getFoodId: String => Long, getShopId: String => Long) = {
    val prices = Seq(
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Eukan"),0.0134),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Durum"),0.0146),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Borg"),0.0205),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Elm"),0.0207),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("KitKat"),0.0292),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Meat100"),0.0135),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("XFood"),0.0251),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Ubix"),0.0251),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("FooChips"),0.0207),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Superfull"),0.0131),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Premium"),0.0239),
      CatFoodPrice(getShopId("MaxFood"),getFoodId("Supreme"),0.0248),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Ubix"),0.0279),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Meat100"),0.0116),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Superfull"),0.0103),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Eukan"),0.0131),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Elm"),0.0203),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("FooChips"),0.0208),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Durum"),0.0126),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("XFood"),0.0261),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Uber"),0.0123),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Premium"),0.0251),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Borg"),0.0228),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("KitKat"),0.0283),
      CatFoodPrice(getShopId("All4Pet"),getFoodId("Abc"), 0.0218),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Ubix"),0.0264),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Premium"),0.0228),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("FooChips"),0.0215),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Superfull"),0.0108),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Uber"),0.0142),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Durum"),0.0152),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("KitKat"),0.0287),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Meat100"),0.0122),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Supreme"),0.0221),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Abc"), 0.0228),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Elm"),0.0160),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Borg"),0.0194),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("XFood"),0.0282),
      CatFoodPrice(getShopId("LovelyPet"),getFoodId("Eukan"),0.0108),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Uber"),0.0129),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Durum"),0.0168),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Abc"), 0.0216),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Meat100"),0.0132),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("FooChips"),0.0250),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Supreme"),0.0253),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Borg"),0.0211),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("XFood"),0.0254),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Premium"),0.0217),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Ubix"),0.0268),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Superfull"),0.0114),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Elm"),0.0180),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("Eukan"),0.0149),
      CatFoodPrice(getShopId("CatCatCats"),getFoodId("KitKat"),0.0298),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Elm"),0.0170),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("FooChips"),0.0214),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Uber"),0.0117),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Supreme"),0.0244),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Eukan"),0.0128),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Ubix"),0.0256),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Borg"),0.0187),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Durum"),0.0157),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Meat100"),0.0094),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Abc"), 0.0217),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("KitKat"),0.0252),
      CatFoodPrice(getShopId("CatFoodShop"),getFoodId("Premium"),0.0287),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Supreme"),0.0229),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Ubix"),0.0238),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Premium"),0.0252),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Borg"),0.0190),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("XFood"),0.0280),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Superfull"),0.0129),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("FooChips"),0.0205),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Uber"),0.0117),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Meat100"),0.0191),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Durum"),0.0123),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Eukan"),0.0138),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("KitKat"),0.0288),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Elm"),0.0228),
      CatFoodPrice(getShopId("HappyCat"),getFoodId("Abc"), 0.0192),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Durum"),0.0120),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Borg"),0.0216),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Ubix"),0.0248),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Premium"),0.0242),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Abc"), 0.0226),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Superfull"),0.0091),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Uber"),0.0132),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("KitKat"),0.0282),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Elm"),0.0180),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("FooChips"),0.0214),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("XFood"),0.0255),
      CatFoodPrice(getShopId("GrumpyCat"),getFoodId("Supreme"),0.0238)
    )
    CatFoodPrices.query ++= prices
  }
}
