import java.lang.RuntimeException

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.hive.HiveContext
import scala.sys.process._
import scalax.file.{Path}
import scalax.io.StandardOpenOption._
import scopt.OptionParser
import org.apache.hadoop.io._
import org.apache.spark.sql.types.{StructType,StructField,StringType};
import org.apache.spark.sql.Row;


object CaffeAppSeq {
	private case class Params(
		input: String = null,
		output: String = null
	)

	def classifyImage(binaryFile :(Text, BytesWritable)): (String, String) = {
		print("The filename is: " + binaryFile._1 + " !!!\n")
		val tmpDir = Path.createTempDirectory(prefix = "caffe", deleteOnExit = false)
		val f = tmpDir \ "caffe.img"
		val fOut = f.outputStream(WriteTruncate:_*)
		fOut.write(binaryFile._2.getBytes)
		
		//Call Docker container
		val cmd ="docker run --rm -v " + tmpDir.path + ":/data nlesc/imagenet1000 data/caffe.img"
		var output = ""
		try {
			output = cmd.!!
		} catch {
			case msg: RuntimeException => {
				println(msg)
				println(cmd)
			}
		}


		tmpDir.deleteRecursively()
		return (binaryFile._1.toString, output)
	}

	def main(args: Array[String]) {
		val defaultParams = Params()
		val parser = new OptionParser[Params]("CaffeApp") {
			head("CaffeApp: categorize images.")
			arg[String]("<input>")
				.text("sequence file with filename and filecontent of the images.")
				.required()
				.action((x, c) => c.copy(input = x))
			arg[String]("<output>")
				.text("orc file with filename and labels")
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
    val sqlContext = new HiveContext(sc)
		val images = sc.sequenceFile(params.input, classOf[Text], classOf[BytesWritable])

		val labels = images.map(image => this.classifyImage(image)).map(row => Row(row._1, row._2))

    val schemaString = "filename labels"
    val labelsSchema = StructType(
        schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))
    sqlContext.createDataFrame(labels, labelsSchema).write.format("orc").mode("append").save(params.output)
	}
}
