public class Mention {
    private final String source;
    private final String destination;
    private final int nbMentions;
    public Mention(String source, String destination, int nbMentions) {
        this.source = source;
        this.destination = destination;
        this.nbMentions = nbMentions;
    }
    public String getSource() {
        return source;
    }
    public String getDestination() {
        return destination;
    }
    public int getNbMentions() {
        return nbMentions;
    }
    @Override
    public String toString() {
        return "Mention [source=" + source + ", destination=" + destination + ", nbMentions=" + nbMentions + "]";
    }
}
