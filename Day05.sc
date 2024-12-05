// https://adventofcode.com/2024/day/5

def solve(input: String): (Int, Int) = {
  val lines = input.split('\n')
  val orderings =
    for (row <- lines; p = row.indexOf('|'); if p != -1)
      yield row.substring(0, p) -> row.substring(p + 1)
  val updates =
    for (row <- lines; if row.contains(','))
      yield row.split(',')
  val indexed =
    for ((a, bs) <- orderings.groupBy(_._1))
      yield a -> bs.map(_._2).toSet

  def correctOrder(row: Array[String]): Boolean =
    row.zip(row.tail).forall(orderings.contains)

  def reorder(row: Set[String]): Seq[String] =
    if (row.isEmpty)
      Nil
    else {
      val i =
        row.find(!indexed.getOrElse(_, Set.empty).exists(row.contains)).get
      i +: reorder(row - i)
    }

  var sumA = 0
  for {
    row <- updates
    if correctOrder(row)
  } sumA += row(row.length / 2).toInt

  var sumB = 0
  for {
    row <- updates
    if !correctOrder(row)
    fixedRow = reorder(row.toSet)
    // Yeah, I do know fixedRow is reversed. Middle is middle.
  } sumB += fixedRow(fixedRow.length / 2).toInt

  sumA -> sumB
}

solve("""47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47""")
