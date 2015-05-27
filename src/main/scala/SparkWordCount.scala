import org.apache.spark._

/**
  * Created by jianzchen on 2015/5/14.
  */
object SparkWordCount {
   def main(args: Array[String]) {
     if (args.length < 2) {
       System.err.println("Usage: SparkWordCount <input_path> <output_path>")
       System.exit(1)
     }

     val conf = new SparkConf().setAppName("Johnson's Spark")

     if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local")
     /*if (System.getProperty("os.name").toLowerCase.indexOf("win") >= 0) conf.setMaster("local[*]")*/

     val spark = new SparkContext(conf)
     /*val sc = new SparkContext(sparkConf,
       InputFormatInfo.computePreferredLocations(
         Seq(new InputFormatInfo(conf, classOf[org.apache.hadoop.mapred.TextInputFormat], inputPath))
       ))*/
     val input = spark.textFile(args(0))
     /* 1st column is the key and 2nd column is a date. Keep the latest record
     val fmt=new java.text.SimpleDateFormat("yyyymmdd")
     val data=sc.textFile("test.data").map(_.split(",")).map{line=>(line(0),fmt.parse(line(1)),line(2))}
     val out=data.groupBy(_._1).mapValues(_.foldLeft
       (("",fmt.parse("00010101"),"")){(m,l)=>if (m._2.before(l._2)) l else m}).values*/
     //val output = input.flatMap((s:String) => s.split("[\\s\\p{Punct}]")).filter(! _.isEmpty).keyBy((s:String) => s)
     val output = input.flatMap((s:String) => s.split("[\\s\\p{Punct}]")).filter(! _.isEmpty).map((_, 1)).reduceByKey(_+_)
     //output.coalesce(1).saveAsTextFile(args(1))
     //val output = input.map(_.split("\\s").size).reduce(_+_)
     //spark.emptyRDD.saveAsTextFile(args(1))
     println(output)
     val out = new java.io.FileWriter(args(1)+"/output.dat")
     out.write(output.collect.mkString("\n"))
     out.flush()
     out.close()
     spark.stop()
   }
 }
