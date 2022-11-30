
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code;
	public String userId;
	public String data;
	public int roomId; // 0 = Server 1,2,3... = roomId
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size;

	public ChatMsg(String userId, String code, String msg, int roomId) {
		this.code = code;
		this.userId = userId;
		this.data = msg;
		this.roomId = roomId;
	}
}