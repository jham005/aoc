// https://adventofcode.com/2024/day/9

def toFreeMap(lengths: Array[Int]): Array[Int] = {
  val freeMap = Array.fill(lengths.sum)(-1)
  var used = 0

  for (fileId <- 0 until lengths.length / 2 + 1) {
    val len = lengths(fileId * 2)
    for (i <- 0 until len)
      freeMap(used + i) = fileId
    used += len + lengths.lift(fileId * 2 + 1).getOrElse(0)
  }

  freeMap
}

def calcChecksum(free: Seq[Int]): Long = {
  var checksum = 0L
  for ((fileId, i) <- free.zipWithIndex; if fileId > 0)
    checksum += fileId * i
  checksum
}

def solveA(input: String): Long = {
  val lengths = input.split("").map(_.toInt)
  val freeMap = toFreeMap(lengths)
  var offset = 0
  for (i <- freeMap.indices.reverse) {
    val p = freeMap.indexWhere(_ == -1, offset)
    if (p != -1) {
      freeMap(p) = freeMap(i)
      freeMap(i) = -2
      offset = p
    }
  }

  calcChecksum(freeMap)
}

def solveB(input: String): Long = {
  val lengths = input.split("").map(_.toInt)
  val usedLengths = Array.tabulate((lengths.length + 1) / 2)(i => lengths(2 * i))
  val freeLengths = Array.tabulate(lengths.length / 2)(i => lengths(2 * i + 1))
  val freeMap = toFreeMap(lengths)

  for ((len, fileId) <- usedLengths.zipWithIndex.reverse) {
    val freeIndex = freeLengths.indexWhere(_ >= len)
    val fromIndex = usedLengths.take(fileId).sum + freeLengths.take(fileId).sum
    val toIndex = usedLengths.take(freeIndex + 1).sum + freeLengths.take(freeIndex).sum
    if (freeIndex != -1 && toIndex < fromIndex) {
      for (i <- 0 until len) {
        freeMap(fromIndex + i) = -1
        freeMap(toIndex + i) = fileId
      }

      freeLengths(freeIndex) -= len
      usedLengths(freeIndex) += len
    }
  }

  calcChecksum(freeMap)
}

def solve(input: String): (Long, Long) =
  solveA(input) -> solveB(input)

solve("2333133121414131402")
