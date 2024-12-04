# Advent of Code 2024

This project contains the code I wrote for the 2024 AoC.
You can find the puzzle questions at <https://adventofcode.com/2024>.

## Day 4 (24:04 + 16:37 = 40.41, rank 5038)

Brain not firing well this morning. I first read the puzzle as allowing any 
path from the starting 'X', so any successive step could be up, down, left or 
right. The concern then was to stop the path from wrapping back on itself. 
Got that working, but then realised it was a much simpler ask - choose a 
direction and follow it along. Bah!

Second path involved a lot of fussing around with the `x` and `y` offsets, 
until I realised I should just make a `Set` and check the required 'M' and 
'S' both appeared.

## Day 3 (5:37 + 7:02 = 12:39, rank 1707)

This puzzle has regex written all over it. Knowing the `{x,y}` pattern for 
limiting the number of repeated matches helped. I made the mistake of 
forgetting to check the `enabled` flag before doing the multiplication, and 
had to wait a minute (!) before it would let me enter the answer again.

## Day 2 (12:09 + 8:25 = 20:34, rank 2920)

I used `.zip(row.tail)` to do most of the heavy lifting in the puzzle. It 
nicely handles the edge cases of empty or singleton list. Two the `.forall` 
tests cover ascending and descending separately.

For the second part I generate all rows with a single element dropped, and 
test them using the `isSafe` method from Part 1. Scala doesn't appear to 
have a function for removing an element by index, so I had to resort to `.
take(i).drop(i + 1)`. This seems really inefficient, as it looks like 
it will create a bunch temporary `Array`s. If performance was an issue then 
I could have passed an "ignore this index" argument to `isSafe`. 

I also worked out a way of organising the Scala worksheets to have the input 
data appear after the code logic, so you don't have to scroll right to the 
bottom of the file to see what's going on.

## Day 1 (36:12 + 8:01 = 44:13, rank 8133)

Main challenge here was setting up. For _reasons unknown_ I hadn't bothered
updating my Intellij version before starting, so lost some time fluffing
about doing that.

Scala has a bunch of built-in functions that are idea for solving this
puzzle, especially `zip` and `unzip`. The distances are given as pairs, one
per line, and these can be extracted from a string using `.split('\n')`
and then `.split("\\s+")`. That gives us an `Array` of pairs, `.unzip` converts
this to a pair of `Array`s (`left` and `right`). We sort these, and `.zip` the
resulting sorted lists together so we can compute the differences (using a `.
foldLeft`).

The second part benefited from a `.groupBy(identity)`, which we can map into 
the similarity scores.
