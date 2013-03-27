import com.github.retronym.SbtOneJar // Import one-jar
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "Pheme"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      javaCore,
      "com.google.guava" % "guava" % "13.0.1",
      "org.reflections" % "reflections" % "0.9.8"
    )

    // Make standard settings and add them to Play
    def standardSettings = Seq(
      exportJars := true
    ) ++ Defaults.defaultSettings

    val main = play.Project(appName, appVersion, appDependencies, settings = standardSettings ++ SbtOneJar.oneJarSettings).settings(
      requireJs += "main.js"
    )

}
