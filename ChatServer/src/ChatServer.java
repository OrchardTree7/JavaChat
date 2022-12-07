import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

public class ChatServer extends Thread {

	private String[] words = new String[] { "아마겟돈", "뱅보드 차트", "개구리", "담배", "단소", "가극", "만리장성", "어린왕자", "바코드", "만남", "새",
			"하늘", "나무", "말뚝", "목소리", "공장", "노래", "계곡", "폭포", "행성", "나비" };

	private static ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector<UserService> userVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private Vector<ChatRoom> roomVec = new Vector<ChatRoom>();

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
			WriteAll("100", userlist);
			WriteOne("100", roomlist);
		}

		public void Logout() {
			userVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll("400", userId);
		}

		public void EstablishRoom(String room_id, String master, String round) {
			ChatRoom room = new ChatRoom(room_id, master, Integer.parseInt(round));
			roomVec.add(room);
			WriteAll("300", room_id);
		}

		public ChatRoom FindRoom(String room_id) {
			for (int i = 0; i < roomVec.size(); i++) {
				if (roomVec.get(i).getRoomId().equals(room_id)) {
					return roomVec.get(i);
				}
			}
			return null;
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

		public void WriteOne(String code, String msg) {
			try {
				ControlMsg obcm = new ControlMsg("SERVER", code, msg);
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

		public void WriteAll(String code, String str) {
			for (int i = 0; i < userVec.size(); i++) {
				UserService user = userVec.elementAt(i);
				user.WriteOne(code, str);
			}
		}

		public void WriteChat(String user_id, String code, String msg, String room_id) {
			try {
				ChatMsg obcm = new ChatMsg(user_id, code, msg, room_id);
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

		@Override
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String[] msg = null;
					ChatMsg cm = null;
					ControlMsg conm = null;
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
					if (obcm instanceof ControlMsg) {
						conm = (ControlMsg) obcm;
						if (conm.code.matches("100")) {
							Login(conm.userId);
						} else if (conm.code.matches("300")) {
							// 방 개설
							msg = conm.data.split(",");
							EstablishRoom(msg[0], conm.userId, msg[1]);
						} else if (conm.code.matches("301")) {
							// 방 삭제

						} else if (conm.code.matches("302")) {

						} else if (conm.code.matches("303")) {
							// 방 입장
							ChatRoom room = FindRoom(conm.data);
							room.handleEnter(conm.userId);
						} else if (conm.code.matches("304")) {
							// 방 퇴장
							ChatRoom room = FindRoom(conm.data);
							// 방장 퇴장시 처리
							if (room.getMaster().equals(conm.userId)) {
							}
							room.handleExit(conm.userId);
							if (room.getUserVec().size() == 0) {
								WriteAll("304", room.getRoomId());
								roomVec.remove(room);
							}
						} else if (conm.code.matches("400")) {
							Logout();
						}
					} else if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						if (cm.code.matches("200")) {
							ChatRoom room = FindRoom(cm.roomId);
							room.handleChat(cm.userId, cm.data);
						} else if (cm.code.matches("203")) {
							Random r = new Random();
							String word = words[r.nextInt(words.length)];
							ChatRoom room = FindRoom(cm.roomId);
							String uid = room.getRoundUser();
							room.handleStart(word + "," + uid);
						} else if (cm.code.matches("204")) {
							ChatRoom room = FindRoom(cm.roomId);
							room.roundOver();
							if (room.isGameOver()) {
								String uid = room.getRoundUser();
								room.handleOver(cm.userId, uid);
							} else {
								Random r = new Random();
								String word = words[r.nextInt(words.length)];
								String uid = room.getRoundUser();
								room.handleEnd(cm.userId, word + "," + uid + "," + room.round);
							}
						} else {
							ChatRoom room = FindRoom(cm.roomId);
							room.handleEtc(cm);
						}
					} else
						continue;
				} catch (IOException e) {

				}
			}
		}

	}

	public class ChatRoom {

		private String roomId;
		private String master;
		private int turn = 5;
		private int round = 0;
		private Vector<UserService> roomUserVec = new Vector<UserService>();

		public ChatRoom(String room_id, String master, int turn) {
			setRoomId(room_id);
			setMaster(master);
			setTurn(turn);
		}

		public void handleChat(String user_id, String msg) {
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteChat(user_id, "200", user_id + ":" + msg, getRoomId());
			}
		}

		public void handleEtc(ChatMsg cm) {
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteOneObject(cm);
			}
		}

		public void handleExit(String user_id) {
			for (int i = 0; i < roomUserVec.size(); i++) {
				if (roomUserVec.get(i).userId.equals(user_id)) {
					roomUserVec.remove(i);
					break;
				}
			}
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteOne("304", user_id);
			}
		}

		public void handleEnter(String user_id) {
			for (int i = 0; i < userVec.size(); i++) {
				if (userVec.get(i).userId.equals(user_id)) {
					roomUserVec.add(userVec.get(i));
				}
			}
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteOne("303", user_id);
			}
		}

		public void handleStart(String msg) {
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteChat(master, "203", msg, roomId);
			}
		}

		public void handleEnd(String user_id, String msg) {
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteChat(user_id, "204", msg, roomId);
			}
		}

		public void handleOver(String user_id, String roundUser) {
			for (int i = 0; i < roomUserVec.size(); i++) {
				roomUserVec.get(i).WriteChat(user_id, "205", roundUser, roomId);
			}
		}

		public void roundOver() {
			if (round < turn)
				round++;
		}

		public boolean isGameOver() {
			if (round == turn) {
				round = 0;
				return true;
			}
			return false;
		}

		public String getRoundUser() {
			return roomUserVec.get(round % roomUserVec.size()).userId;
		}

		public Vector<UserService> getUserVec() {
			return roomUserVec;
		}

		public void setUserVec(Vector<UserService> roomUserVec) {
			this.roomUserVec = roomUserVec;
		}

		public String getMaster() {
			return master;
		}

		public void setMaster(String master) {
			this.master = master;
		}

		public String getRoomId() {
			return roomId;
		}

		public void setRoomId(String roomId) {
			this.roomId = roomId;
		}

		public int getTurn() {
			return turn;
		}

		public void setTurn(int turn) {
			this.turn = turn;
		}

	}
}
