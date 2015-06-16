import org.apache.spark._
import org.apache.spark.SparkContext._

/**
 * Created by jianzchen on 2015/6/9.
 */
object SparkJoin {
  def main(args: Array[String]): Unit = {
    if (args.length < 0) {
      System.err.println("Usage: SparkJoin")
      System.exit(1)
    }

    val conf = new SparkConf().setAppName("Johnson's Spark")

    if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local[*]")

    val sc = new SparkContext(conf)

    val file1 = sc.textFile("data/in/test1.csv")
    val file2 = sc.textFile("data/in/test2.csv")
    val file3 = sc.textFile("data/in/test3.csv")

    val output = sc.makeRDD(1 to 5).map(x => (x,null)) leftOuterJoin file1.map(x => (x.split(",")(0).toInt,x)).reduceByKey((a,b) => a) leftOuterJoin
      file2.map(x => (x.split(",")(0).toInt,x)) leftOuterJoin file3.map(x => (x.split(",")(0).toInt,x))

    output.collect.foreach(x => println("[PK] " + x._1 + " [VALUE] " + x._2._2.getOrElse("NA")))

    sc.stop()
  }
}
