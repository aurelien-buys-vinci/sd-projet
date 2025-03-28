import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Artist {
    private int id;
    private String name;
    private String category;

    public Artist(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Artist artist)) return false;
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return name + " (" + category + ")";
    }
}


