/**
 * Created by jianzchen on 2015/6/15.
 */
object testWordCount {
  def main(args: Array[String]) {
    val file = List("warn 2013 msg", "warn 2012 msg",
      "error 2013 msg", "warn 2013 msg")

    def wordcount(str: String): Int = str.split(" ").count("msg" == _)

    val num = file.map(wordcount).reduceLeft(_ + _)

    println(file.map(wordcount))

    println("wordcount:" + num)
  }
}
