public class Playlist {
    public interface SongAvailability {
        boolean exists(String songId);
    }

    private final String playlistId;
    private final String displayName;
    private final DynamicArray<String> songIds;
    private final SimpleStack<PlaylistEdit> history;

    public Playlist(String playlistId, String displayName) {
        this.playlistId = playlistId;
        this.displayName = displayName == null ? "" : displayName;
        this.songIds = new DynamicArray<String>();
        this.history = new SimpleStack<PlaylistEdit>();
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int size() {
        return songIds.size();
    }

    public DynamicArray<String> snapshotSongIds() {
        return songIds.copy();
    }

    public boolean addSong(String songId, Integer position, boolean allowDuplicates) {
        if (songId == null) {
            return false;
        }
        if (!allowDuplicates && songIds.contains(songId)) {
            return false;
        }
        int pos = position == null ? songIds.size() : position.intValue();
        if (pos < 0 || pos > songIds.size()) {
            return false;
        }
        if (!songIds.addAt(pos, songId)) {
            return false;
        }
        history.push(new PlaylistEdit(PlaylistEdit.TYPE_ADD, songId, pos));
        return true;
    }

    public boolean removeSongByPosition(int position) {
        if (position < 0 || position >= songIds.size()) {
            return false;
        }
        String removedSongId = songIds.removeAt(position);
        if (removedSongId == null) {
            return false;
        }
        history.push(new PlaylistEdit(PlaylistEdit.TYPE_REMOVE, removedSongId, position));
        return true;
    }

    public boolean removeSongBySongId(String songId) {
        if (songId == null) {
            return false;
        }
        int index = songIds.indexOf(songId);
        if (index < 0) {
            return false;
        }
        String removedSongId = songIds.removeAt(index);
        if (removedSongId == null) {
            return false;
        }
        history.push(new PlaylistEdit(PlaylistEdit.TYPE_REMOVE, removedSongId, index));
        return true;
    }

    public void removeAllSongOccurrencesNoHistory(String songId) {
        if (songId == null) {
            return;
        }
        int i = 0;
        while (i < songIds.size()) {
            String current = songIds.get(i);
            if (songId.equals(current)) {
                songIds.removeAt(i);
            } else {
                i++;
            }
        }
    }

    public void purgeHistoryForSong(String songId) {
        if (songId == null) {
            return;
        }
        SimpleStack<PlaylistEdit> filtered = new SimpleStack<PlaylistEdit>();
        while (!history.isEmpty()) {
            PlaylistEdit edit = history.pop();
            if (!songId.equals(edit.getSongId())) {
                filtered.push(edit);
            }
        }
        while (!filtered.isEmpty()) {
            history.push(filtered.pop());
        }
    }

    public boolean undo(SongAvailability availability, boolean allowDuplicates) {
        if (history.isEmpty()) {
            return false;
        }
        PlaylistEdit edit = history.pop();
        if (edit == null) {
            return false;
        }

        if (edit.getType() == PlaylistEdit.TYPE_ADD) {
            boolean removed = undoAdd(edit);
            if (!removed) {
                history.push(edit);
            }
            return removed;
        }

        if (edit.getType() == PlaylistEdit.TYPE_REMOVE) {
            boolean restored = undoRemove(edit, availability, allowDuplicates);
            if (!restored) {
                history.push(edit);
            }
            return restored;
        }

        history.push(edit);
        return false;
    }

    private boolean undoAdd(PlaylistEdit edit) {
        int pos = edit.getPosition();
        if (pos < 0 || pos >= songIds.size()) {
            return false;
        }
        String atPos = songIds.get(pos);
        if (!edit.getSongId().equals(atPos)) {
            return false;
        }
        return songIds.removeAt(pos) != null;
    }

    private boolean undoRemove(PlaylistEdit edit, SongAvailability availability, boolean allowDuplicates) {
        if (availability == null || !availability.exists(edit.getSongId())) {
            return false;
        }
        if (!allowDuplicates && songIds.contains(edit.getSongId())) {
            return false;
        }
        int pos = edit.getPosition();
        if (pos < 0 || pos > songIds.size()) {
            return false;
        }
        return songIds.addAt(pos, edit.getSongId());
    }
}
