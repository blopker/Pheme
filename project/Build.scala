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

    val main = play.Project(appName, appVersion, appDependencies).settings(
      requireJs += "main.js",
      requireNativePath := Some("r.js"),
      publishTo := Some(Resolver.file("Bo's Repo", Path.userHome / "code" / "blopker.github.com" / "maven-repo" asFile)),
      publishMavenStyle := true
    )

}
