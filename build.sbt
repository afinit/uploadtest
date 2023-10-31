val Http4sVersion = "0.23.23"
val Http4sBlazeVersion      = "0.23.15"
val CirceVersion = "0.14.6"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.4.11"
val MunitCatsEffectVersion = "1.0.7"



lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "uploadtest",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.12",
    scalacOptions ++= Seq(
      "-deprecation",
      "-Ywarn-unused",
      "-Ywarn-unused:privates",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:imports",
      "-feature",
      "-unchecked",
    ),
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-server" % Http4sBlazeVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion         % Runtime,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
  )
