public class SoundWaveEngine {
    private final SimpleHashMap<String, Song> songsById;
    private final SimpleHashMap<String, Playlist> playlistsById;
    private final SongQueue upNextQueue;

    // Documented policy: playlists allow duplicates.
    private final boolean playlistAllowsDuplicates;

    public SoundWaveEngine() {
        this.songsById = new SimpleHashMap<String, Song>();
        this.playlistsById = new SimpleHashMap<String, Playlist>();
        this.upNextQueue = new SongQueue();
        this.playlistAllowsDuplicates = true;
    }

    public boolean addSong(Song song) {
        if (song == null) {
            return false;
        }
        if (!Song.isValid(song.getSongId(), song.getTitle(), song.getArtist(), song.getAlbum(),
                song.getYear(), song.getDurationSec(), song.getGenres(), song.getPopularity())) {
            return false;
        }
        return songsById.put(song.getSongId(), song);
    }

    public boolean removeSong(String songId) {
        if (isBlank(songId)) {
            return false;
        }
        Song removed = songsById.remove(songId);
        if (removed == null) {
            return false;
        }

        DynamicArray<String> playlistIds = playlistsById.keys();
        for (int i = 0; i < playlistIds.size(); i++) {
            Playlist playlist = playlistsById.get(playlistIds.get(i));
            if (playlist != null) {
                playlist.removeAllSongOccurrencesNoHistory(songId);
                playlist.purgeHistoryForSong(songId);
            }
        }

        upNextQueue.removeAll(songId);
        return true;
    }

    public Song getSongById(String songId) {
        if (isBlank(songId)) {
            return null;
        }
        return songsById.get(songId);
    }

    public DynamicArray<Song> listSongsByArtist(String artist) {
        if (isBlank(artist)) {
            return null;
        }
        String wanted = normalizeBasic(artist);
        DynamicArray<Song> all = songsById.values();
        DynamicArray<Song> out = new DynamicArray<Song>();
        for (int i = 0; i < all.size(); i++) {
            Song song = all.get(i);
            if (song != null && normalizeBasic(song.getArtist()).equals(wanted)) {
                out.add(song);
            }
        }
        return out;
    }

    public DynamicArray<Song> searchSongsByTitleKeyword(String keyword) {
        if (isBlank(keyword)) {
            return null;
        }
        String normalizedKeyword = normalizeForSearch(keyword);
        if (normalizedKeyword.isEmpty()) {
            return null;
        }
        DynamicArray<Song> all = songsById.values();
        DynamicArray<Song> out = new DynamicArray<Song>();
        for (int i = 0; i < all.size(); i++) {
            Song song = all.get(i);
            if (song == null) {
                continue;
            }
            String normalizedTitle = normalizeForSearch(song.getTitle());
            if (normalizedTitle.contains(normalizedKeyword)) {
                out.add(song);
            }
        }
        return out;
    }

    public boolean createPlaylist(String playlistId, String displayName) {
        if (isBlank(playlistId)) {
            return false;
        }
        Playlist playlist = new Playlist(playlistId.trim(), displayName);
        return playlistsById.put(playlistId.trim(), playlist);
    }

    public boolean deletePlaylist(String playlistId) {
        if (isBlank(playlistId)) {
            return false;
        }
        Playlist removed = playlistsById.remove(playlistId.trim());
        return removed != null;
    }

    public boolean addSongToPlaylist(String playlistId, String songId, Integer position) {
        if (isBlank(playlistId) || isBlank(songId)) {
            return false;
        }
        Playlist playlist = playlistsById.get(playlistId.trim());
        if (playlist == null) {
            return false;
        }
        if (!songsById.containsKey(songId.trim())) {
            return false;
        }
        return playlist.addSong(songId.trim(), position, playlistAllowsDuplicates);
    }

    public boolean removeSongFromPlaylistByPosition(String playlistId, int position) {
        if (isBlank(playlistId)) {
            return false;
        }
        Playlist playlist = playlistsById.get(playlistId.trim());
        if (playlist == null) {
            return false;
        }
        return playlist.removeSongByPosition(position);
    }

    public boolean removeSongFromPlaylistBySongId(String playlistId, String songId) {
        if (isBlank(playlistId) || isBlank(songId)) {
            return false;
        }
        Playlist playlist = playlistsById.get(playlistId.trim());
        if (playlist == null) {
            return false;
        }
        return playlist.removeSongBySongId(songId.trim());
    }

    public DynamicArray<PlaylistSongView> showPlaylist(String playlistId) {
        if (isBlank(playlistId)) {
            return null;
        }
        Playlist playlist = playlistsById.get(playlistId.trim());
        if (playlist == null) {
            return null;
        }
        DynamicArray<String> songIds = playlist.snapshotSongIds();
        DynamicArray<PlaylistSongView> out = new DynamicArray<PlaylistSongView>();
        for (int i = 0; i < songIds.size(); i++) {
            String songId = songIds.get(i);
            Song song = songsById.get(songId);
            if (song != null) {
                out.add(new PlaylistSongView(song.getSongId(), song.getTitle(), song.getArtist()));
            }
        }
        return out;
    }

    public boolean addNext(String songId) {
        if (isBlank(songId)) {
            return false;
        }
        if (!songsById.containsKey(songId.trim())) {
            return false;
        }
        return upNextQueue.enqueue(songId.trim());
    }

    public Song playNext() {
        String songId = upNextQueue.dequeue();
        if (songId == null) {
            return null;
        }
        return songsById.get(songId);
    }

    public Song peekNext() {
        String songId = upNextQueue.peek();
        if (songId == null) {
            return null;
        }
        return songsById.get(songId);
    }

    public DynamicArray<Song> showUpNext() {
        DynamicArray<String> songIds = upNextQueue.toArray();
        DynamicArray<Song> out = new DynamicArray<Song>();
        for (int i = 0; i < songIds.size(); i++) {
            Song song = songsById.get(songIds.get(i));
            if (song != null) {
                out.add(song);
            }
        }
        return out;
    }

    public boolean undo(String playlistId) {
        if (isBlank(playlistId)) {
            return false;
        }
        Playlist playlist = playlistsById.get(playlistId.trim());
        if (playlist == null) {
            return false;
        }
        return playlist.undo(new Playlist.SongAvailability() {
            @Override
            public boolean exists(String songId) {
                return songsById.containsKey(songId);
            }
        }, playlistAllowsDuplicates);
    }

    public DynamicArray<Song> recommend() {
        return recommend(5);
    }

    public DynamicArray<Song> recommend(int limit) {
        if (limit <= 0) {
            return new DynamicArray<Song>();
        }
        DynamicArray<Song> allSongs = songsById.values();
        Song[] sorted = new Song[allSongs.size()];
        for (int i = 0; i < allSongs.size(); i++) {
            sorted[i] = allSongs.get(i);
        }

        for (int i = 1; i < sorted.length; i++) {
            Song current = sorted[i];
            int j = i - 1;
            while (j >= 0 && compareForRecommendation(current, sorted[j]) < 0) {
                sorted[j + 1] = sorted[j];
                j--;
            }
            sorted[j + 1] = current;
        }

        DynamicArray<Song> out = new DynamicArray<Song>();
        int take = limit < sorted.length ? limit : sorted.length;
        for (int i = 0; i < take; i++) {
            out.add(sorted[i]);
        }
        return out;
    }

    public boolean isPlaylistDuplicateAllowed() {
        return playlistAllowsDuplicates;
    }

    private int compareForRecommendation(Song left, Song right) {
        if (left.getPopularity() != right.getPopularity()) {
            return right.getPopularity() - left.getPopularity();
        }
        return left.getSongId().compareTo(right.getSongId());
    }

    public static String[] parseGenres(String genresField) {
        if (genresField == null) {
            return new String[0];
        }
        String[] raw = splitByPipe(genresField);
        DynamicArray<String> out = new DynamicArray<String>();
        for (int i = 0; i < raw.length; i++) {
            String trimmed = raw[i] == null ? "" : raw[i].trim();
            if (!trimmed.isEmpty()) {
                out.add(trimmed);
            }
        }
        String[] genres = new String[out.size()];
        for (int i = 0; i < out.size(); i++) {
            genres[i] = out.get(i);
        }
        return genres;
    }

    private static String[] splitByPipe(String value) {
        if (value.isEmpty()) {
            return new String[]{""};
        }
        DynamicArray<String> parts = new DynamicArray<String>();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '|') {
                parts.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());
        String[] out = new String[parts.size()];
        for (int i = 0; i < parts.size(); i++) {
            out[i] = parts.get(i);
        }
        return out;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String normalizeBasic(String value) {
        return value.trim().toLowerCase();
    }

    public static String normalizeForSearch(String value) {
        String lower = value.toLowerCase();
        StringBuilder normalized = new StringBuilder();
        boolean previousWasSpace = false;
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                normalized.append(c);
                previousWasSpace = false;
            } else if (!previousWasSpace) {
                normalized.append(' ');
                previousWasSpace = true;
            }
        }
        return normalized.toString().trim();
    }
}
