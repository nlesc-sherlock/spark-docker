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

object CaffeApp {
	private case class Params(
		input: String = null,
		output: String = null
	)

	def mapDocker(binaryFile :(String, PortableDataStream)):(String,String) = {
		print("The filename is: " + binaryFile._1 + " !!!\n")
		val tmpDir = Path.createTempDirectory(prefix = "caffe", deleteOnExit = false)
		val f = tmpDir \ "caffe.img"
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
		val parser = new OptionParser[Params]("CaffeApp") {
			head("CaffeApp: categorize images.")
			arg[String]("<input>")
				.text("input path to directory where are the images.")
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
		val conf = new SparkConf().setAppName("Caffe App")
		val sc = new SparkContext(conf)

		val binF = sc.binaryFiles(params.input)
		print("The list of files is: ")
		val fileLengths = binF.map(T => this.mapDocker(T))
		//TODO: here we should save the result into a file
		fileLengths.foreach{println}
		print("done!\n")
	}
}
