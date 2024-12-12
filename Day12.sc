// https://adventofcode.com/2024/day/12

import scala.collection.mutable
import scala.language.implicitConversions

implicit def boolToInt(b: Boolean): Int =
  if (b) 1 else 0

case class Point(x: Int, y: Int) {
  def up: Point = Point(x, y - 1)
  def down: Point = Point(x, y + 1)
  def left: Point = Point(x - 1, y)
  def right: Point = Point(x + 1, y)
  def leftUp: Point = Point(x - 1, y - 1)
  def rightUp: Point = Point(x + 1, y - 1)
  def leftDown: Point = Point(x - 1, y + 1)
  def rightDown: Point = Point(x + 1, y + 1)

  def neighbours: Iterator[Point] =
    for ((dx, dy) <- Iterator(0 -> 1, 0 -> -1, 1 -> 0, -1 -> 0))
      yield Point(x + dx, y + dy)

  def isAdjacent(other: Point): Boolean =
    x == other.x && Math.abs(y - other.y) == 1 ||
      y == other.y && Math.abs(x - other.x) == 1

  def commonWalls(others: Set[Point]): Int =
    others.count(isAdjacent)

  def nCorner(inRegion: Point => Boolean): Int = {
    val ul = !inRegion(up) && !inRegion(left) && !inRegion(leftUp)
    val dr = !inRegion(down) && !inRegion(right) && !inRegion(rightDown)
    val dl = !inRegion(down) && !inRegion(left) && !inRegion(leftDown)
    val ur = !inRegion(up) && !inRegion(right) && !inRegion(rightUp)
    ul + dr + dl + ur
  }

  def internalDiagonal(inRegion: Point => Boolean): Option[Point] =
    if (inRegion(rightDown) && !inRegion(right) && !inRegion(down))
      Some(this)
    else if (inRegion(leftDown) && !inRegion(left) && !inRegion(down))
      Some(left)
    else
      None
}

def perimeter(region: Set[Point]): Int = {
  var p = 4 * region.size
  for {
    tail <- region.tails
    if tail.nonEmpty
  } p -= tail.head.commonWalls(tail.tail) * 2
  p
}

def numberOfSides(region: Set[Point]): Int = {
  var nSides = 0
  val seen = mutable.Set[Point]()
  val diagonals = mutable.Set[Point]()
  for (p <- region) {
    diagonals ++= p.internalDiagonal(region)
    nSides += p.nCorner(region)
    for {
      outside <- p.neighbours
      if !region(outside) && seen.add(outside)
    } nSides += outside.nCorner(!region(_))
  }

  nSides + 2 * diagonals.size
}

def solve(input: String): (Int, Int) = {
  val plot = input.split('\n').map(_.getBytes)
  val visited = mutable.Set[Point]()

  def growRegion(plant: Byte, pos: Point, region: mutable.Set[Point]): Unit =
    if (visited.add(pos)) {
      region += pos
      for {
        adj <- pos.neighbours
        row <- plot.lift(adj.x)
        adjacent <- row.lift(adj.y)
        if adjacent == plant
      } growRegion(plant, adj, region)
    }

  val regions = mutable.ArrayBuffer[(Byte, Set[Point])]()
  for {
    x <- plot.indices
    y <- plot(x).indices
    plant = plot(x)(y)
  } {
    val region = mutable.Set[Point]()
    growRegion(plant, Point(x, y), region)
    if (region.nonEmpty)
      regions.append(plant -> region.toSet)
  }

  var cost = 0
  var discounted = 0
  for ((_, region) <- regions) {
    cost += perimeter(region) * region.size
    discounted += numberOfSides(region) * region.size
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
