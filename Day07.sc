// https://adventofcode.com/2024/day/7

def solve(input: String): Long = {
  val equations =
    for (line <- input.split('\n')) yield {
      val p = line.indexOf(':')
      val answer = line.substring(0, p).toLong
      val args = line.substring(p + 2).split(' ').map(_.toLong).toList
      answer -> args
    }

  def possibles(args: Seq[Long]): Seq[Long] =
    args match {
      case a :: b :: tail =>
        possibles((a + b) :: tail) ++
          possibles((a * b) :: tail) ++
          possibles(s"$a$b".toLong :: tail)
      case x => x
    }

  var sum = 0L
  for ((answer, args) <- equations)
    if (possibles(args).contains(answer))
      sum += answer
  sum
}

solve("""190: 10 19
        |3267: 81 40 27
        |83: 17 5
        |156: 15 6
        |7290: 6 8 6 15
        |161011: 16 10 13
        |192: 17 8 14
        |21037: 9 7 18 13
        |292: 11 6 16 20""".stripMargin)
