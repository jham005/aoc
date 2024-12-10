// https://adventofcode.com/2024/day/10

def solve(input: String): (Int, Int) = {
  val grid = input.split('\n').map(_.getBytes)

  var trails = Set.empty[((Int, Int), (Int, Int))]
  var distinct = 0

  def traverse(start: (Int, Int), x: Int, y: Int): Unit = {
    val height = grid(x)(y)
    if (height == '9') {
      distinct += 1
      trails += start -> (x, y)
    } else
      for {
        (dx, dy) <- Seq(0 -> 1, 0 -> -1, 1 -> 0, -1 -> 0)
        nextRow <- grid.lift(x + dx).toSeq
        nextCell <- nextRow.lift(y + dy)
        if nextCell == height + 1
      } traverse(start, x + dx, y + dy)
  }

    for {
      x0 <- grid.indices
      y0 <- grid(x0).indices
      if grid(x0)(y0) == '0'
    } traverse((x0, y0), x0, y0)

  trails.size -> distinct
}

solve("""89010123
        |78121874
        |87430965
        |96549874
        |45678903
        |32019012
        |01329801
        |10456732""".stripMargin)
