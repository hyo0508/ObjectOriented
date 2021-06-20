import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reservation {
    private int roomNum;
    private boolean[] possible;

    public Reservation(String[] arr) {
        int i;
        roomNum = Integer.parseInt(arr[0]);
        possible = new boolean[14];
        for (i = 1; i <= 14; i++)
            possible[i - 1] = arr[i].equals("possible");
    }

    public void setRoomNum(int roomNum) { this.roomNum = roomNum; }
    public void setPossible(int index, boolean possible) { this.possible[index] = possible; }
    public int getRoomNum() { return roomNum; }
    public boolean getPossible(int index) { return possible[index]; }
}
