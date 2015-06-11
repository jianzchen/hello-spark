import org.apache.spark._
import org.apache.spark.SparkContext._

/**
  * Created by jianzchen on 2015/5/14.
  */
object SparkDedup {
   def main(args: Array[String]): Unit = {
     if (args.length < 2) {
       System.err.println("Usage: SparkDedup <input_path> <output_path>")
       System.exit(1)
     }

     val conf = new SparkConf().setAppName("Johnson's Spark")

     if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local")
     /*if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local[*]")*/

     val spark = new SparkContext(conf)

     val input = spark.textFile(args(0))
     
     val shuffle = input.map(line => (line.split(",",2)(0), line))
     val output = shuffle.reduceByKey((a, b) => a)/*.saveAsTextFile(args(1)+"/output.dat")*/


     output.foreach(x => println(x._1))
     output.foreach(x => println(x._2))
/*     val out = new java.io.FileWriter(args(1)+"/output.dat")
     out.write(output)
     out.flush()
     out.close()*/
     spark.stop()
   }
 }
