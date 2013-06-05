import com.typesafe.config._
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

  val appName    = conf.getString("app.name")
  val appVersion = conf.getString("app.version")

  val appDependencies = Seq(
    javaCore,
    "com.google.guava" % "guava" % "13.0.1",
    "org.reflections" % "reflections" % "0.9.8",
    "ws.wamplay" %% "wamplay" % "0.1.6",
    "pheme" % "pheme-api" % "1.1.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq("Bo's Repository" at "http://blopker.github.com/maven-repo/"),
    requireJs += "main.js",
    requireNativePath := Some("r.js"),
    publishTo := Some(Resolver.file("Bo's Repo", Path.userHome / "code" / "blopker.github.com" / "maven-repo" asFile)),
    publishMavenStyle := true
  )

}
