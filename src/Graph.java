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
    private Map<String, Artist> artistsById;
    private Map<String, Artist> artistsByName;
    private Map<String, List<Mention>> adjacencyList;

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
                    String id = parts[0];
                    String name = parts[1];
                    String category = parts[2];

                    Artist artist = new Artist(id, name, category);
                    artistsById.put(id, artist);
                    artistsByName.put(name, artist);
                    adjacencyList.put(id, new ArrayList<>());
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
                    String sourceId = parts[0];
                    String destId = parts[1];
                    int nbMentions = Integer.parseInt(parts[2]);

                    if (artistsById.containsKey(sourceId) && artistsById.containsKey(destId)) {
                        Mention mention = new Mention(sourceId, destId, nbMentions);
                        adjacencyList.get(sourceId).add(mention);
                    }
                }
            }
        }
    }

    public Artist getArtistById(String id) {
        return artistsById.get(id);
    }

    public Artist getArtistByName(String name) {
        return artistsByName.get(name);
    }

    public List<Mention> getOutgoingMentions(String artistId) {
        return adjacencyList.getOrDefault(artistId, new ArrayList<>());
    }

    public boolean areConnected(String sourceId, String destId) {
        for (Mention mention : adjacencyList.getOrDefault(sourceId, new ArrayList<>())) {
            if (mention.getDestination().equals(destId)) {
                return true;
            }
        }
        return false;
    }

    // Méthode pour trouver le chemin le plus court entre deux artistes (en nombre d'arcs)
    public void trouverCheminLePlusCourt(String sourceNom, String destNom) {
        Artist sourceArtist = artistsByName.get(sourceNom);
        Artist destArtist = artistsByName.get(destNom);

        if (sourceArtist == null || destArtist == null) {
            System.out.println("Artiste(s) non trouvé(s)");
            return;
        }

        String sourceId = sourceArtist.getId();
        String destId = destArtist.getId();

        // Implémentation de l'algorithme BFS pour trouver le chemin le plus court
        Map<String, String> predecesseurs = new HashMap<>();
        Set<String> visites = new HashSet<>();
        List<String> file = new ArrayList<>();

        file.add(sourceId);
        visites.add(sourceId);

        boolean trouve = false;

        while (!file.isEmpty() && !trouve) {
            String courant = file.remove(0);

            for (Mention mention : adjacencyList.get(courant)) {
                String voisin = mention.getDestination();

                if (!visites.contains(voisin)) {
                    visites.add(voisin);
                    file.add(voisin);
                    predecesseurs.put(voisin, courant);

                    if (voisin.equals(destId)) {
                        trouve = true;
                        break;
                    }
                }
            }
        }

        if (trouve) {
            // Reconstruction du chemin
            List<String> chemin = new ArrayList<>();
            String courant = destId;

            while (!courant.equals(sourceId)) {
                chemin.add(0, courant);
                courant = predecesseurs.get(courant);
            }

            chemin.add(0, sourceId);

            // Affichage du chemin
            System.out.println("Chemin le plus court de " + sourceNom + " à " + destNom + " :");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chemin.size(); i++) {
                sb.append(artistsById.get(chemin.get(i)).getName());
                if (i < chemin.size() - 1) {
                    sb.append(" -> ");
                }
            }
            System.out.println(sb.toString());
            System.out.println("Longueur du chemin : " + (chemin.size() - 1));
        } else {
            System.out.println("Aucun chemin trouvé entre " + sourceNom + " et " + destNom);
        }
    }

    // Méthode pour trouver le chemin avec le maximum de mentions entre deux artistes
    public void trouverCheminMaxMentions(String sourceNom, String destNom) {
        Artist sourceArtist = artistsByName.get(sourceNom);
        Artist destArtist = artistsByName.get(destNom);

        if (sourceArtist == null || destArtist == null) {
            System.out.println("Artiste(s) non trouvé(s)");
            return;
        }

        String sourceId = sourceArtist.getId();
        String destId = destArtist.getId();

        // Implémentation de l'algorithme de Dijkstra modifié pour trouver le chemin avec le maximum de mentions
        Map<String, Integer> mentions = new HashMap<>();
        Map<String, String> predecesseurs = new HashMap<>();
        Set<String> visites = new HashSet<>();

        // Initialisation
        for (String id : artistsById.keySet()) {
            mentions.put(id, Integer.MIN_VALUE);
        }
        mentions.put(sourceId, 0);

        // File de priorité pour sélectionner le nœud avec le plus de mentions
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(id -> -mentions.get(id)));
        pq.add(sourceId);

        while (!pq.isEmpty()) {
            String courant = pq.poll();

            if (courant.equals(destId)) {
                break;
            }

            if (visites.contains(courant)) {
                continue;
            }

            visites.add(courant);

            for (Mention mention : adjacencyList.get(courant)) {
                String voisin = mention.getDestination();
                int nouvellesMentions = mentions.get(courant) + mention.getNbMentions();

                if (nouvellesMentions > mentions.get(voisin)) {
                    mentions.put(voisin, nouvellesMentions);
                    predecesseurs.put(voisin, courant);
                    pq.add(voisin);
                }
            }
        }

        if (predecesseurs.containsKey(destId)) {
            // Reconstruction du chemin
            List<String> chemin = new ArrayList<>();
            String courant = destId;

            while (!courant.equals(sourceId)) {
                chemin.add(0, courant);
                courant = predecesseurs.get(courant);
            }

            chemin.add(0, sourceId);

            // Affichage du chemin
            System.out.println("Chemin avec le maximum de mentions de " + sourceNom + " à " + destNom + " :");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chemin.size(); i++) {
                sb.append(artistsById.get(chemin.get(i)).getName());
                if (i < chemin.size() - 1) {
                    sb.append(" -> ");
                }
            }
            System.out.println(sb.toString());
            System.out.println("Nombre total de mentions : " + mentions.get(destId));
        } else {
            System.out.println("Aucun chemin trouvé entre " + sourceNom + " et " + destNom);
        }
    }
}