organization  := "com.girlrising"

version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
  val akkaV = "2.2.4"
  val sprayV = "1.2.1"
  Seq(
    "io.spray"                %   "spray-can"     % sprayV,
    "io.spray"                %   "spray-routing" % sprayV,
    "io.spray"                %   "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"       %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"       %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"              %%  "specs2-core"   % "2.3.7" % "test",
    "io.spray"                %%  "spray-json"    % "1.2.6",
    "org.reactivemongo"       %%  "reactivemongo" % "0.10.0",
    "com.github.nscala-time"  %%  "nscala-time"   % "0.6.0",
    "com.amazonaws"           %   "aws-java-sdk"  % "1.7.4"
  )
}

Revolver.settings
