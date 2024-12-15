// https://adventofcode.com/2024/day/13

def findSum(aX: Int,
            aY: Int,
            bX: Int,
            bY: Int,
            prizeX: Int,
            prizeY: Int): Option[Long] = {
  val maxA = Math.min(prizeX / aX, prizeY / aY)
  val stepSizes =
    Iterator
      .range(0, maxA)
      .map(_.toLong)
      .filter(v => (prizeX - v * aX) % bX == 0 && (prizeY - v * aY) % bY == 0)
  val solutions =
    for {
      nA <- stepSizes
      nB = (prizeX - nA * aX) / bX
    } yield 3 * nA + nB

  if (solutions.nonEmpty)
    Some(solutions.next())
  else
    None
}

def solve(input: String): Long = {
  val buttonRegex = """Button [AB]: X\+(\d+), Y\+(\d+)""".r
  val prizeRegex = """Prize: X=(\d+), Y=(\d+)""".r

  val lines = input.split('\n')
  var spend = 0L
  for (i <- Range(0, lines.length, 4)) {
    val buttonA = buttonRegex.findFirstMatchIn(lines(i)).get
    val buttonB = buttonRegex.findFirstMatchIn(lines(i + 1)).get
    val prize = prizeRegex.findFirstMatchIn(lines(i + 2)).get
    findSum(
      buttonA.group(1).toInt,
      buttonA.group(2).toInt,
      buttonB.group(1).toInt,
      buttonB.group(2).toInt,
      prize.group(1).toInt,
      prize.group(2).toInt).foreach(spend += _)
  }

  spend
}

solve("""Button A: X+94, Y+34
        |Button B: X+22, Y+67
        |Prize: X=8400, Y=5400
        |
        |Button A: X+26, Y+66
        |Button B: X+67, Y+21
        |Prize: X=12748, Y=12176
        |
        |Button A: X+17, Y+86
        |Button B: X+84, Y+37
        |Prize: X=7870, Y=6450
        |
        |Button A: X+69, Y+23
        |Button B: X+27, Y+71
        |Prize: X=18641, Y=10279""".stripMargin)
