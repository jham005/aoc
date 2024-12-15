// https://adventofcode.com/2024/day/14

class Puzzle(input: String, width: Int, height: Int) {
  case class Robot(var col: Int, var row: Int, vCol: Int, vRow: Int) {
    def move(times: Int): Unit = {
      val vRowNorm = (vRow * times) % height
      val vColNorm = (vCol * times) % width
      row = (row + vRowNorm + height) % height
      col = (col + vColNorm + width) % width
    }
  }

  val robots: Array[Robot] = {
    val regex = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".r
    for {
      line <- input.split('\n')
      m <- regex.findFirstMatchIn(line)
    } yield Robot(m.group(1).toInt, m.group(2).toInt, m.group(3).toInt, m.group(4).toInt)
  }

  def solve(): Int = {
    for (robot <- robots)
      robot.move(100)

    var (topLeft, topRight, bottomLeft, bottomRight) = (0, 0, 0, 0)
    for (robot <- robots) {
      (height / 2 - robot.row) -> (width / 2 - robot.col) match {
        case (t, l) if t < 0 && l < 0 => topLeft += 1
        case (t, l) if t < 0 && l > 0 => topRight += 1
        case (t, l) if t > 0 && l < 0 => bottomLeft += 1
        case (t, l) if t > 0 && l > 0 => bottomRight += 1
        case _ =>
      }
    }

    topLeft * topRight * bottomLeft * bottomRight
  }
}

def solve(input: String, width: Int, height: Int): Int =
  new Puzzle(input, width, height).solve()

solve("""p=0,4 v=3,-3
        |p=6,3 v=-1,-3
        |p=10,3 v=-1,2
        |p=2,0 v=2,-1
        |p=0,0 v=1,3
        |p=3,0 v=-2,-2
        |p=7,6 v=-1,-3
        |p=3,0 v=-1,-2
        |p=9,3 v=2,3
        |p=7,3 v=-1,2
        |p=2,4 v=2,-3
        |p=9,5 v=-3,-3""".stripMargin, 11, 7)
