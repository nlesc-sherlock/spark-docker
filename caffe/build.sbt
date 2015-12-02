name := "Caffe App"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.2" % "provided"
libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3"
libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"


assemblyMergeStrategy in assembly := {
    case PathList(ps @ _*) if ps.last endsWith ".RSA" => MergeStrategy.first 
    case x =>
       val oldStrategy = (assemblyMergeStrategy in assembly).value
       oldStrategy(x)
}
