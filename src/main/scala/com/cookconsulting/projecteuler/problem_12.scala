/*
 * Copyright (c) 2011, Todd Cook.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the <ORGANIZATION> nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cookconsulting.projecteuler

/**
 * Problem 12
 * The sequence of triangle numbers is generated by adding the natural numbers.
 * So the 7^(th) triangle number would be 1 + 2 + 3 + 4 + 5 + 6 + 7 = 28.
 * The first ten terms would be:
 *      1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
 *
 * Let us list the factors of the first seven triangle numbers:
 *
 *   1: 1
 *   3: 1,3
 *   6: 1,2,3,6
 *  10: 1,2,5,10
 *  15: 1,3,5,15
 *  21: 1,3,7,21
 *  28: 1,2,4,7,14,28
 *
 *  We can see that 28 is the first triangle number to have over five divisors.
 *  What is the value of the first triangle number to have over five hundred divisors?
 *
 * Commentary after the program
 * @author : Todd Cook
 * @since : 5/7/2011
 */

import scala.collection.mutable.ListBuffer

object problem_12 {

  /**
   * http://en.wikipedia.org/wiki/Triangle_numbers
   */
  def makeTriangleNumber (seed: Long): Long = (seed * (seed + 1)) / 2L

  def findFactors (num: Long): List[Long] = {
    var inc = 1L
    var buf = new ListBuffer[Long]()
    while (inc < num) {
      if (num % inc == 0) {
        buf.append(inc)
      }
      inc += 1
    }
    buf.toList
  }

  def bruteForce () = {
    var max = 1
    (2 to 100000).foreach(x => {
      var result = countFactors(makeTriangleNumber(x))
      if (result > max) {
        println(x + " factors: " + result)
        max = result
      }
    })
    max
  }

  /**
   * Squares in base 16 end in 0, 1, 4, or 9
   * see: http://www.johndcook.com/blog/2008/11/17/fast-way-to-test-whether-a-number-is-a-square/
   */
  def isPerfectSquare (n: Long): Boolean = {
    val hex = java.lang.Long.toHexString(n)
    val lastDigit = hex(hex.length - 1)
    if ((lastDigit == '0') || (lastDigit == '1') || (lastDigit == '4') || (lastDigit == '9')) {
      return true;
    }
    val bsr = new BigSquareRoot()
    val result = bsr.get(new java.math.BigInteger(n.toString))
    if (bsr.error.compareTo(bsr.ZERO) == 0) {
      true
    }
    else {
      false
    }
  }

  /**
   *
   * An integer x is triangular exactly if 8x + 1 is a square
   */
  def isTriangleNumber (n: Long): Boolean = {
    isPerfectSquare((8 * n) + 1)
  }

  def countFactors (n: Long): Int = {
    var count = 1
    var ii = 1L
    while (ii <= n / 2) {
      if (n % ii == 0) {
        count += 1
      }
      ii += 1
    }
    count
  }

  def answer (seed: Int) {
    val MAX = 500
    var currentMax = 1L
    var inc = seed * 1L
    var result = 0
    while (currentMax < MAX) {
      var triangleNum = makeTriangleNumber(inc)
      if (triangleNum % 2 == 0) {
        // optimization from brute force results; see below
        result = countFactors(triangleNum)
        if (result > currentMax) {
          println(inc + " factors: " + result)
          currentMax = result
        }
      }
      inc += 1
    }
    (inc, result)
  }

  def main (args: Array[String]) = {
    // println( answer(1)) // uncomment this to see the long, slow progression
    println(answer(12000))
  }
}

/*
Commentary:

This object class contains two methods: isPerfectSquare(), isTriangleNumber(), one train of thought
was that it might be possible to jump a high enough number, find the nearest number that is
triangular and then zero in on where it exactly crosses over the desired threshold of 500

The problem is that triangle numbers don't increase in factors in predictably even linear manner,
as one can see by running a scala console example e.g.

scala> (2 to 1000).foreach( x => println (x + " factors: " + countFactors(makeTriangleNumber(x) )))
2 factors: 1
3 factors: 3
4 factors: 3
5 factors: 3
6 factors: 3
7 factors: 5
8 factors: 8
9 factors: 5
10 factors: 3
11 factors: 7
12 factors: 7
13 factors: 3
14 factors: 7
15 factors: 15
16 factors: 7
17 factors: 5
18 factors: 5
19 factors: 7
20 factors: 15
21 factors: 7
22 factors: 3
23 factors: 11
24 factors: 17
25 factors: 5
26 factors: 7
27 factors: 15
28 factors: 7
29 factors: 7
30 factors: 7
31 factors: 9
32 factors: 19
33 factors: 7
34 factors: 7
35 factors: 23
36 factors: 11
37 factors: 3
38 factors: 7
39 factors: 23
40 factors: 11
41 factors: 7
42 factors: 7
43 factors: 7
44 factors: 23
45 factors: 11
46 factors: 3
47 factors: 15
48 factors: 23
49 factors: 8
50 factors: 11
51 factors: 15
52 factors: 7
53 factors: 7
54 factors: 15
55 factors: 23
56 factors: 23
57 factors: 7
58 factors: 3
59 factors: 15
60 factors: 15
61 factors: 3
62 factors: 11
63 factors: 35
64 factors: 23
65 factors: 15
66 factors: 7
67 factors: 7
68 factors: 15
69 factors: 15
70 factors: 7
71 factors: 17
72 factors: 17
73 factors: 3
74 factors: 11
75 factors: 23
76 factors: 15
77 factors: 15
78 factors: 7
79 factors: 15
80 factors: 39
81 factors: 9
82 factors: 3
83 factors: 15
84 factors: 31
85 factors: 7
86 factors: 7
87 factors: 23
88 factors: 11
89 factors: 11
90 factors: 23
91 factors: 15
92 factors: 15
93 factors: 7
94 factors: 7
95 factors: 39
96 factors: 19
97 factors: 5
98 factors: 17
99 factors: 35
100 factors: 11
etc...


This forces a solution that isn't appetizing, it's a brute force solution with a couple of
optimizations applied post run.
Often speed can be found only by cutting some corners that may exclude results; e.g.
2 is the only common divisor among the triangular numbers that break new ground in the number of
factors. 3 and 4 could be used to filter the candidates, but some loss is introduced.


Brute force increments produces these solutions

3 factors: 3
7 factors: 5
8 factors: 8
15 factors: 15
24 factors: 17
32 factors: 19
35 factors: 23
63 factors: 35
80 factors: 39
104 factors: 47
224 factors: 89
384 factors: 111
560 factors: 127
935 factors: 143
1224 factors: 161
1664 factors: 167
1728 factors: 191
2015 factors: 239
2079 factors: 319
5984 factors: 479
12375 factors: 575

// by using these as seeds, we find that 2 is the only common divisor

val keys = List( 3 ,7 ,8 ,15,24,32,35,63,80,104,224,384,560,935,1224,1664,1728,2015,2079,5984,12375)

def testList (keys: List[Int], divisor: Int) :Int ={
var inc =0
keys.foreach ( x =>{ var y = makeTriangleNumber( x * 1L); if (y % divisor ==0) inc += 1 })
println (inc)
inc
}

testList (keys, 2)
testList (keys, 3)
testList (keys, 4)
testList (keys, 5)
testList (keys, 6)
testList (keys, 7)
testList (keys, 8)
testList (keys, 9)
testList (keys, 10)

yields:

scala> testList (keys, 2)
21
res13: Int = 21

scala> testList (keys, 3)
20
res14: Int = 20

scala> testList (keys, 4)
19
res15: Int = 19

scala> testList (keys, 5)
15
res16: Int = 15

scala> testList (keys, 6)
20
res17: Int = 20

scala> testList (keys, 7)
13
res18: Int = 13

scala> testList (keys, 8)
12
res19: Int = 12

scala> testList (keys, 9)
13
res20: Int = 13

scala> testList (keys, 10)
15
res21: Int = 15


So the final example yields:

12000 factors: 160
12024 factors: 216
12095 factors: 384
12375 factors: 576

*/