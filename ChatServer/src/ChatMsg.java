
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String roomId;
	public String code;
	public String userId;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size;
	public Color pen_color = Color.BLACK;

	public ChatMsg(String userId, String code, String msg, String roomId) {
		this.code = code;
		this.userId = userId;
		this.data = msg;
		this.roomId = roomId;
	}
}