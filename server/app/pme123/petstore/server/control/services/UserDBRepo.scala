package pme123.petstore.server.control.services

import doobie.Fragment
import doobie.implicits._
import javax.inject.Inject
import pme123.petstore.server.entity.AuthUser
import pme123.petstore.shared.services._

import scala.concurrent.{ExecutionContext, Future}


class UserDBRepo @Inject()()
                          (implicit val ec: ExecutionContext)
  extends DoobieDB {

  def insertUser(user: User): Future[Int] =
    insert(
      sql"""insert into users (username, groups, firstname, lastname, email, avatar, language)
             values (${user.username}, ${user.groupsString}, ${user.firstName}, ${user.lastName}, ${user.email}, ${user.avatar}, ${user.languageString})"""
    )

  def selectUsers(where: Fragment = fr""): Future[List[User]] =
    select((fr"""select username, groups, firstname, lastname, email, avatar, language
                     from users
         """ ++ where)
      .query[(String, String, String, String, String, String, String)]
      .map { case (username, groups, firstname, lastname, email, avatar, language) =>
        User.apply(username, groups, firstname, lastname, email, avatar, language)
      }
    )

  def findUser(ident: String): Future[Option[User]] =
    selectUsers(fr"where username = $ident")
      .map(_.headOption)

  def selectAuthUsers(where: Fragment = fr""): Future[List[AuthUser]] =
    select((fr"""select username, groups
                     from users
         """ ++ where)
      .query[(String, String)]
      .map { case (name, groups) =>
        AuthUser.apply(name, groups.splitToSet)
      })

  def selectAuthUser(username: String): Future[AuthUser] =
    selectAuthUsers(fr"where username = $username")
      .map(_.head)

  def containsAuthUser(username: String): Future[Boolean] =
    selectAuthUsers(fr"where username = $username")
      .map(_.nonEmpty)

}
