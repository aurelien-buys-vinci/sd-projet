public class Mention {
    private final int source;
    private final int destination;
    private final double nbMentions;

    public Mention(int source, int destination, double nbMentions) {
        this.source = source;
        this.destination = destination;
        this.nbMentions = nbMentions;
    }

    public int getSource() {
        return source;
    }
    public int getDestination() {
        return destination;
    }
    public double getNbMentions() {
        return nbMentions;
    }

    @Override
    public String toString() {
        return "Mention [source=" + source + ", destination=" + destination + ", nbMentions=" + nbMentions + "]";
    }
}
