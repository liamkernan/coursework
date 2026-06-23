public class Song {
    private final String songId;
    private final String title;
    private final String artist;
    private final String album;
    private final int year;
    private final int durationSec;
    private final String[] genres;
    private final int popularity;

    public Song(String songId, String title, String artist, String album, int year,
                int durationSec, String[] genres, int popularity) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.durationSec = durationSec;
        this.genres = copyGenres(genres);
        this.popularity = popularity;
    }

    public static boolean isValid(String songId, String title, String artist, String album, int year,
                                  int durationSec, String[] genres, int popularity) {
        if (isBlank(songId) || isBlank(title) || isBlank(artist)) {
            return false;
        }
        if (album == null) {
            return false;
        }
        if (year < -1) {
            return false;
        }
        if (durationSec < 0) {
            return false;
        }
        if (popularity < 0 || popularity > 100) {
            return false;
        }
        if (genres == null || genres.length == 0) {
            return false;
        }
        for (int i = 0; i < genres.length; i++) {
            if (isBlank(genres[i])) {
                return false;
            }
        }
        return true;
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

    public String getAlbum() {
        return album;
    }

    public int getYear() {
        return year;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public String[] getGenres() {
        return copyGenres(genres);
    }

    public int getPopularity() {
        return popularity;
    }

    @Override
    public String toString() {
        return songId + " | " + title + " | " + artist + " | pop=" + popularity;
    }

    private String[] copyGenres(String[] values) {
        if (values == null) {
            return new String[0];
        }
        String[] out = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = values[i];
        }
        return out;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
