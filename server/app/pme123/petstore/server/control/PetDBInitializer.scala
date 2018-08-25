package pme123.petstore.server.control

import cats.implicits._
import doobie._
import doobie.implicits._
import javax.inject.{Inject, Singleton}

@Singleton
class PetDBInitializer @Inject() ()
  extends PetDB {

  val initCategory: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS pet_category
      """,
    sql"""
        CREATE TABLE pet_category (
          id   SERIAL,
          ident VARCHAR NOT NULL UNIQUE,
          name VARCHAR,
          sub_title VARCHAR
        )"""
  )

  val initProduct: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS pet_product
      """,
    sql"""
        CREATE TABLE pet_product (
          id   SERIAL,
          ident VARCHAR NOT NULL UNIQUE,
          name VARCHAR,
          category VARCHAR,
          tags VARCHAR
        )"""
  )

  val initPet: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS pet
      """,
    sql"""
        CREATE TABLE pet (
          id   SERIAL,
          ident VARCHAR NOT NULL UNIQUE,
          descr VARCHAR,
          price FLOAT,
          product VARCHAR,
          status VARCHAR,
          tags VARCHAR,
          photo_urls VARCHAR
        )"""
  )

  private def initTable(drop: Fragment, create: Fragment): Int = {
    (drop.update.run, create.update.run)
      .mapN(_ + _).transact(xa).unsafeRunSync
  }


}
