import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

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
    
    public void trouverCheminLePlusCourt(String sourceNom, String destNom) {
        Artist sourceArtist = getArtistByName(sourceNom);
        Artist destArtist = getArtistByName(destNom);

        if (sourceArtist == null || destArtist == null) {
            System.out.println("Un des artistes n'existe pas.");
            return;
        }

        int sourceId = sourceArtist.getId();
        int destId = destArtist.getId();

        Deque<Integer> pile = new ArrayDeque<>();
        Map<Integer, Integer> predecesseurs = new HashMap<>();
        Set<Integer> visites = new HashSet<>();

        pile.add(sourceId);
        predecesseurs.put(sourceId, null);
        visites.add(sourceId);

        boolean trouve = false;
        while (!pile.isEmpty() && !trouve) {
            int current = pile.pop();
            if (current == destId) {
                break;
            }
            for (Mention mention : getOutgoingMentions(current)) {
                int voisin = mention.getDestination();
                if (!visites.contains(voisin)) {
                    pile.add(voisin);
                    predecesseurs.put(voisin, current);
                    visites.add(voisin);
                }
                if (voisin == destId){
                    trouve = true;
                    break;
                }
            }
        }
        if(!trouve){
            System.out.println("Il n'y a pas de chemin entre " + sourceNom + " et " + destNom);
            return;
        }

        Deque<Integer> chemin = new ArrayDeque<>();
        int current = destId;
        double cout = 0;
        while (current != sourceId) {
            for (Mention mention : getOutgoingMentions(predecesseurs.get(current))) {
                if (mention.getDestination() == current) {
                    cout += mention.getNbMentions();
                    break;
                }
            }
            chemin.addFirst(current);
            current = predecesseurs.get(current);
        }
        System.out.println("Longueur de chemin : " + chemin.size());
        System.out.println("Cout total du chemin : " + cout);
        chemin.addFirst(sourceId);
        System.out.println("Chemin : ");
        for (Integer i : chemin) {
            Artist artist = getArtistById(i);
            System.out.println(artist.getName() + " (" + artist.getCategory() + ")");
        }
    }


    public void trouverCheminMaxMentions(String sourceNom, String destNom) {
        Artist sourceArtist = getArtistByName(sourceNom);
        Artist destArtist = getArtistByName(destNom);

        if (sourceArtist == null || destArtist == null) {
            System.out.println("Un des artistes n'existe pas.");
            return;
        }

        int sourceId = sourceArtist.getId();
        int destId = destArtist.getId();

    }
}