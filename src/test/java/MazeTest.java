import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

public class MazeTest {
    @Test
    public void testConstructorWithTrailingCR() {
        Maze m = new Maze("SF\n");
        assertThat(m.getRows(), is(3));
        assertThat(m.getCols(), is(4));
        assertThat(m.toString(), is(new StringBuilder()
                .append("****\n")
                .append("*SF*\n")
                .append("****\n")
                .toString()));
    }

    @Test
    public void testConstructorWithoutTrailingCR() {
        Maze m = new Maze("FS");
        assertThat(m.getRows(), is(3));
        assertThat(m.getCols(), is(4));
        assertThat(m.toString(), is(new StringBuilder()
                .append("****\n")
                .append("*FS*\n")
                .append("****\n")
                .toString()));
    }

    @Test
    public void testConstructorIrregularRows() {
        String grid = new StringBuilder()
                .append(" S\n")
                .append(" **\n")
                .append("    \n")
                .append(" ****\n")
                .append("     F\n")
                .toString();
        Maze m = new Maze(grid);
        assertThat(m.getRows(), is(7));
        assertThat(m.getCols(), is(8));
        assertThat(m.toString(), is(new StringBuilder()
                .append("********\n")
                .append("* S*****\n")
                .append("* ******\n")
                .append("*    ***\n")
                .append("* ******\n")
                .append("*     F*\n")
                .append("********\n")
                .toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMissingStart() {
        new Maze("F");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMultipleStarts() {
        new Maze("SFS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMissingFinish() {
        new Maze("S");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMultipleFinishes() {
        new Maze("FSF");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMissingMaze() {
        new Maze("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidChar() {
        new Maze("S:F");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull() {
        new Maze(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawMazeMismatch() {
        Maze maze1 = new Maze("S \n F\n");
        List<Maze.Square> path1 = maze1.solve().iterator().next();
        Maze maze2 = new Maze("F \n S\n");
        maze2.drawMaze(path1);
    }

    @Test
    public void testDrawMazeSimple() {
        Maze m = new Maze("SF\n");
        assertThat(m.drawMaze(), is(new StringBuilder()
                .append("****\n")
                .append("*SF*\n")
                .append("****\n")
                .toString()));

        List<Maze.Square> path = m.solve().iterator().next();
        assertThat(m.drawMaze(path), is(new StringBuilder()
                .append("****\n")
                .append("*SF*\n")
                .append("****\n")
                .toString()));
    }

    @Test
    public void testDrawMazeCaseInsensitive() {
        Maze m = new Maze("sf\n");
        assertThat(m.drawMaze(), is(new StringBuilder()
                .append("****\n")
                .append("*SF*\n")
                .append("****\n")
                .toString()));
    }

    @Test
    public void testDrawMazeComplicated() {
        Maze m = new Maze(new StringBuilder()
                .append(" S*   \n")
                .append(" ** * \n")
                .append("    * \n")
                .append(" ****\n")
                .append("     F\n")
                .toString());

        assertThat(m.drawMaze(), is(new StringBuilder()
                .append("********\n")
                .append("* S*   *\n")
                .append("* ** * *\n")
                .append("*    * *\n")
                .append("* ******\n")
                .append("*     F*\n")
                .append("********\n")
                .toString()));

        List<Maze.Square> path = m.solve().iterator().next();
        assertThat(m.drawMaze(path), is(new StringBuilder()
                .append("********\n")
                .append("*.S*   *\n")
                .append("*.** * *\n")
                .append("*.   * *\n")
                .append("*.******\n")
                .append("*.....F*\n")
                .append("********\n")
                .toString()));
    }

    @Test
    public void testDrawMazeAlternateWalls() {
        Maze m = new Maze(new StringBuilder()
                .append(" S|   \n")
                .append(" -+ + \n")
                .append("    | \n")
                .append(" +--+\n")
                .append("     F\n")
                .toString());

        assertThat(m.drawMaze(), is(new StringBuilder()
                .append("********\n")
                .append("* S*   *\n")
                .append("* ** * *\n")
                .append("*    * *\n")
                .append("* ******\n")
                .append("*     F*\n")
                .append("********\n")
                .toString()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolutionSetUnmodifiable() {
        Maze m = new Maze("S \n F\n");
        Set<List<Maze.Square>> solutions = m.solve();
        solutions.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolutionContentsUnmodifiable() {
        Maze m = new Maze("S \n F\n");
        Set<List<Maze.Square>> solutions = m.solve();
        solutions.iterator().next().clear();
    }

    @Test
    public void testSolutionCached() {
        Maze m = new Maze("S \n F\n");
        Set<List<Maze.Square>> solutions1 = m.solve();
        Set<List<Maze.Square>> solutions2 = m.solve();

        assertThat(solutions2, is(theInstance(solutions1)));
    }

    @Test
    public void testImpossibleSolve() {
        Maze m = new Maze("S*F\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(0));
    }

    @Test
    public void testSimpleForwardVerticalSolve() {
        Maze m = new Maze("S\n \nF\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(1));
        assertThat(Maze.getPathString(solutions.iterator().next()), is("START -> [1, 1] -> [2, 1] -> [3, 1] -> FINISH"));
    }

    @Test
    public void testSimpleReverseVerticalSolve() {
        Maze m = new Maze("F\n \nS\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(1));
        assertThat(Maze.getPathString(solutions.iterator().next()), is("START -> [3, 1] -> [2, 1] -> [1, 1] -> FINISH"));
    }

    @Test
    public void testSimpleForwardHorizontalSolve() {
        Maze m = new Maze("S F\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(1));
        assertThat(Maze.getPathString(solutions.iterator().next()), is("START -> [1, 1] -> [1, 2] -> [1, 3] -> FINISH"));
    }

    @Test
    public void testSimpleReverseHorizontalSolve() {
        Maze m = new Maze("F S\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(1));
        assertThat(Maze.getPathString(solutions.iterator().next()), is("START -> [1, 3] -> [1, 2] -> [1, 1] -> FINISH"));
    }

    @Test
    public void testSimpleForwardSquareSolve() {
        Maze m = new Maze("S \n F\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(2));
        Set<String> expected = new HashSet<String>(Arrays.asList(
                "START -> [1, 1] -> [2, 1] -> [2, 2] -> FINISH",
                "START -> [1, 1] -> [1, 2] -> [2, 2] -> FINISH"));
        assertThat(transformSolutionsToString(solutions), is(expected));
    }

    @Test
    public void testSimpleReverseSquareSolve() {
        Maze m = new Maze("F \n S\n");
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(2));
        Set<String> expected = new HashSet<String>(Arrays.asList(
                "START -> [2, 2] -> [2, 1] -> [1, 1] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 1] -> FINISH"));
        assertThat(transformSolutionsToString(solutions), is(expected));
    }

    @Test
    public void testMazeWithDeadEnd() {
        String grid = new StringBuilder()
                .append(" S\n")
                .append(" **\n")
                .append("    \n")
                .append(" ****\n")
                .append("     F\n")
                .toString();
        Maze m = new Maze(grid);
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(1));
        assertThat(Maze.getPathString(solutions.iterator().next()), is("START -> [1, 2] -> [1, 1] -> [2, 1] -> [3, 1] -> [4, 1] -> [5, 1] -> [5, 2] -> [5, 3] -> [5, 4] -> [5, 5] -> [5, 6] -> FINISH"));
    }

    @Test
    public void testMazeWithNoWalls() {
        String grid = new StringBuilder()
                .append("    \n")
                .append(" SF \n")
                .append("    \n")
                .toString();
        Maze m = new Maze(grid);
        Set<List<Maze.Square>> solutions = m.solve();
        assertThat(solutions.size(), is(19));
        Set<String> expected = new HashSet<String>(Arrays.asList(
                "START -> [2, 2] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [3, 2] -> [3, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 3] -> [1, 4] -> [2, 4] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [2, 1] -> [1, 1] -> [1, 2] -> [1, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [2, 1] -> [3, 1] -> [3, 2] -> [3, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [3, 2] -> [3, 3] -> [3, 4] -> [2, 4] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 1] -> [2, 1] -> [3, 1] -> [3, 2] -> [3, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 3] -> [1, 4] -> [2, 4] -> [3, 4] -> [3, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [2, 1] -> [1, 1] -> [1, 2] -> [1, 3] -> [1, 4] -> [2, 4] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [2, 1] -> [3, 1] -> [3, 2] -> [3, 3] -> [3, 4] -> [2, 4] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [3, 2] -> [3, 1] -> [2, 1] -> [1, 1] -> [1, 2] -> [1, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [3, 2] -> [3, 3] -> [3, 4] -> [2, 4] -> [1, 4] -> [1, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 1] -> [2, 1] -> [3, 1] -> [3, 2] -> [3, 3] -> [3, 4] -> [2, 4] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [2, 1] -> [1, 1] -> [1, 2] -> [1, 3] -> [1, 4] -> [2, 4] -> [3, 4] -> [3, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [2, 1] -> [3, 1] -> [3, 2] -> [3, 3] -> [3, 4] -> [2, 4] -> [1, 4] -> [1, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [3, 2] -> [3, 1] -> [2, 1] -> [1, 1] -> [1, 2] -> [1, 3] -> [1, 4] -> [2, 4] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [1, 2] -> [1, 1] -> [2, 1] -> [3, 1] -> [3, 2] -> [3, 3] -> [3, 4] -> [2, 4] -> [1, 4] -> [1, 3] -> [2, 3] -> FINISH",
                "START -> [2, 2] -> [3, 2] -> [3, 1] -> [2, 1] -> [1, 1] -> [1, 2] -> [1, 3] -> [1, 4] -> [2, 4] -> [3, 4] -> [3, 3] -> [2, 3] -> FINISH"));
    }

    @Test
    public void testMazeEquals() {
        Maze maze1 = new Maze("SF");
        Maze maze2 = new Maze("SF");

        // basics: identity, null, type
        assertThat(maze1, is(equalTo(maze1)));
        assertFalse(maze1.equals(null));
        assertFalse(maze1.equals("SF"));

        // equals and hashCode match
        assertThat(maze1, is(equalTo(maze2)));
        assertThat(maze1.hashCode(), is(maze2.hashCode()));

        // mazes are equal if they have the same grid, but the solutions are unique because the squares are unique
        assertThat(maze1.solve(), is(not(maze2.solve())));

        // different mazes with squares in common
        assertThat(new Maze("S F"), is(not(new Maze("S  F"))));
        assertThat(new Maze("F S"), is(not(new Maze("F  S"))));
        assertThat(new Maze("S\nF"), is(not(new Maze("S\n\nF"))));
        assertThat(new Maze("F\nS"), is(not(new Maze("F\n\nS"))));
    }

    @Test
    public void testSquareEquals() {
        Maze maze1 = new Maze("SF");
        Maze maze2 = new Maze("SF");

        // basics: identity, null, type, hashCode
        assertThat(maze1.getStart(), is(equalTo(maze1.getStart())));
        assertThat(maze1.getStart().hashCode(), is(maze1.getStart().hashCode()));
        assertFalse(maze1.getStart().equals(null));
        assertFalse(maze1.getStart().equals("S"));

        // mazes are equal because they have the same grid, but the squares are not because they belong to different mazes
        assertThat(maze1, is(equalTo(maze2)));
        assertThat(maze1.getStart(), is(not(maze2.getStart())));
        assertThat(maze1.getStart().getRow(), is(maze1.getStart().getRow()));
        assertThat(maze1.getStart().getCol(), is(maze1.getStart().getCol()));
        assertThat(maze1.getFinish(), is(not(maze2.getFinish())));
        assertThat(maze1.getFinish().getRow(), is(maze1.getFinish().getRow()));
        assertThat(maze1.getFinish().getCol(), is(maze1.getFinish().getCol()));
    }

    @Test
    public void testDrawColorMaze() {
        Maze m = new Maze(new StringBuilder()
                .append(" S*   \n")
                .append(" ** * \n")
                .append("    * \n")
                .append(" ****\n")
                .append("     F\n")
                .toString());

        String colorMaze = m.drawMaze(true);

        // strip all ANSI sequences and it should match a normal draw
        assertThat(colorMaze.replaceAll("\u001B\\[[0-9;]+m", ""), is(m.drawMaze(false)));

        // every char should have at least one ANSI sequence on each side
        String[] ansi_sequences = colorMaze.split("[" + Maze.SPACE + Maze.WALL + Maze.START + Maze.FINISH + "]", -1);
        for(String sequence : ansi_sequences ) {
            assertTrue(sequence.matches("^(?:\u001B\\[[0-9;]+m\n?)+$"));
        }
    }

    @Test
    public void testDrawColorSolution() {
        Maze m = new Maze(new StringBuilder()
                .append(" S*   \n")
                .append(" ** * \n")
                .append("    * \n")
                .append(" ****\n")
                .append("     F\n")
                .toString());
        List<Maze.Square> path = m.solve().iterator().next();

        String colorMaze = m.drawMaze(path, true);

        // strip all ANSI sequences and it should match a normal draw
        assertThat(colorMaze.replaceAll("\u001B\\[[0-9;]+m", ""), is(m.drawMaze(path, false)));

        // every char should have at least one ANSI sequence on each side
        String[] ansi_sequences = colorMaze.split("[" + Maze.SPACE + Maze.WALL + Maze.START + Maze.FINISH + Maze.MOVE + "]", -1);
        for(String sequence : ansi_sequences ) {
            assertTrue(sequence.matches("^(?:\u001B\\[[0-9;]+m\n?)+$"));
        }
    }

    @Test
    public void testEmptyDrawMaze() {
        Maze m = new Maze("S F");
        List<Maze.Square> empty = Collections.emptyList();
        // an empty solution is the same as having no solution
        assertThat(m.drawMaze(empty), is(m.drawMaze()));
        // so is a null solution
        assertThat(m.drawMaze(null), is(m.drawMaze()));
        // same thing when in color
        assertThat(m.drawMaze(empty, true), is(m.drawMaze(true)));
        assertThat(m.drawMaze(null, true), is(m.drawMaze(true)));
    }

    // helper method
    private Set<String> transformSolutionsToString(Set<List<Maze.Square>> solutions) {
        Set<String> results = new HashSet<String>();
        for (List<Maze.Square> path : solutions) {
            results.add(Maze.getPathString(path));
        }
        return Collections.unmodifiableSet(results);
    }
}