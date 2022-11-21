import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChatClientView extends JFrame {

	private static final long serialVersionUID = 1L;

	private ImageIcon friendListIcon = new ImageIcon("./friendList_icon.png");
	private ImageIcon roomListIcon = new ImageIcon("./roomList_icon.png");
	private ImageIcon dotsIcon = new ImageIcon("./dots_icon.png");
	private ImageIcon addRoomIcon = new ImageIcon("./addRoom_icon.png");
	private ImageIcon addFriendIcon = new ImageIcon("./addFriend_icon.png");
	private ImageIcon basicProfileIcon = new ImageIcon("./basicProfile_icon.png");
	private ImageIcon basicRoomIcon = new ImageIcon("./basicRoom_icon.png");

	private JButton friendListBtn = new JButton(friendListIcon);
	private JButton roomListBtn = new JButton(roomListIcon);
	private JButton dotsBtn = new JButton(dotsIcon);
	private JButton addRoomBtn = new JButton(addRoomIcon);
	private JButton addFriendBtn = new JButton(addFriendIcon);

	private JScrollPane mainScrollPane = new JScrollPane();

	private JPanel navigator = new JPanel();
	private JPanel friendPanel = new JPanel();
	private JPanel roomPanel = new JPanel();

	private JPanel contentPane;
	private JLabel friendListLabel = new JLabel("친구");
	private JLabel roomListLabel = new JLabel("방 목록");
	private JPanel friendListHeading = new JPanel();
	private JPanel roomListHeading = new JPanel();
	private JPanel friendMainPanel = new JPanel();
	private JPanel roomMainPanel = new JPanel();

	/**
	 * Create the application.
	 */
	public ChatClientView(String username, String ip_addr, String port_no) {
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
		addFriendBtn.setBorderPainted(false);
		addFriendBtn.setContentAreaFilled(false);
		addFriendBtn.setFocusPainted(false);
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
		addFriendBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton item = new JButton(basicProfileIcon);
				item.setText("sample");
				item.setBorderPainted(false);
				item.setContentAreaFilled(false);
				item.setFocusPainted(false);
				friendMainPanel.add(item);
				friendMainPanel.revalidate();
			}

		});
		addRoomBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton item = new JButton(basicRoomIcon);
				item.setText("sample");
				item.setBorderPainted(false);
				item.setContentAreaFilled(false);
				item.setFocusPainted(false);
				roomMainPanel.add(item);
				roomMainPanel.revalidate();
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
		friendListHeading.add(addFriendBtn);

		friendPanel.add(friendListHeading, BorderLayout.NORTH);
		roomPanel.add(roomListHeading, BorderLayout.NORTH);

		setVisible(true);

	}
}
