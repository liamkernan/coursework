public class SongQueue {
    private static class Node {
        private String songId;
        private Node next;

        private Node(String songId) {
            this.songId = songId;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public boolean enqueue(String songId) {
        if (songId == null) {
            return false;
        }
        Node node = new Node(songId);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
        return true;
    }

    public String dequeue() {
        if (head == null) {
            return null;
        }
        String songId = head.songId;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return songId;
    }

    public String peek() {
        if (head == null) {
            return null;
        }
        return head.songId;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void removeAll(String songId) {
        if (songId == null || head == null) {
            return;
        }
        while (head != null && songId.equals(head.songId)) {
            head = head.next;
            size--;
        }
        if (head == null) {
            tail = null;
            return;
        }
        Node current = head;
        while (current.next != null) {
            if (songId.equals(current.next.songId)) {
                current.next = current.next.next;
                size--;
            } else {
                current = current.next;
            }
        }
        tail = current;
    }

    public DynamicArray<String> toArray() {
        DynamicArray<String> out = new DynamicArray<String>();
        Node current = head;
        while (current != null) {
            out.add(current.songId);
            current = current.next;
        }
        return out;
    }
}
