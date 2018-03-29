package instances

import cats.{Monad, Semigroup}
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

object DbioInstances {

  /**
    * In this file you should implement Monad typeclass instance for DBIO.
    * Basic structure is already done, your task is to write 4 methods product, map, pure and flatMap.
    * Correctness of your implementation will be tested by Exercise4Test.
    *
    */
  implicit def dbioMonad(implicit ec: ExecutionContext) = new Monad[DBIO] {

    /**
      * `product` should compose two actions, keeping value produced by both of them.
      *
      */

    override def product[A, B](fa: DBIO[A], fb: DBIO[B]): DBIO[(A, B)] = ???

    /**
      * `map` should allow us to have a value in a context (F[A]) and then feed
      * that into a function that takes a normal value and returns a different one.
      *
      */
    override def map[A, B](fa: DBIO[A])(f: (A) => B): DBIO[B] = ???

    /**
      * `pure` should lift any value into the Applicative Functor.
      *
      */

    override def pure[A](x: A): DBIO[A] = ???

    /**
      * `flatMap` should allow us to have a value in a context (F[A]) and then feed
      * that into a function that takes a normal value and returns a value
      * in a context (A => F[B]).
      *
      */

    override def flatMap[A, B](fa: DBIO[A])(f: (A) => DBIO[B]): DBIO[B] = ???

    /**
      * Keep calling `f` until a `scala.util.Right[B]` is returned.
      * Implementations of this method should use constant stack space relative to `f`.
      *
      */
    override def tailRecM[A, B](a: A)(f: (A) => DBIO[Either[A, B]]): DBIO[B] = f(a).flatMap {
      case Left(nextA) => tailRecM(nextA)(f)
      case Right(b)    => pure(b)
    }(ec)
  }

  implicit def dbioSemigroup[A: Semigroup](implicit ec: ExecutionContext) = new Semigroup[DBIO[A]] {
    override def combine(fx: DBIO[A], fy: DBIO[A]): DBIO[A] = {
      import cats.syntax.semigroup._
      (fx zip fy).map { case (x, y) => x |+| y }
    }
  }

}
