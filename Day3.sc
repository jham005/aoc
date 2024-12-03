def solve(input: String): Int = {
  val mulRegex = """(mul\((\d{1,3}),(\d{1,3})\))|(do\(\))|(don't\(\))""".r
  var enable = true
  var sum = 0
  for (m <- mulRegex.findAllMatchIn(input))
    if (m.group(5) != null)
      enable = false
    else if (m.group(4) != null)
      enable = true
    else if (enable)
        sum += m.group(2).toInt * m.group(3).toInt
  sum
}

solve("""xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))""")
