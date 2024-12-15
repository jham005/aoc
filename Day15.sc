// https://adventofcode.com/2024/day/15

def motion(m: Char): Option[(Int, Int)] =
  m match {
    case '>' => Some(0 -> 1)
    case '<' => Some(0 -> -1)
    case '^' => Some(-1 -> 0)
    case 'v' => Some(1 -> 0)
    case _   => None
  }

def move(pos: (Int, Int), dir: (Int, Int)): (Int, Int) =
  (pos._1 + dir._1) -> (pos._2 + dir._2)

def findRobot(warehouse: Array[Array[Byte]]): (Int, Int) = {
  var robot = 0 -> 0
  for (row <- warehouse.indices; col <- warehouse(row).indices)
    if (warehouse(row)(col) == '@')
      robot = row -> col
  robot
}

def score(warehouse: Array[Array[Byte]]): Int = {
  var gps = 0
  for {
    row <- warehouse.indices
    col <- warehouse(row).indices
    h = warehouse(row)(col)
    if h == '[' || h == 'O'
  } gps += row * 100 + col
  gps
}

def solve1(input: String, movements: String): Int = {
  val warehouse = input.split('\n').map(_.getBytes)

  def at(p: (Int, Int)): Byte =
    (for (row <- warehouse.lift(p._1); col <- row.lift(p._2))
      yield col).getOrElse('#')

  def shove(p: (Int, Int), dir: (Int, Int)): (Int, Int) = {
    val p1 = move(p, dir)
    if (at(p1) == 'O')
      shove(p1, dir)
    if (at(p1) == '.') {
      warehouse(p1._1)(p1._2) = at(p)
      warehouse(p._1)(p._2) = '.'
      p1
    } else
      p
  }

  var robot = findRobot(warehouse)
  for (m <- movements; dir <- motion(m))
    robot = shove(robot, dir)

  score(warehouse)
}

def solve2(input: String, movements: String): Int = {
  val expand = Map('#' -> "##", '.' -> "..", 'O' -> "[]", '@' -> "@.")
  val warehouse = input.split('\n').map(_.flatMap(expand(_)).getBytes)
  var robot = findRobot(warehouse)

  def at(p: (Int, Int)): Byte =
    (for (row <- warehouse.lift(p._1); col <- row.lift(p._2))
      yield col).getOrElse('#')

  def containsBox(p: (Int, Int)): Boolean = {
    val box = at(p)
    box == '[' || box == ']'
  }

  def canMove(p: (Int, Int), dy: Int): Boolean =
    at(p) match {
      case '.' => true
      case '[' => canMove(move(p, dy -> 0), dy) && canMove(move(p, dy -> 1), dy)
      case ']' => canMove(move(p, dy -> 0), dy) && canMove(move(p, dy -> -1), dy)
      case _ => false
    }

  def shove(p: (Int, Int), dir: (Int, Int)): (Int, Int) = {
    val p1 = move(p, dir)
    if (containsBox(p1)) {
      val (dy, _) = dir
      if (dy == 0) {
        shove(move(p1, dir), dir)
        shove(p1, dir)
      } else {
        val p2 = move(p1, 0 -> (if (at(p1) == '[') 1 else -1))
        if (canMove(p1, dy) && canMove(p2, dy)) {
          shove(p1, dir)
          shove(p2, dir)
        }
      }
    }

    if (at(p1) == '.') {
      warehouse(p1._1)(p1._2) = at(p)
      warehouse(p._1)(p._2) = '.'
      p1
    } else
      p
  }

  for (m <- movements; dir <- motion(m))
    robot = shove(robot, dir)

  score(warehouse)
}

def solve(input: String, movements: String): (Int, Int) =
  solve1(input, movements) -> solve2(input, movements)

solve(
  """##########
        |#..O..O.O#
        |#......O.#
        |#.OO..O.O#
        |#..O@..O.#
        |#O#..O...#
        |#O..O..O.#
        |#.OO.O.OO#
        |#....O...#
        |##########""".stripMargin,
  """<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
    |vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
    |><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
    |<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
    |^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
    |^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
    |>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
    |<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
    |^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
    |v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^""".stripMargin
)
