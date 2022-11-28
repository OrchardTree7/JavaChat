import java.util.Vector;

public class ChatRoom {

	public int roomId;
	private Vector UserVec = new Vector();
	private static final int BUF_LEN = 128;

	public ChatRoom(Vector userVec) {
		UserVec = userVec;
	}

	public void appendText(String msg) {

	}
}
