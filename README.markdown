
# Usage

Kingpin is run from the command line with player names and order of play
specified as arguments:

    $ java -jar kingpin-assembly-1.0.jar Roy Ernie Ishmael

# Output

             |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |   10  | Total
    ---------+-----+-----+-----+-----+-----+-----+-----+-----+-----+-------+------
         Roy | 3 6 | X   | 7 2 |     |     |     |     |     |     |       |
             |   9 |  28 |  37 |     |     |     |     |     |     |       |    37
    ---------+-----+-----+-----+-----+-----+-----+-----+-----+-----+-------+------
       Ernie | X   | 4 / | 5   |     |     |     |     |     |     |       |
             |  20 |  35 |     |     |     |     |     |     |     |       |    35
    ---------+-----+-----+-----+-----+-----+-----+-----+-----+-----+-------+------
     Ishmael | 9 / | X   |     |     |     |     |     |     |     |       |
             |  20 |     |     |     |     |     |     |     |     |       |    20

    Ernie's turn (0 - 5 or '/' for all remaining pins):

# Design

* Game logic is decoupled from the UI to facilitate reuse
* State machine modelled as case classes
* Scoring by recursive pattern matching
* Next game state and remaining pins computed via polymorphic dispatch

