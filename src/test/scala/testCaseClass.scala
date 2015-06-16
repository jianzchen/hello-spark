/**
 * Created by jianzchen on 2015/6/15.
 */
object testCaseClass {
  def main(args: Array[String]) {
    abstract class Expr

    case class FibonacciExpr(n: Int) extends Expr {
      require(n >= 0)
    }

    case class SumExpr(a: Expr, b: Expr) extends Expr

    def value(input: Expr): Int = input match {
      case FibonacciExpr(0) => 0
      case FibonacciExpr(1) => 1
      case FibonacciExpr(n) =>
        value(SumExpr(FibonacciExpr(n - 1), FibonacciExpr(n - 2)))
      case SumExpr(a, b) => value(a) + value(b)
      case _ => 0
    }
    println(value(FibonacciExpr(-33)))
  }

}
