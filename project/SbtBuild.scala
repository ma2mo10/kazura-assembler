package psp

import sbt._, Keys._, Deps._

object Deps {
  object CompileD {
    def cats       = "org.typelevel"         %% "cats-core"       % "1.2.0"
    def config     = "com.typesafe"          %  "config"          % "1.3.3"
    def jawn       = "org.spire-math"        %% "jawn-parser"     % "0.13.0"
    def matryoshka = "com.slamdata"          %% "matryoshka-core" % "0.21.3"
    def pureConfig = "com.github.pureconfig" %% "pureconfig"      % "0.9.1"
    def scalaz     = "org.scalaz"            %% "scalaz-core"     % "7.2.25"
    def shapeless  = "com.chuusai"           %% "shapeless"       % "2.3.3"
  }
  object TestD {
    def junit      = "com.novocode"          %  "junit-interface" % "0.11"   % Test
    def scalacheck = "org.scalacheck"        %% "scalacheck"      % "1.14.0" % Test
    def scalaprops = "com.github.scalaprops" %% "scalaprops"      % "0.5.5"  % Test
  }
  object PluginD {
    def macroParadise = "org.scalamacros" % "paradise"       % "2.1.1" cross CrossVersion.full
    def kindProjector = "org.spire-math"  % "kind-projector" % "0.9.7" cross CrossVersion.binary
  }
}

object Sbtx {
  import Project.{ inThisBuild, inConfig, inTask, inScope }
  import Def.{ MapScoped, ScopedKey, mapScope }

  type ->[+A, +B]     = (A, B)
  type SettingOf[A]   = Def.Initialize[A]
  type TaskOf[A]      = Def.Initialize[Task[A]]
  type InputTaskOf[A] = Def.Initialize[InputTask[A]]
  type Setting_       = Setting[_]

  def javaSpecVersion = Def setting sys.props("java.specification.version")
  def wordSeq(s: String) = s split "\\s+" filterNot (_ == "") toVector

  /** sbt already adds scala-$scalaBinaryVersion to the source directories.
   *  We further cross to {main,test}-$javaSpecVersion.
   */
  def crossJavaSourceDirs(config: Configuration) = Def setting {
    (unmanagedSourceDirectories in config).value map { basis =>
      val parent = basis.getParentFile
      val grand  = parent.getParentFile

      grand / (parent.getName + "-" + javaSpecVersion.value) / basis.getName
    }
  }

  implicit class ProjectOps(val p: Project) {
    def root: Project = p in file(".")
    def typelevelArgs = wordSeq("-Ypartial-unification -Yliteral-types")
    def typelevel     = also(scalaOrganization := "org.typelevel", scalacOptions ++= typelevelArgs)

    def useJunit = also(
      TestD.junit,
      testOptions in Test += Tests.Argument(TestFrameworks.JUnit, wordSeq("-a -v -s"): _*)
    )
    def useScalacheck = also(
      TestD.scalacheck,
      testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-verbosity", "1")
    )
    def crossDirs: Project = also(
      unmanagedSourceDirectories in Compile ++= crossJavaSourceDirs(Compile).value,
         unmanagedSourceDirectories in Test ++= crossJavaSourceDirs(Test).value
    )
    def noArtifacts: Project = also(
                publish := (()),
           publishLocal := (()),
         Keys.`package` := file(""),
             packageBin := file(""),
      packagedArtifacts := Map()
    )

    implicit def idToSetting(m: ModuleID): Setting_ = libraryDependencies += m

    def also(s: Setting_, ss: Setting_ *): Project   = also(s +: ss.toSeq)
    def also(ss: Seq[Setting_]): Project             = p settings (ss: _*)
    def deps(m: ModuleID, ms: ModuleID*): Project    = also(libraryDependencies ++= m +: ms)
    def plugins(m: ModuleID, ms: ModuleID*): Project = also(m +: ms flatMap addCompilerPlugin)
  }
}
