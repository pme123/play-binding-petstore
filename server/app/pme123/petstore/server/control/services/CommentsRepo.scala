package pme123.petstore.server.control.services

import pme123.petstore.shared.services.{Comment, Comments}

object CommentsRepo {

  import UserRepo._

  val comments = Map(demoCustomer ->
    Comments(userFor(demoCustomer),
    Seq(Comment(userFor(demoCustomer), "Hi do you have also Pink Rats?")))
  )

  def commentsForUser(username: String): Comments =
    comments.getOrElse(username, Comments(users(username)))

}
