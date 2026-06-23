import java.util.*;

public class PlaylistDriver {

    static ArrayList<Song> playlist = new ArrayList<>();

    public static void addSong(Song song){
        playlist.add(song);
    }

    public static void removeSong(Song song){
        song.setAdded(false);
        playlist.remove(song);
    }

    public static void main (String[] args){
        Song track1 = new Song("hazard duty pay", "JPEGMAFIA", "Hip Hop", 2.39, true);
        Song track2 = new Song("stick", "jim legaxcy", "indie", 3.12, true);
        Song track3 = new Song("sympathy is a knife", "charli xcx", "pop", 2.12, true);
        Song track4 = new Song("hey driver", "zach bryan", "country", 4.39, true);
        Song track5 = new Song("jungle", "drake", "rnb", 5.39, true);

        PlaylistDriver.addSong(track1);
        PlaylistDriver.addSong(track2);
        PlaylistDriver.removeSong(track2);

        System.out.print(track2);
    }

}
