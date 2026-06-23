public class PlaylistSongView {
    private final String songId;
    private final String title;
    private final String artist;

    public PlaylistSongView(String songId, String title, String artist) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
    }

    public String getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return songId + " | " + title + " | " + artist;
    }
}
