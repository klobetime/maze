import java.util.*;

/* package */ class ColorMazeStringBuilder extends MazeStringBuilder {

    // ANSI color triggers
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[0;30m";
    private static final String ANSI_RED = "\u001B[0;31m";
    private static final String ANSI_BLUE_BOLD = "\033[1;34m";
    private static final String ANSI_WHITE = "\u001B[0;37m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final Map<Character, String> COLOR_MAP;
    static {
        Map<Character, String> colorMap = new HashMap<Character, String>();
        colorMap.put(Maze.SPACE, ANSI_WHITE + ANSI_WHITE_BACKGROUND);
        colorMap.put(Maze.START, ANSI_BLUE_BOLD + ANSI_WHITE_BACKGROUND);
        colorMap.put(Maze.FINISH, ANSI_BLUE_BOLD + ANSI_WHITE_BACKGROUND);
        colorMap.put(Maze.MOVE, ANSI_RED + ANSI_WHITE_BACKGROUND);
        colorMap.put(Maze.WALL, ANSI_BLACK + ANSI_BLACK_BACKGROUND);
        COLOR_MAP = Collections.unmodifiableMap(colorMap);
    }

    public ColorMazeStringBuilder(int capacity) {
        super(capacity * 5);
    }

    @Override
    public MazeStringBuilder append(char x) {
        if (COLOR_MAP.containsKey(x))
            getBuilder().append(COLOR_MAP.get(x));
        else
            getBuilder().append(ANSI_RESET);
        return super.append(x);
    }

    @Override
    public String toString() {
        getBuilder().append(ANSI_RESET);
        return super.toString();
    }
}
