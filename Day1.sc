// https://adventofcode.com/2024/day/1

def solve(input: String): (Int, Int) = {
  val pairs =
    for {
      line <- input.split('\n')
      Array(l, r) = line.split("\\s+")
    } yield l.toInt -> r.toInt
  val (left, right) = pairs.unzip
  val answerA = left.sorted.zip(right.sorted).foldLeft(0) {
    case (sum, (l, r)) => sum + Math.abs(l - r)
  }

  val sim = for ((n, ns) <- right.groupBy(identity)) yield n -> n * ns.length
  val answerB = left.foldLeft(0)(_ + sim.getOrElse(_, 0))

  answerA -> answerB
}
solve(
  """3   4
    |4   3
    |2   5
    |1   3
    |3   9
    |3   3""".stripMargin)
