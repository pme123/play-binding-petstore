package pme123.petstore.server.control.services

import cats.effect.IO
import doobie.Fragment
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

import scala.concurrent.{ExecutionContext, Future, blocking}

trait DoobieDB {

  implicit def ec: ExecutionContext

  protected val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/postgres",
    "postgres",
    "3sf2reRer"
  )
  /*
  H2
    protected val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
      "org.h2.Driver",
      "jdbc:h2:~/dev/servers/h2",
      "sa",
      ""
    )
    */

  protected def insert(sql: Fragment): Future[Int] =
    Future(
      blocking(
        sql
          .update
          .run
          .transact(xa)
          .unsafeRunSync
      ))

  protected def select[T](query: Query0[T]): Future[List[T]] =
    Future(
      blocking(
        query
          .stream
          .transact(xa)
          .take(50)
          .compile
          .toList
          .unsafeRunSync
      ))

}
