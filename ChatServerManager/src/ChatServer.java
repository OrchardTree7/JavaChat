import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer extends Thread {

	private static ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private Vector<ChatRoom> ServerVec = new Vector<ChatRoom>();
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			socket = new ServerSocket(30000);
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ChatServer server = new ChatServer();
		server.start();
	}

	@Override
	public void run() {
		while (true) { // 사용자 접속을 계속해서 받기 위해 while문
			try {
				client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
				// User 당 하나씩 Thread 생성
				UserService new_user = new UserService(client_socket);
				UserVec.add(new_user); // 새로운 참가자 배열에 추가
				new_user.start(); // 만든 객체의 스레드 실행
			} catch (IOException e) {
				e.printStackTrace();
				// System.exit(0);
			}
		}
	}

	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		public String UserName = "";
		public String UserStatus;

		public UserService(Socket client_socket) {
			this.client_socket = client_socket;

			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void Login() {

		}

		public void Logout() {
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
		}

		@Override
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
					} else
						continue;
					if (cm.code.matches("100")) {

					}
				} catch (IOException e) {

				}
			}
		}

	}
}
