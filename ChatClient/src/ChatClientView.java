import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChatClientView extends JFrame {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String ipAddr;
	private int port;

	private Socket socket;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private List<String> userList = new ArrayList<String>();
	private List<String> roomList = new ArrayList<String>();
	private ChatClientInChat chatView;

	private ImageIcon friendListIcon = new ImageIcon("./friendList_icon.png");
	private ImageIcon roomListIcon = new ImageIcon("./roomList_icon.png");
	private ImageIcon dotsIcon = new ImageIcon("./dots_icon.png");
	private ImageIcon addRoomIcon = new ImageIcon("./addRoom_icon.png");
	private ImageIcon basicProfileIcon = new ImageIcon("./basicProfile_icon.png");
	private ImageIcon basicRoomIcon = new ImageIcon("./basicRoom_icon.png");

	private JButton friendListBtn = new JButton(friendListIcon);
	private JButton roomListBtn = new JButton(roomListIcon);
	private JButton dotsBtn = new JButton(dotsIcon);
	private JButton addRoomBtn = new JButton(addRoomIcon);

	private JScrollPane mainScrollPane = new JScrollPane();

	private JPanel navigator = new JPanel();
	private JPanel friendPanel = new JPanel();
	private JPanel roomPanel = new JPanel();

	private JPanel contentPane;
	private JLabel friendListLabel = new JLabel("유저 목록");
	private JLabel roomListLabel = new JLabel("방 목록");
	private JPanel friendListHeading = new JPanel();
	private JPanel roomListHeading = new JPanel();
	private JPanel friendMainPanel = new JPanel();
	private JPanel roomMainPanel = new JPanel();

	/**
	 * Create the application.
	 */
	public ChatClientView(String user_id, String ip_addr, String port_no) {
		setTitle("캐치마인드");
		userId = user_id;
		ipAddr = ip_addr;
		port = Integer.parseInt(port_no);

		try {
			socket = new Socket(ipAddr, port);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ControlMsg obcm = new ControlMsg(userId, "100", "Login");
			SendObject(obcm);
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListenNetwork net = new ListenNetwork();
		net.start();

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 391, 541);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 221, 218));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		mainScrollPane.setBounds(0, 0, 389, 514);
		contentPane.add(mainScrollPane);

		mainScrollPane.setRowHeaderView(navigator);
		navigator.setLayout(new BoxLayout(navigator, BoxLayout.Y_AXIS));

		friendListBtn.setBorderPainted(false);
		friendListBtn.setContentAreaFilled(false);
		friendListBtn.setFocusPainted(false);
		roomListBtn.setBorderPainted(false);
		roomListBtn.setContentAreaFilled(false);
		roomListBtn.setFocusPainted(false);
		addRoomBtn.setBorderPainted(false);
		addRoomBtn.setContentAreaFilled(false);
		addRoomBtn.setFocusPainted(false);

		navigator.add(friendListBtn);
		navigator.add(roomListBtn);

		friendListBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainScrollPane.setViewportView(friendPanel);
			}
		});
		roomListBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainScrollPane.setViewportView(roomPanel);
			}
		});

		addRoomBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChatRoomEstablishView view = new ChatRoomEstablishView();
				view.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentHidden(ComponentEvent e) {
						String roomId = view.textField.getText();
						String turn = view.getRound();

						SendMessage("300", roomId + "," + turn);
						ChatClientInChat view = new ChatClientInChat(userId, oos, roomId, true);
						SendMessage("303", roomId);

						view.addWindowListener(new WindowAdapter() {

							@Override
							public void windowClosing(WindowEvent e) {
								SendMessage("304", roomId);
								setVisible(true);
							}

						});
						view.addComponentListener(new ComponentAdapter() {

							@Override
							public void componentHidden(ComponentEvent e) {
								SendMessage("304", roomId);
								setVisible(true);
							}

						});
						view.setMaster(true);
						view.setCurrUser(userId);
						chatView = view;
						setVisible(false);
					}
				});
			}

		});

		mainScrollPane.setViewportView(friendPanel);

		roomPanel.setLayout(new BorderLayout(0, 0));
		roomPanel.add(roomMainPanel, BorderLayout.CENTER);
		roomMainPanel.setLayout(new BoxLayout(roomMainPanel, BoxLayout.Y_AXIS));

		friendPanel.setLayout(new BorderLayout(0, 0));
		friendPanel.add(friendMainPanel, BorderLayout.CENTER);
		friendMainPanel.setLayout(new BoxLayout(friendMainPanel, BoxLayout.Y_AXIS));

		roomListLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		friendListLabel.setFont(new Font("Dialog", Font.BOLD, 20));

		roomListHeading.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		roomListHeading.add(roomListLabel);
		roomListHeading.add(addRoomBtn);
		friendListHeading.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		friendListHeading.add(friendListLabel);

		friendPanel.add(friendListHeading, BorderLayout.NORTH);
		roomPanel.add(roomListHeading, BorderLayout.NORTH);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ControlMsg obcm = new ControlMsg(userId, "400", "Logout");
				SendObject(obcm);
			}
		});

		setVisible(true);

	}

	class ListenNetwork extends Thread {
		@Override
		public void run() {
			while (true) {
				try {

					// obcm 그대로 사용시 cm.data 내용 출력됨
					Object obcm = null;
					ChatMsg cm;
					ControlMsg conm;
					String[] msg;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ControlMsg) {
						conm = (ControlMsg) obcm;
						switch (conm.code) {
						case "100":

							msg = conm.data.split(" ");

							if (msg[msg.length - 1].equals("userlist")) {
								for (int i = 0; i < msg.length - 1; i++) {
									if (!userList.contains(msg[i]) && !msg[i].equals(userId)) {
										userList.add(msg[i]);
									}
								}
								setFriendMainPanel();
							} else if (msg[msg.length - 1].equals("roomlist")) {
								for (int i = 0; i < msg.length - 1; i++) {
									if (!roomList.contains(msg[i])) {
										roomList.add(msg[i]);
									}
								}
								setRoomMainPanel();
							}
							break;
						case "300":
							roomList.add(conm.data);
							setRoomMainPanel();
							break;
						case "301":
							break;
						case "302":
							break;
						case "303":
							chatView.AppendNotice(conm.data + "님이 입장하셨습니다.");
							break;
						case "304":
							msg = conm.data.split(",");
							for (int i = 0; i < roomList.size(); i++) {
								if (roomList.get(i).equals(msg[0])) {
									roomList.remove(i);
									break;
								}
							}
							if (msg.length == 1) {
								chatView.AppendNotice(conm.data + "님이 퇴장하셨습니다.");
							}
							setRoomMainPanel();
							break;
						case "400":
							userList.remove(userList.indexOf(conm.data));
							setFriendMainPanel();
							break;
						}
					} else if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						switch (cm.code) {
						case "200":
							if (!userId.equals(cm.userId)) {
								chatView.AppendText(cm.data);
							}
							break;
						case "201":
							if (!userId.equals(cm.userId)) {
								chatView.DoMouseEvent(cm);
							}
							break;
						case "202":
							if (userId.equals(cm.userId)) {
								chatView.AppendImage(cm.img);
							}
							break;
						case "203":
							msg = cm.data.split(",");
							if (msg[1].equals(userId)) {
								chatView.setTurn(true);
							} else {
								chatView.setTurn(false);
							}
							chatView.AppendNotice("\n게임시작");
							chatView.AppendNotice(msg[1] + "님의 차례입니다.\n");
							chatView.clearDrawingPanel();
							chatView.setWord(msg[0]);
							chatView.setDrawingUser(msg[1]);
							break;
						case "204":
							// 정답자 처리
							if (userId.equals(cm.userId)) {
								chatView.setScore(chatView.getScore() + 10);
							}
							msg = cm.data.split(",");
							if (msg[1].equals(userId)) {
								chatView.setTurn(true);
							} else {
								chatView.setTurn(false);
							}
							chatView.AppendNotice("\n" + msg[2] + " 라운드 종료");
							chatView.AppendNotice("정답자:" + cm.userId + ", 제시어:" + chatView.getWord());
							chatView.AppendNotice(msg[1] + "님의 차례입니다.\n");
							chatView.clearDrawingPanel();
							chatView.setWord(msg[0]);
							chatView.setDrawingUser(msg[1]);
							break;
						case "205":
							if (userId.equals(cm.userId)) {
								chatView.setScore(chatView.getScore() + 10);
							}
							if (chatView.isMaster()) {
								chatView.enableStartBtn(true);
							}
							if (cm.data.equals(userId)) {
								chatView.setTurn(true);
							} else {
								chatView.setTurn(false);
							}
							chatView.AppendNotice("\n마지막 라운드 종료");
							chatView.AppendNotice("정답자:" + cm.userId + ", 제시어:" + chatView.getWord() + "\n");
							chatView.clearDrawingPanel();
							chatView.setWord("");
							chatView.setDrawingUser(cm.data);
							break;
						}
					} else
						continue;

				} catch (IOException e) {
					try {
						e.printStackTrace();
						System.out.println(userId + " error 1");
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}

	}

	// Server에게 network으로 전송
	public void SendMessage(String code, String msg) {
		try {
			ControlMsg obcm = new ControlMsg(userId, code, msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			try {

				e.printStackTrace();
				System.out.println(userId + " " + code + " " + msg + "error 2");
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {

				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}

	public void setFriendMainPanel() {
		friendMainPanel.removeAll();
		for (int i = 0; i < userList.size(); i++) {
			JButton item = new JButton(basicProfileIcon);
			item.setText(userList.get(i));
			item.setBorderPainted(false);
			item.setContentAreaFilled(false);
			item.setFocusPainted(false);
			friendMainPanel.add(item);
		}
		friendMainPanel.revalidate();
		friendMainPanel.repaint();
	}

	public void setRoomMainPanel() {
		roomMainPanel.removeAll();
		for (int i = 0; i < roomList.size(); i++) {
			JButton item = new JButton(basicRoomIcon);
			item.setText(roomList.get(i));
			item.setBorderPainted(false);
			item.setContentAreaFilled(false);
			item.setFocusPainted(false);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String roomId = item.getText();
					ChatClientInChat view = new ChatClientInChat(userId, oos, roomId);
					SendMessage("303", roomId);

					view.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosing(WindowEvent e) {
							SendMessage("304", roomId);
							setVisible(true);
						}

					});
					view.addComponentListener(new ComponentAdapter() {

						@Override
						public void componentHidden(ComponentEvent e) {
							SendMessage("304", roomId);
							setVisible(true);
						}

					});
					chatView = view;
					view.setCurrUser(userId);
					setVisible(false);
				}

			});
			roomMainPanel.add(item);
		}
		roomMainPanel.revalidate();
		roomMainPanel.repaint();
	}
}
