// https://adventofcode.com/2024/day/11

import scala.collection.mutable

def blink1(stones: Seq[BigInt]): Seq[BigInt] = {
  val result = mutable.ArrayBuffer[BigInt]()
  for (stone <- stones)
    if (stone == 0)
      result.append(1)
    else {
      val s = stone.toString
      if (s.length % 2 == 0) {
        val mid = s.length / 2
        result.append(BigInt(s.take(mid)), BigInt(s.drop(mid)))
      } else
        result.append(stone * 2024)
    }

  result
}

def solve1(input: String, blinks: Int = 25): Int = {
  var stones = input.split(' ').map(BigInt(_)).toSeq
  for (_ <- 1 to blinks)
    stones = blink1(stones)
  stones.size
}

val cached = mutable.Map[(String, Int), Long]()

def blink2(stone: String, blinks: Int): Long =
  cached.getOrElseUpdate(
    stone -> blinks, {
      if (blinks == 0)
        1L
      else if (stone == "0")
        blink2("1", blinks - 1)
      else if (stone.length % 2 == 0) {
        val mid = stone.length / 2
        val left = BigInt(stone.take(mid)).toString
        val right = BigInt(stone.drop(mid)).toString
        blink2(left, blinks - 1) + blink2(right, blinks - 1)
      } else
        blink2((BigInt(stone) * 2024).toString, blinks - 1)
    })

def solve2(input: String, blinks: Int): Long =
  input.split(' ').map(blink2(_, blinks)).sum

solve1("125 17")
solve2("8435 234 928434 14 0 7 92446 8992692", 75)