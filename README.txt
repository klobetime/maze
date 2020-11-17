This project is a simple maze solver.  It uses Java (v1.8.0) and Maven (v3.6.3) to compile and execute.

The main source file is src/main/java/Maze.java, and an example of how to use it is src/main/java/Example.java.  The tests for Maze are in src/test/java/MazeTest.java.

To compile and run tests:
> mvn package

To run the example (after compiling):
> java -jar target/maze-1.0-SNAPSHOT.jar

The example uses ANSI character sequences to print the maze in color by default; if color is not wanted (or not supported by the terminal) straight ASCII can be used by passing a parameter to the example on the command line:
> java -jar target/maze-1.0-SNAPSHOT.jar --nocolor

The example uses a default maze, but one can be loaded from a file as well:
> java -jar target/maze-1.0-SNAPSHOT.jar --file maze.txt
