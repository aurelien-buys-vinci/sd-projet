public class PoidsSource implements Comparable<PoidsSource> {
  private int artist;
  private double poids;

  public PoidsSource(double poids, int artist) {
    this.poids = poids;
    this.artist = artist;
  }

  public int getArtist() {
    return artist;
  }

  public double getPoids() {
    return poids;
  }

  @Override
  public int compareTo(PoidsSource o) {
    return Double.compare(this.poids, o.poids);
  }
}
