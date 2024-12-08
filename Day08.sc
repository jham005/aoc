// https://adventofcode.com/2024/day/8

def solve(input: String): Int = {
  val grid = input.split('\n').map(_.getBytes.map(_.toChar))
  val antenna =
    for {
    (row, x) <- grid.zipWithIndex
    (cell, y) <- row.zipWithIndex
    if cell != '.'
  } yield (cell, x -> y)

  def markAntiNodes(antenna: (Int, Int), locations: Seq[(Int, Int)]): Unit =
    for {
      (lx, ly) <- locations
      (x, y) = antenna
      harmonic <- 0 to grid.length
      dx = (lx - x) * harmonic
      dy = (ly - y) * harmonic
      (ax, ay) <- Seq((x - dx) -> (y - dy), (lx + dx) -> (ly + dy))
      if grid.isDefinedAt(ax) && grid(ax).isDefinedAt(ay)
    } grid(ax)(ay) = '#'

  for {
    (_, locations) <- antenna.groupBy(_._1)
    tails <- locations.map(_._2).tails.toList
    if tails.nonEmpty
  } markAntiNodes(tails.head, tails.tail)

  grid.foldLeft(0)(_ + _.count(_ == '#'))
}

solve("""............
        |........0...
        |.....0......
        |.......0....
        |....0.......
        |......A.....
        |............
        |............
        |........A...
        |.........A..
        |............
        |............""".stripMargin)
