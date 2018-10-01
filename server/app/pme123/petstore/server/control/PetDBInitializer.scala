package pme123.petstore.server.control

import cats.implicits._
import doobie._
import doobie.implicits._
import javax.inject.{Inject, Singleton}
import pme123.petstore.server.control.services.DoobieDB

import scala.concurrent.ExecutionContext

@Singleton
class PetDBInitializer @Inject()()
                                (implicit val ec: ExecutionContext)
  extends DoobieDB {

  val initCategory: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS pet_categories
      """,
    sql"""
        CREATE TABLE pet_categories (
          id   SERIAL,
          ident VARCHAR NOT NULL UNIQUE,
          name VARCHAR,
          sub_title VARCHAR
        )"""
  )

  val initProduct: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS pet_products
      """,
    sql"""
        CREATE TABLE pet_products (
          id   SERIAL,
          ident VARCHAR NOT NULL UNIQUE,
          name VARCHAR,
          category VARCHAR,
          tags VARCHAR
        )"""
  )

  val initPet: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS pets
      """,
    sql"""
        CREATE TABLE pets (
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

  val initUser: Int = initTable(
    sql"""
        DROP TABLE IF EXISTS users
      """,
    sql"""
        CREATE TABLE users (
          id   SERIAL,
          username VARCHAR NOT NULL UNIQUE,
          groups VARCHAR,
          firstName VARCHAR,
          lastName VARCHAR,
          email VARCHAR,
          avatar VARCHAR,
          language VARCHAR
         )"""
  )

  private def initTable(drop: Fragment, create: Fragment): Int = {
    (drop.update.run, create.update.run)
      .mapN(_ + _).transact(xa).unsafeRunSync
  }


}
