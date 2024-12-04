// https://adventofcode.com/2024/day/2

def problemDampener(row: Array[Int]): Seq[Array[Int]] =
  for (i <- row.indices)
    yield row.take(i) ++ row.drop(i + 1)

def isSafe(row: Array[Int]): Boolean = {
  val diffs =
    for ((x, y) <- row.zip(row.tail))
      yield x - y
  diffs.forall(d => d > 0 && d <= 3) ||
  diffs.forall(d => d < 0 && d >= -3)
}

def isTolerablySafe(row: Array[Int]): Boolean =
  isSafe(row) || problemDampener(row).exists(isSafe)

def countSafeLevels(safeTest: Array[Int] => Boolean)(input: String): Int = {
  var count = 0
  for {
    line <- input.split('\n')
    row = line.split(' ').map(_.toInt)
  } if (safeTest(row)) count += 1
  count
}

countSafeLevels(problemDampener(_).exists(isSafe))("""7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9""")
