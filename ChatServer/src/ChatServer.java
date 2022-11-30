import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer extends Thread {

	private int room_id = 1;
	private static ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector<UserService> userVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private Vector<ChatRoom> roomVec = new Vector<ChatRoom>();
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
		System.out.println("서버 시작");
		while (true) { // 사용자 접속을 계속해서 받기 위해 while문
			try {
				client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
				// User 당 하나씩 Thread 생성
				UserService new_user = new UserService(client_socket);
				userVec.add(new_user); // 새로운 참가자 배열에 추가
				new_user.start(); // 만든 객체의 스레드 실행
			} catch (IOException e) {
				e.printStackTrace();
				// System.exit(0);
			}
		}
	}

	public class UserService extends Thread {

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		public String userId = "";

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

		public void Login(String user_id) {
			userId = user_id;

			String userlist = MakeUserList();
			String roomlist = MakeRoomList();
			WriteAll("100", userlist, 0);
			WriteAll("100", roomlist, 0);
		}

		public void Logout() {
			userVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll("404", userId, 0);
		}

		public void EstablishRoom(String members) {
			Vector<UserService> uv = new Vector<UserService>();
			String[] memberList = members.split(" ");
			for (int i = 1; i < memberList.length; i++) {
				for (int j = 0; j < userVec.size(); j++) {
					if (userVec.get(j).userId.equals(memberList[i])) {
						uv.add(userVec.get(j));
						userVec.get(j).WriteOne("301", Integer.toString(room_id) + " " + members, 0);
					}
				}
			}
			ChatRoom room = new ChatRoom(room_id, uv);
			roomVec.add(room);
			WriteOne("300", Integer.toString(room_id) + " " + members, 0);
		}

		public void HandleChat(String msg, int room_id) {
			for (int i = 0; i < roomVec.size(); i++) {
				if (roomVec.get(i).getRoomId() == room_id) {

				}
			}
		}

		public void WriteOneObject(Object ob) {
			try {
				oos.writeObject(ob);
			} catch (IOException e) {
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Logout();
			}
		}

		public void WriteAllObject(Object ob) {
			for (int i = 0; i < userVec.size(); i++) {
				UserService user = userVec.elementAt(i);
				user.WriteOneObject(ob);
			}
		}

		public void WriteOne(String code, String msg, int room_id) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", code, msg, room_id);
				oos.writeObject(obcm);
			} catch (IOException e) {
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Logout();
			}
		}

		public void WriteAll(String code, String str, int room_id) {
			for (int i = 0; i < userVec.size(); i++) {
				UserService user = userVec.elementAt(i);
				user.WriteOne(code, str, room_id);
			}
		}

		public String MakeUserList() {
			StringBuilder temp = new StringBuilder();
			for (int i = 0; i < userVec.size(); i++) {
				UserService user = userVec.elementAt(i);
				temp.append(user.userId + " ");
			}
			temp.append("userlist");
			return temp.toString();
		}

		public String MakeRoomList() {
			StringBuilder temp = new StringBuilder();
			for (int i = 0; i < roomVec.size(); i++) {
				ChatRoom room = roomVec.elementAt(i);
				temp.append(room.getRoomId() + " ");
			}
			temp.append("roomlist");
			return temp.toString();
		}

		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
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
						Login(cm.userId);
						System.out.println(cm.userId + " " + cm.data);
					} else if (cm.code.matches("200")) {
						System.out.println(cm.userId + " " + cm.data);
						HandleChat(cm.data, cm.roomId);
					} else if (cm.code.matches("300")) {
						EstablishRoom(cm.data);
					} else if (cm.code.matches("404")) {
						Logout();
					}
				} catch (IOException e) {

				}
			}
		}

	}
}
