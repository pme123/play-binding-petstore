import cats.effect._
import doobie._
import doobie.implicits._

val xa = Transactor.fromDriverManager[IO](
  "org.h2.Driver", // driver classname
  "jdbc:h2:!file:/Users/mpa/dev/servers", // connect URL (driver-specific)
  "sa",              // user
  ""                       // password
)
val drop =
  sql"""
    DROP TABLE IF EXISTS person
  """.update.run

val create =
  sql"""
    CREATE TABLE person (
      id   SERIAL,
      name VARCHAR NOT NULL UNIQUE,
      age  SMALLINT
    )
  """.update.run

(drop).transact(xa).unsafeRunSync


def insert1(name: String, age: Option[Short]): Update0 =
  sql"insert into person (name, age) values ($name, $age)".update

insert1("Alice", Some(12)).run.transact(xa).unsafeRunSync

