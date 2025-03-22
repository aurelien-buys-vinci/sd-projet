import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;

class Graph {
    private Map<Integer, Artist> artistsById;
    private Map<String, Artist> artistsByName;
    private Map<Integer, Set<Mention>> adjacencyList;

    public Graph(String artistsFile, String mentionsFile) {
        artistsById = new HashMap<>();
        artistsByName = new HashMap<>();
        adjacencyList = new HashMap<>();

        try {
            loadArtists(artistsFile);
            loadMentions(mentionsFile);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture des fichiers : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadArtists(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String category = parts[2];

                    Artist artist = new Artist(id, name, category);
                    artistsById.put(id, artist);
                    artistsByName.put(name, artist);
                    adjacencyList.put(id, new HashSet<>(){
                    });
                }
            }
        }
    }

    private void loadMentions(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int sourceId = Integer.parseInt(parts[0]);
                    int destId = Integer.parseInt(parts[1]);
                    double nbMentions = (double) 1 /Integer.parseInt(parts[2]);

                    if (artistsById.containsKey(sourceId) && artistsById.containsKey(destId)) {
                        Mention mention = new Mention(sourceId, destId, nbMentions);
                        adjacencyList.get(sourceId).add(mention);
                    }
                }
            }
        }
    }

    public Artist getArtistById(int id) {
        return artistsById.get(id);
    }

    public Artist getArtistByName(String name) {
        return artistsByName.get(name);
    }

    public Set<Mention> getOutgoingMentions(int artistId) {
        return adjacencyList.get(artistId);
    }

    public boolean areConnected(int sourceId, int destId) {
        for (Mention mention : getOutgoingMentions(sourceId)) {
            if (mention.getDestination() == destId) {
                return true;
            }
        }
        return false;
    }

    // Méthode pour trouver le chemin le plus court entre deux artistes (en nombre d'arcs)
    public void trouverCheminLePlusCourt(String sourceNom, String destNom) {

    }

    // Méthode pour trouver le chemin avec le maximum de mentions entre deux artistes
    public void trouverCheminMaxMentions(String sourceNom, String destNom) {

    }
}