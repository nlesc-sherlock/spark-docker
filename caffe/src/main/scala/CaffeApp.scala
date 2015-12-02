import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.rdd.RDD
import org.apache.spark.input.PortableDataStream
import scala.sys.process._
import java.io.File.createTempFile
import java.io.FileOutputStream
import java.nio.file.Path
import java.nio.file.Files.createTempDirectory
import java.nio.file.Files.createFile


object CaffeApp {

	def mapDocker(binaryFile :(String, PortableDataStream)):(String,String) = {
		print("The filename is: " + binaryFile._1 + " !!!\n")
		val tmpDir = createTempDirectory("caffe")
		val f = createFile(tmpDir.resolve("caffe.img"))	
		val fOut = new FileOutputStream(f.toFile())	
		fOut.write(binaryFile._2.toArray())	
		fOut.close()
		
		//Call Docker container
		// docker run -v /home/romulo/sherlock/tmp_dir:/data caffe data/f.basename
		val cmd ="docker run --rm -v " + tmpDir.toString() + ":/data caffe data/caffe.img" 
		val output = cmd.!!

		//f.deleteOnExit()
		return (binaryFile._1, output)
	}

	def main(args: Array[String]) {
		val conf = new SparkConf().setAppName("Caffe App")
		val sc = new SparkContext(conf)
		val hconf = sc.hadoopConfiguration

		val binF = sc.binaryFiles("/home/romulo/sherlock/in_dir")
		print("The list of ifiles is: ")
		val fileLengths = binF.map(T => this.mapDocker(T))
		//binF.foreach{ T => mapDocket(T._1,T._2) } 
		fileLengths.foreach{println}
		print("done!\n")
	}
}
