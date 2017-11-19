import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Example {
    public static void main(String[] args) throws IOException {
        // check the command line for color usage
        boolean useColor = true;
        String filename = null;
        if (0 < args.length) {
            boolean nextIsPath = false;
            for (String arg : args) {
                if (nextIsPath) {
                    filename = arg;
                    nextIsPath = false;
                } else if (arg.equalsIgnoreCase("--nocolor"))
                    useColor = false;
                else if (arg.equalsIgnoreCase("--color"))
                    useColor = true;
                else if (arg.equalsIgnoreCase("--file"))
                    nextIsPath = true;
                else
                    System.err.println(String.format("Unknown parameter; ignoring: %s", arg));
            }
        }

        // load maze from file if presented, or use default if not
        String mazeBuffer = null == filename ? defaultMaze() : readFile(filename);

        // build a reasonably complicated maze with multiple solutions
        Maze maze = new Maze(mazeBuffer);

        // print empty maze
        System.out.println(maze.drawMaze(useColor));

        // print the number of solutions
        System.out.println("Solving...");
        int size = maze.solve().size();
        System.out.println(String.format("There %s %d solution%s to this maze.",
                1 == size ? "is" : "are", size, 1 == size ? "" : "s"));

        if (0 < size) {
            // find the longest and shortest solutions...
            List<Maze.Square> shortest = null;
            List<Maze.Square> longest = null;
            for (List<Maze.Square> path : maze.solve()) {
                if (null == shortest || path.size() < shortest.size())
                    shortest = path;
                if (null == longest || path.size() > longest.size())
                    longest = path;
            }

            // ...and print them
            System.out.println(String.format("\nThe %ssolution is %d moves: %s\n\n%s", 1 == size ? "" : "shortest ",
                    shortest.size(), Maze.getPathString(shortest), maze.drawMaze(shortest, useColor)));
            if (1 < size) {
                System.out.println(String.format("\nThe longest solution is %d moves: %s\n\n%s",
                        longest.size(), Maze.getPathString(longest), maze.drawMaze(longest, useColor)));
            }
        }
    }

    static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    static String defaultMaze() {
        return new StringBuilder()
                .append("       *       *                       *      **           \n")
                .append(" ***** * ***** * * ***** * * ********* ******  * *** * ** *\n")
                .append(" *     * *   * * * *   * * * *       *        ** * * * *   \n")
                .append(" * ***** * * * * * * * * * ********* * ******* * * * ******\n")
                .append(" * *     * * * *   * * * *         *   *           *       \n")
                .append(" * * *** * *** * *** * * * ******* * *** **************** *\n")
                .append(" * *   * *   *     *  *  * *     * * *F* * *               \n")
                .append(" * ***** ** ** * **** ** * **** ** * * * * * ************* \n")
                .append(" *             *   *     *       * * * * * * *     *     * \n")
                .append(" ************* ********* ********* * * * * * * ** ** *** * \n")
                .append("             *         *         * *   * * * * *   * * * * \n")
                .append(" *********** ********* ********* * ***** * * * ***** * * * \n")
                .append(" *         * *       * *       * *     * * * *         * * \n")
                .append(" * ******* * * ***** * * ***** * **** ** * * *********** * \n")
                .append(" * *     * * * *   * * * *   * * *     * * *           *   \n")
                .append(" * **** ** * * * * * * * * * * * * ***** * *********** ****\n")
                .append(" *       * * * * * * * * * * *   * *   * *   *   *   * *   \n")
                .append("   * * *   * * *  *  * *   *   *     *   * *   *   *     * \n")
                .append("**** * ** ** * * ** ** * ***************** * ************* \n")
                .append("   * * *   * * *     * * *       *         * * *           \n")
                .append(" * * * * * * * ***** * * ****** **** ***** * * * **********\n")
                .append(" * * * * *   * *   *   * *           *   * * * * *       * \n")
                .append(" *** * ******* * * ***** * * ** ******* ** * * * * * *** * \n")
                .append("   * *           *         * *   *         * *   * *   *   \n")
                .append(" * * ************* ****************** ****** *** ***** ****\n")
                .append(" * * *   *         *               *       * * *     *     \n")
                .append(" * * * * * ********* ************* ********* * ***** * *** \n")
                .append(" * * * * * *       * *           *           *   * * * * * \n")
                .append(" *** * *** * ******* * *********************** * * * * * * \n")
                .append("     *   * * *       * *                   *   * * * * * * \n")
                .append(" ***** * * * * ***** * * *********** ***** * * * * * * * * \n")
                .append(" *     * * * * *   * * * *     *   * *   * * * * * * * * * \n")
                .append(" ******* * * * * * * * * * * * *** * ** ** * * * * * * * * \n")
                .append("       * * * * * * * * * * * *     * *     * * *   * * * * \n")
                .append("****** * * * * *** * * * *** ******* * ***** * ***** * * * \n")
                .append("     * * * *       * *       *     * *     * *       *   * \n")
                .append("* **** * * ********* ********** ** * ******* ****** ****** \n")
                .append("         *                       *S*             *         \n")
                .toString();
    }
}
