public class Song {

    private String title;
    private String artist;
    private String genre;
    private double length;
    private boolean added;

    public Song(String title, String artist, String genre, double length, boolean added){
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.length = length;
        this.added = added;
    }

    public void setAdded(boolean added){
        this.added = added;
    }

    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }

    public String getGenre(){
        return genre;
    }

    public double getLength(){
        return length;
    }

    public boolean getAdded(){
        return added;
    }

    public String toString(){
        return "test:" + title;
    }
}
