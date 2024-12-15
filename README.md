# Advent of Code 2024

This project contains the code I wrote for the 2024 AoC.
You can find the puzzle questions at <https://adventofcode.com/2024>.

## [Day 15](Day15.sc): (55:30 + 4:40:24, rank 7009)

Nice to return to a fairly straightforward coding puzzle today. I didn't
find any need for any complex data structures, just a 2d array for the
warehouse. Each move updates the warehouse in-place, using recursion to
handle chains of connected boxes. The clue to a box being successfully moved
is having its previous position replaced with a `.`. This requires calling
`at(p1)` twice: even though `p1` doesn't change, the warehouse changes
underneath. Got bitten by that once or twice.

Part 2 had some nice edge cases that I need to take a break and think about.
Eventually decided to check if a move is possible before performing the move,
but had to fiddle around with several variation of my `canMove` function 
before settling on one that worked. It's pretty inefficient as it 
re-evaluates locations repeatedly, but I couldn't be bothered adding 
memoization given it was performant enough. Second decision was to treat 
horizontal and vertical movements separately. The two cases could probably 
be merged, but again it didn't seem worth the effort.

## [Day 14](Day14a.sc): Another failure :-(

More congruence theory needed?

## [Day 13](Day13a.sc): Failure :-(

There's surely an elegant, efficient solution for this somewhere. I tinkered
with binary search, but couldn't see how that could ever work with two
variables to adjust. The puzzle requires the minimum cost, so just finding
any solution by search seemed unlikely to cut it. So what, do we need to do
some maths here? Seriously? Well, start at the beginning. A guaranteed
minimum cost is when you can just press button B. That means zero As, which
can only work if $B_x$ divides $X$ and $B_y$ divides $Y$. I think you can
write that as $B_x | X$ and $B_y | Y$, so I remember some maths notation at
least! In general, we will have to press A multiple times, which we can
generate using `Iterator.from(0)`. But the only multiples of A that are of
interest are the ones where B can fill in the difference. So we need $B_x |
X - A \times A_x$ and $B_y | Y - A \times A_y$. That leads is to

```scala
Iterator
  .from(0)
  .filter(A => (X - A * Ax) % Bx == 0 && (Y - A * Ay) % By == 0)
```

This will give a sequence of candidate `A`s (the number of times we press
the button A). We only need those factors that don't overflow the solution,
so we can crop the iterator with `.takeWhile(A => A * Ax < X && A * Ay < Y)`.

I think any solution must involve some multiple of one of these factors. We
can generate possible multiples using `Iterator.from(1)`. Again, we can limit
this iterator to multiples that don't overflow.

Once we have a plausible candidate for `A` we can compute backward to find
what the required `B` must be: $(X - A * A_x) / B_x$. No other `B` will
suffice, so from there we just need to check it all adds up correctly.

Sound ok in theory? I can get this to work for all the examples in Part 1,
but my Scala program hangs for Part 2. I'm guessing there is an integer (or
long?) overflow going undetected. Danged if I can see where. Re-wrote the
program in C++, but now my GNU C++ compiler has broken :-(.

## [Day 12](Day12.sc): (1:21:15 + ages = ages and ages, rank awful)

Work interrupted play today, which is fortunate because I was looking for
someone to blame for my poor showing. There is probably some graph theory I
should know that lets you compute the number of edges trivially, but since I
don't know it (and refuse to google anything) I had to bludgeon my way
through this puzzle. The code looks complicated, and despite giving the
correct answers it's hard for me to have high confidence in it.

Seemed reasonable to compute the perimeter by first treating each plot as
an isolated region, and then subtracting the number of common walls. For the
straight sections of fence, I (eventually) thought of counting the number of
bends. A bend either being a plot with no neighbours in the region, or a
point outside the region that touches three plots inside the region (the
points outside have to abut the region, making candidates easy to find). The
puzzle kindly (I thought) pointed out an exception to this rule, when a
diagonal forms in an internal region. Translating that into code took a
while, and it was all a bit fiddly.

Probably should have adopted a class for storing points, so I could attach
methods to give the neighbours etc.

## [Day 11](Day11.sc): (21:24 + 29:24 = 50:48, rank 4133)

Lovely twist in part 2. When the penny dropped it was delightfully easy to
add a cache to limit the repeated recursive calls.

Couldn't decide whether to make `stone` a `BigInt` or `String` in part 2.
Since the main operation was testing the length, I went with `String`. Did
mean a bunch of conversions into `BigInt` and back. Tried to do this just
with `String`, using `stripPrefix("0")`, but it got fiddly handling zero so
I decided to leave `BigInt` to do the work.

My "no imports" challenge is lost :-(. I don't see any easy way of avoiding
`mutable.Map` (not without losing the elegance of `getOrElseUpdate`).

## [Day 10](Day10.sc): (48:26 + 2:02 = 50:28, rank 6215)

Gotta love recursion, just need to focus on what's being counted. Basically
solved part 2 before part 1. Also need to get directions right - "up, down,
left, right" doesn't mean choose any pair of `dx`, `dy` from `(-1, 0, 1)` :-
(. Ended up with a couple of `var`s to do the counting, which made
`traverse` feel a bit unclean. But I can live with that.

## [Day 9](Day09.sc): (36:53 + 1:14:43) = 1:51:36, rank 5453)

Getting the right data structures was the key here. For the first part I did
it all using a simple free map, containing the `fileId` or `-1` for each
block. This worked, but was pretty slow.

For the second part I decided I needed to separately track the used and free
space lengths, since looking for gaps of the right length was going to be a
pain otherwise. It was a bit fiddly converting between the lengths and the
indexes into the free map (which I needed to keep in order to work out the
checksum; didn't think of that at first!), but our friend `take` came to the
rescue eventually.

Nasty subtlety at the close, where the first version I managed to get
working on the sample input failed on the puzzle input. Turned out I was
allowing compaction to move blocks to the right. Lost a bunch of time
debugging that one, as I had to first construct a new example to identity the
issue.

## [Day 8](Day08.sc): (57:04 + 5:17 = 1:02:21, rank 5904)

`tails` to the rescue on this one. Quite a nice pattern to use to compare
each pair of elements, by taking the `head` and matching it with the `tail`
for of each `tails`.

Would help me to tighten up my geometry a bit. Coming up with the formula to
reflect a point was more a case of permutation coding than applying actual
logic. At least the harmonics in part 2 fell out easily.

## [Day 7](Day07.sc): (28:09 + 1:44 = 29:53, rank 3703)

Nice to have a proper non-tail recursive problem. I lost a bit of time on
parsing the input. If I was smarter I'd have just throw `.split` at it, but
fool me thought I'd use a regex. It looks like a regex along the lines of
`"""(\d+): (?: (\d+))*"""` should do the trick, but Java's regex
implementation doesn't support matching repeated patterns. Maybe I was
thinking of .NET regex, because I was sure this sort of thing was possible.

The logic fell out easily enough once I reverted to using linked lists
instead of `Array`s. Scala doesn't treat `Array` as a first-class `Seq`, so
it fails to pattern match `a :: b :: tail`. I'm usually averse to anything
linked list, given the ridiculous per-element overhead they incur, but they
do fit this puzzle quite well. I didn't bother optimising `possibles` to
avoid the concatenation, since the size of the results was fairly small.

Nice touch including some `Long`s in the puzzle input. Let's just hope they
pop in some `BigInt`s sometime soon!

## [Day 6](Day06.sc): (48:06 + 1:21:49 = 2:09:55, rank 7725)

Plenty of fiddly detail to contend with today. I can see how doing these
events regularly would help. The puzzles have a certain style that takes a
while to get used to.

I started out writing `walk` as a `while`-loop, but it soon became apparent
that recursion would make things easier. It eventually did (but I had to
break my private challenge of avoiding `import`s, although `@tailrec` is
arguably a core feature). In fact, the more functional I made the code the
easier it was to get it right. Passing the guard's position and direction as
arguments (rather than having them as `var`s) meant it was really clear when
the guard moved position vs. just changing direction. I still left the grid
mutable, so I could record the path explicitly. This proved to be a major
obstacle for solving the second part. The second part required trying out
different positions for the obstacle, so I decided cloning the grid would be
the easiest way to do that. But of course Java (and hence Scala) `.clone()`
is shallow - leaving the underlying arrays intact. Once I twigged (hurrah
for `println`) things started to come together.

I also messed up the logic for detecting if the guard had looped. The
easiest method would have been to just keep a count of how many steps he had
taken, but instead I tried to look at the board for footsteps. Sadly, this
ignores the direction! When I adopted the "proper" approach of maintaining a
set of locations and directions, it all came together.

## [Day 5](Day05.sc): (40.57 + 59:33 = 1:40:30, rank 10436)

Interesting puzzle today. I think solving the second part first might
actually have made it easier! Despite appearances, the puzzle doesn't really
involve numbers; e.g. there are no `>` or `<` comparisons required anywhere.
All the ordering comparisons are defined by the input pairs.

I re-wrote the `reorder` function after I'd solved the puzzle to use a `Set`
rather than a sequence and leave the inputs as `String`s, and the code came
out much cleaner. I also made it generate the result in a "natural" (i.e.
reversed) order. Since we only care about the middle element, there is no
need to reverse it back.

At one point I attempted a solution involving `.permutations`, kind of
brute-force on steroids. It worked fine for the sample input, but far too
slow for the actual puzzle data.

I'm kinda pleased that, so far, I've used Scala out-of-the-box. No import in
sight. Interesting to see how long I can keep this up.

## [Day 4](Day04.sc) (24:04 + 16:37 = 40.41, rank 5038)

Brain not firing well this morning. I first read the puzzle as allowing any
path from the starting 'X', so any successive step could be up, down, left or
right. The concern then was to stop the path from wrapping back on itself.
Got that working, but then realised it was a much simpler ask - choose a
direction and follow it along. Bah!

Second path involved a lot of fussing around with the `x` and `y` offsets,
until I realised I should just make a `Set` and check the required 'M' and
'S' both appeared.

## [Day 3](Day03.sc) (5:37 + 7:02 = 12:39, rank 1707)

This puzzle has regex written all over it. Knowing the `{x,y}` pattern for
limiting the number of repeated matches helped. I made the mistake of
forgetting to check the `enabled` flag before doing the multiplication, and
had to wait a minute (!) before it would let me enter the answer again.

## [Day 2](Day02.sc) (12:09 + 8:25 = 20:34, rank 2920)

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

## [Day 1](Day01.sc) (36:12 + 8:01 = 44:13, rank 8133)

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
