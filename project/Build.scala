import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Pheme"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.google.guava" % "guava" % "13.0.1",
      "org.reflections" % "reflections" % "0.9.8"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here
    )

}
