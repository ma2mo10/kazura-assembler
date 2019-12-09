import psp._, Sbtx._, Deps._

lazy val root = (
  project.root.noArtifacts.crossDirs.useJunit settings (
                                      name :=  "kazura-assembler",
                                   version :=  "0.0.1-SNAPSHOT",
                               description :=  "...",
                              organization :=  "fyi.brb",
                         scalaOrganization :=  "org.scala-lang",
                              scalaVersion :=  "2.12.6",
                                  licenses :=  Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
                 parallelExecution in Test :=  false,
       scalacOptions in (Compile, compile) ++= wordSeq("-language:_ -Yno-adapted-args -Ywarn-unused -Ywarn-unused-import"),
       scalacOptions in (Compile, console) ++= wordSeq("-language:_ -Yno-adapted-args"),
                   javacOptions in Compile ++= wordSeq("-nowarn -XDignore.symbol.file"),
                initialCommands in console +=  "\nimport java.nio.file._, psp._",
                       libraryDependencies ++= Seq()
  )
  plugins (
    PluginD.kindProjector
  )
)
