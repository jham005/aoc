// https://adventofcode.com/2024/day/6

import scala.annotation.tailrec // (minor personal goal failure :-()

def solve(input: String): (Int, Int) = {
  val motion =
    Map(
      '^' -> (-1, 0, '>'),
      '<' -> (0, -1, '^'),
      'v' -> (1, 0, '<'),
      '>' -> (0, 1, 'v')
    )

  def read(grid: Array[Array[Char]], pos: (Int, Int)): Char =
    grid
      .lift(pos._1)
      .getOrElse(Array.emptyCharArray)
      .lift(pos._2)
      .getOrElse('!')

  @tailrec
  def walk(grid: Array[Array[Char]], guard: (Int, Int), dir: Char): Int = {
    grid(guard._1)(guard._2) = 'X'
    val (dx, dy, turn) = motion(dir)
    val ahead = (guard._1 + dx) -> (guard._2 + dy)
    read(grid, ahead) match {
      case '!' =>
        var steps = 0
        for (x <- grid.indices; y <- grid.indices)
          if (grid(x)(y) == 'X')
            steps += 1
        steps
      case '#' => walk(grid, guard, turn)
      case _   => walk(grid, ahead, dir)
    }
  }

  @tailrec
  def loops(
      grid: Array[Array[Char]],
      guard: (Int, Int),
      dir: Char,
      wasObstructed: Boolean,
      visited: Set[((Int, Int), Char)]
  ): Boolean = {
    grid(guard._1)(guard._2) = ' '
    val (dx, dy, turn) = motion(dir)
    val ahead = (guard._1 + dx) -> (guard._2 + dy)
    read(grid, ahead) match {
      case '!' => false
      case '#' =>
        loops(grid, guard, turn, wasObstructed, visited + (guard -> dir))
      case 'O' =>
        loops(grid, guard, turn, wasObstructed = true, visited + (guard -> dir))
      case _ =>
        if (visited(guard, dir))
          wasObstructed
        else
          loops(
            grid,
            ahead,
            dir,
            wasObstructed,
            visited + (guard -> dir)
          )
    }
  }

  val originalGrid = input.split('\n').map(line => line.getBytes.map(_.toChar))
  val guards =
    for {
      x <- originalGrid.indices
      y <- originalGrid.indices
      if motion.isDefinedAt(originalGrid(x)(y))
    } yield (x -> y) -> originalGrid(x)(y)
  val (guard, dir) = guards.head

  val steps = walk(originalGrid, guard, dir)

  var obstructions = 0
  for {
    x <- originalGrid.indices
    y <- originalGrid.indices
    if originalGrid(x)(y) != '#'
  } {
    val grid = originalGrid.map(_.clone()) // deep clone!
    grid(x)(y) = 'O'
    if (loops(grid, guard, dir, wasObstructed = false, Set.empty))
      obstructions += 1
  }

  steps -> obstructions
}

solve("""....#.....
        |.........#
        |..........
        |..#.......
        |.......#..
        |..........
        |.#..^.....
        |........#.
        |#.........
        |......#...""".stripMargin)
