# Held-Karp

A Java implementation of the Held Karp algorithm.

<h2><b>Algorithm:</b></h2>
<p>Held Karp is a dynamic programming algorithm used to effciently solve the Travelling Salesman Problem.
<br>By applying the divide-and-conquer principle, Held Karp calculates the path cost of subsets of increasing length.</p>
<p>For a more detailed explanation of the algorithm check the
<a href="https://en.wikipedia.org/wiki/Held%E2%80%93Karp_algorithm">Wikipedia Page</a>.
<br>The core algorithm was based on the following <a href="https://github.com/CarlEkerot/held-karp">Python port</a>.</p>
<p> As of now, the program does not implement any kind of heuristics and will always return the optimal path.</p>
  

<h2><b>How to use:</b></h2>
<p>This project comes with a executable jar file, which you can run in a command line/prompt by typing:
<br><code>java -jar HeldKarp.jar</code></p>
<p><b>Notes:</b>
<br>1. The program can be quite memory hungry for computations with over 20 cities,
  <br>remember to allocate more memory to the jar execution if needed.
<br>2. If you have a 64 bits JRE, you can run the program with the flag <code>-d64</code> to boost performance in 64 bit machines.</p>
<p><b>Example command:</b>
<code>java -d64 -Xmx4096m -Xms4096m -jar HeldKarp.jar</code></p>
 
<h2><b>Benchmarks:</b></h2>
<p><b>All test were done on a Intel Core i7 6700 @3.4Ghz - 16GB DDR4 RAM - Windows 10 (64 bits).</b></p>
<p>23 cities: 28.428 seconds<br>
  22 cities: 11.909 seconds<br>
  21 cities: 5.451 seconds<br>
  20 cities: 2.371 seconds<br>
  19 cities: 1.029 seconds<br>
  18 cities: 309.666 milliseconds<br>
  17 cities: 106.951 milliseconds<br>
  16 cities: 62.438 milliseconds<br>
  15 cities: 27.893 milliseconds<br>
  14 cities: 12.631 milliseconds<br>
  13 cities: 3.994 milliseconds<br>
  12 cities: 1.379 milliseconds<br>
  11 cities: 529.427 microseconds<br>
  10 cities: 238.574 microseconds<br></p>

<h2><b>To be Implemented:</b></h2>
<p>1. Read input matrix from file.
<br>2. Memory overflow detection.</p>
  

