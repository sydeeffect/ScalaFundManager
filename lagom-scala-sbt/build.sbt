organization in ThisBuild := "org.syde"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `fundmanagerrack` = (project in file("."))
  .aggregate(`fundmanagerrack-api`, `fundmanagerrack-impl`, `fundmanagerrack-stream-api`, `fundmanagerrack-stream-impl`)

lazy val `fundmanagerrack-api` = (project in file("fundmanagerrack-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `fundmanagerrack-impl` = (project in file("fundmanagerrack-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`fundmanagerrack-api`)

lazy val `fundmanagerrack-stream-api` = (project in file("fundmanagerrack-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `fundmanagerrack-stream-impl` = (project in file("fundmanagerrack-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`fundmanagerrack-stream-api`, `fundmanagerrack-api`)
