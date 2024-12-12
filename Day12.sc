// https://adventofcode.com/2024/day/12

import scala.collection.mutable
import scala.language.implicitConversions

def adjacent(a: (Int, Int), b: (Int, Int)): Boolean =
  (a._1 == b._1 && Math.abs(a._2 - b._2) == 1) ||
    (a._2 == b._2 && Math.abs(a._1 - b._1) == 1)

def commonWalls(a: (Int, Int), others: Seq[(Int, Int)]): Int =
  others.count(adjacent(a, _))

def perimeter(region: Seq[(Int, Int)]): Int = {
  var p = 4 * region.size
  for {
    tail <- region.tails
    if tail.nonEmpty
  } p -= commonWalls(tail.head, tail.tail) * 2
  p
}

implicit def boolToInt(b: Boolean): Int =
  if (b) 1 else 0

def nCorner(p: (Int, Int), region: ((Int, Int)) => Boolean): Int = {
  val (x, y) = p
  val (up, down, left, right) = (y - 1, y + 1, x - 1, x + 1)
  val ul = !region(x -> up) && !region(left -> y) && !region(left -> up)
  val dr = !region(x -> down) && !region(right -> y) && !region(right -> down)
  val dl = !region(x -> down) && !region(left -> y) && !region(left -> down)
  val ur = !region(x -> up) && !region(right -> y) && !region(right -> up)
  ul + dr + dl + ur
}

def internalDiagonal(
    p: (Int, Int),
    region: Set[(Int, Int)]
): Option[(Int, Int)] = {
  val (x, y) = p
  val (down, left, right) = (y + 1, x - 1, x + 1)
  val leading =
    region(right -> down) && !region(right -> y) && !region(x -> down)
  val trailing =
    region(left -> down) && !region(left -> y) && !region(x -> down)
  if (leading)
    Some(p)
  else if (trailing)
    Some(left -> y)
  else
    None
}

def numberOfSides(region: Set[(Int, Int)]): Int = {
  var nSides = 0
  val seen = mutable.Set[(Int, Int)]()
  val diagonals = mutable.Set[(Int, Int)]()
  for (p <- region) {
    diagonals ++= internalDiagonal(p, region)
    nSides += nCorner(p, region)
    for {
      (dx, dy) <- Seq(0 -> 1, 0 -> -1, 1 -> 0, -1 -> 0)
      outside = (p._1 + dx) -> (p._2 + dy)
      if seen.add(outside)
      if !region(outside)
    } nSides += nCorner(outside, !region(_))
  }

  nSides + 2 * diagonals.size
}

def solve(input: String): (Int, Int) = {
  val plot = input.split('\n').map(_.getBytes)
  val visited = mutable.Set[(Int, Int)]()

  def growRegion(
      plant: Byte,
      pos: (Int, Int),
      region: mutable.ArrayBuffer[(Int, Int)]
  ): Unit =
    if (visited.add(pos)) {
      region.append(pos)
      for {
        (dx, dy) <- Seq(0 -> 1, 0 -> -1, 1 -> 0, -1 -> 0)
        adj = (pos._1 + dx) -> (pos._2 + dy)
        row <- plot.lift(adj._1)
        adjacent <- row.lift(adj._2)
        if adjacent == plant
      } growRegion(plant, adj, region)
    }

  val regions = mutable.ArrayBuffer[(Byte, Seq[(Int, Int)])]()
  for {
    x <- plot.indices
    y <- plot(x).indices
    plant = plot(x)(y)
  } {
    val region = mutable.ArrayBuffer[(Int, Int)]()
    growRegion(plant, x -> y, region)
    if (region.nonEmpty)
      regions.append(plant -> region)
  }

  var cost = 0
  var discounted = 0
  for ((_, region) <- regions) {
    cost += perimeter(region) * region.size
    discounted += numberOfSides(region.toSet) * region.size
  }

  cost -> discounted
}

solve("""RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""")
