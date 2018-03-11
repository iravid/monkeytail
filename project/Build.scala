import com.typesafe.sbt.pgp.PgpKeys
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object Build extends AutoPlugin {

  override def trigger = AllRequirements
  override def requires = JvmPlugin

  object autoImport {
    val org = "com.sksamuel.monkeytail"
    val CatsVersion = "1.0.1"
    val ScalatestVersion = "3.0.4"
  }

  import autoImport._

  override def projectSettings = Seq(
    organization := org,
    scalaVersion := "2.11.12",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    publishMavenStyle := true,
    resolvers += Resolver.mavenLocal,
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    publishArtifact in Test := false,
    fork := false,
    parallelExecution := false,
    parallelExecution in ThisBuild := false,
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    sbtrelease.ReleasePlugin.autoImport.releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    sbtrelease.ReleasePlugin.autoImport.releaseCrossBuild := true,
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    javacOptions := Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      "org.typelevel"                         %% "cats-core"                % CatsVersion,
      "org.scalatest"                         %% "scalatest"                % ScalatestVersion      % "test"
    ),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := {
      <url>https://github.com/sksamuel/monkeytail</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:sksamuel/monkeytail.git</url>
          <connection>scm:git@github.com:sksamuel/monkeytail.git</connection>
        </scm>
        <developers>
          <developer>
            <id>sksamuel</id>
            <name>sksamuel</name>
            <url>http://github.com/sksamuel</url>
          </developer>
        </developers>
    }
  )
}
