package pme123.petstore.server.entity


case class PageConfig(
                       context: String, // the context the project is running (play.http.context)
                       isDevMode: Boolean, // in dev mode you have fast opt JavaScripts
                       projectName: String = "play-binding-petstore" // the name of the project (Settings.projectName of your project)
                     ) {

}

object PageConfig {

}
