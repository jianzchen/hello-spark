import org.apache.spark.rdd.RDD
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row

import scala.collection.JavaConverters._

/**
  * Created by chenjianzhou622 on 2016/2/16.
  */
object testDF {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Johnson's Spark")

    if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local[*]")

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val input:JavaRDD[Row] = sc.textFile("data/in/*.csv").map[Row](x => x.split(",").getRow())

    val columns:List[String] = List[String]("id","name","desc")
    val df = sqlContext.createDataFrame(input,columns.asJava)

    df.saveAsParquetFile("data/out/result.parquet")

  }
}
