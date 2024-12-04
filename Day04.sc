def solve(input: String): (Int, Int) = {
  val grid = input.split('\n')

  def get(x: Int, y: Int): Char =
    if (grid.isDefinedAt(x) && grid(x).isDefinedAt(y))
      grid(x)(y)
    else
      '.'

  var xmas = 0
  for {
    x <- grid.indices
    y <- grid(x).indices
    if grid(x)(y) == 'X'
    dx <- -1 to 1
    dy <- -1 to 1
    if dx != 0 || dy != 0 &&
      get(x + dx, y + dy) == 'M' &&
      get(x + 2 * dx, y + 2 * dy) == 'A' &&
      get(x + 3 * dx, y + 3 * dy) == 'S'
  } xmas += 1

  var `x-mas` = 0
  for {
    x <- grid.indices
    y <- grid(x).indices
    if grid(x)(y) == 'A' &&
      Set(get(x - 1, y - 1), get(x + 1, y + 1)) == Set('M', 'S') &&
      Set(get(x + 1, y - 1), get(x - 1, y + 1)) == Set('M', 'S')
  } `x-mas` += 1
  xmas -> `x-mas`
}

solve("""MMMSXXMASM
        |MSAMXMSMSA
        |AMXSXMAAMM
        |MSAMASMSMX
        |XMASAMXAMM
        |XXAMMXXAMA
        |SMSMSASXSS
        |SAXAMASAAA
        |MAMMMXMMMM
        |MXMXAXMASX""".stripMargin)
