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

object CaffeApp {

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
		val conf = new SparkConf().setAppName("Caffe App")
		val sc = new SparkContext(conf)
		val hconf = sc.hadoopConfiguration

		val binF = sc.binaryFiles("/home/romulo/sherlock/in_dir")
		print("The list of ifiles is: ")
		val fileLengths = binF.map(T => this.mapDocker(T))
		fileLengths.foreach{println}
		print("done!\n")
	}
}
