
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.ImageIcon;

class ControlMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code;
	public String userId;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size;

	public ControlMsg(String userId, String code, String msg) {
		this.code = code;
		this.userId = userId;
		this.data = msg;
	}
}