import org.apache.hadoop.io.Text
import org.apache.hadoop.io.compress.SnappyCodec
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by chenjianzhou622 on 2016/2/16.
  */
object testDF {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Johnson's Spark")

    if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local")

    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    //val input:JavaRDD[Row] = sc.textFile("data/in/test1.csv").map[Row](x => Row(x.split(",")))
    val input: RDD[Row] = sc.textFile(args(0) + "/*").map(_.split(",")).map(x => Row(x(0), x(1), x(2)))
    println(input.count())
    println(input.collect().apply(1).get(1))
    //val columns = List[String]("id","name","desc").asJava
    //val df = sqlContext.createDataFrame(input,columns)
    val schema = StructType("id name desc".split(" ").map(fieldName => StructField(fieldName, StringType, true)))
    val df = sqlContext.createDataFrame(input, schema)

    val hadoopConf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(hadoopConf)
    try {
      hdfs.delete(new org.apache.hadoop.fs.Path(args(1)), true)
    } catch {
      case _: Throwable => {}
    }
    df.distinct.repartition(1).saveAsParquetFile(args(1) + "/parquet")
    df.distinct.rdd.map(x => x.mkString("\t")).repartition(1).saveAsTextFile(args(1) + "/text")
    df.distinct.map(x => (new Text(x.get(0).toString), new Text(x.mkString("\t")))).repartition(1).saveAsSequenceFile(args(1) + "/sequence", Some(classOf[SnappyCodec]))

    println(df.collect().foreach(println))

    df.registerTempTable("testTable")

    val results = sqlContext.sql("select id from testTable")

    println(results.distinct.count)
    sc.stop
  }
}
