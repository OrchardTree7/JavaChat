import java.util.Vector;

public class ChatRoom {

	private int roomId;
	private Vector userVec = new Vector();
	private static final int BUF_LEN = 128;

	public ChatRoom(int room_id, Vector userVec) {
		roomId = room_id;
		this.setUserVec(userVec);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Vector getUserVec() {
		return userVec;
	}

	public void setUserVec(Vector userVec) {
		this.userVec = userVec;
	}
}
