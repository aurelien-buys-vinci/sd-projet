public class PoidsSource implements Comparable<PoidsSource> {
  private int idArtist;
  private double poids;

  public PoidsSource(double poids, int idArtist) {
    this.poids = poids;
    this.idArtist = idArtist;
  }

  public int getIdArtist() {
    return idArtist;
  }

  public double getPoids() {
    return poids;
  }

  @Override
  public int compareTo(PoidsSource o) {
    return Double.compare(this.poids, o.poids);
  }
}
