import java.util.Date

/**
 * Created by jianzchen on 2015/6/8.
 */
object testScala {
/*    def main (args: Array[String]) {
      myFunc(() => println(new java.util.Date))
  }
  def myFunc(childFunc:()=>Unit) {
    while(true) {
      childFunc()
      Thread.sleep(1000)
    }
  }*/
  def main (args: Array[String]) {
    lazy val now:Date = { new Date}
    myFunc(now)
  }
  def myFunc(x:Date): Unit = {
    while (true) {
      println(x)
      Thread sleep 1000
    }
  }
}
