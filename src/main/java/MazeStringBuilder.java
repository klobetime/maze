/* package */ class MazeStringBuilder {
    private StringBuilder builder;

    public MazeStringBuilder(int capacity) {
        builder = new StringBuilder(capacity);
    }

    public MazeStringBuilder append(char x) {
        getBuilder().append(x);
        return this;
    }

    @Override
    public String toString() {
        return getBuilder().toString();
    }

    protected StringBuilder getBuilder() {
        return builder;
    }
}
