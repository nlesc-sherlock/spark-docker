import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.rdd.RDD
import org.apache.spark.input.PortableDataStream
import scala.sys.process._
import scalax.file.{Path, FileSystem}
import scalax.io.StandardOpenOption._
import scopt.OptionParser

object EnronDataApp {
	private case class Params(
		input: String = null,
		output: String = null,
		strategy: String = "topicModdeling"
	)

	def mapDocker(emailFile :(String, Byte)) {
		print("The email is: " + emailFile._1 + " !!!\n")
		val tmpDir = Path.createTempDirectory(prefix = "emails", deleteOnExit = false)
		val f = tmpDir \ "email.txt"
		val fOut = f.outputStream(WriteTruncate:_*)
		fOut.write(binaryFile._2.toArray())
		
		//Call Docker container
		val cmd ="docker run --rm -v " + tmpDir.toString() + ":/data nlesc/imagenet1000 data/caffe.img" 
		val output = cmd.!!

		tmpDir.deleteRecursively()
		return (binaryFile._1, output)
	}

	def main(args: Array[String]) {
		val defaultParams = Params()
		val parser = new OptionParser[Params]("EnronDataApp") {
			head("EnronDataApp: prepare enron data for different apps.")
			opt[Int]("strategy")
			        .text(s"data preparation strategy. default: ${defaultParams.strategy}")
			        .action((x, c) => c.copy(k = x))
			arg[String]("<input>")
				.text("input path to directory where are the files.")
				.required()
				.action((x, c) => c.copy(input = x))
			arg[String]("<output>")
				.text("Output directory for the results!!!")
				.required()
				.action((x, c) => c.copy(output = x))
		}

		parser.parse(args, defaultParams).map { params =>
		      run(params)
		}.getOrElse {
		      parser.showUsageAsError
		      sys.exit(1)
		}

	}

	def run(params: Params) {
		val conf = new SparkConf().setAppName("EnronData App")
		val sc = new SparkContext(conf)

		val emailsList = sc.sequenceFile(params.input, String.class, Byte.class)
		emailsList.map(T => this.mapDocker(T))
		print("done!\n")
	}
}
