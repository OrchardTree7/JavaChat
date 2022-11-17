//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.util.Vector;

public class ChatRoom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serverName;
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

}