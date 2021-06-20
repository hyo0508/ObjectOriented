public class Room implements Comparable<Room> {
    private int roomNum;
    private int seat;
    private int computer;
    private String mic;
    private String board;
    private String screen;

    public Room(String[] arr) {
        this.roomNum = Integer.parseInt(arr[0]);
        this.seat = Integer.parseInt(arr[1]);
        this.computer = Integer.parseInt(arr[2]);
        this.mic = arr[3];
        this.board = arr[4];
        this.screen = arr[5];
    }

    public int getRoomNum() {
        return roomNum;
    }

    public int compareTo(Room otherRoom) {
        return Integer.compare(roomNum, otherRoom.getRoomNum());
    }
}
