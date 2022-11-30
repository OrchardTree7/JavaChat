import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChatRoomEstablishView extends JFrame {

	private JPanel contentPane;

	private JScrollPane mainScrollPane;
	private JPanel friendPanel;
	private JPanel friendListHeading;
	private JLabel friendListLabel;
	private JPanel friendMainPanel;
	private JButton btnEstablish;

	private ImageIcon basicProfileIcon = new ImageIcon("./basicProfile_icon.png");

	private List<String> userList = new ArrayList<String>();
	private List<String> roomMembers = new ArrayList<String>();

	public ChatRoomEstablishView(List<String> userList) {
		this.userList = userList;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 300, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 221, 218));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		mainScrollPane = new JScrollPane();
		mainScrollPane.setBounds(0, 0, 298, 473);
		contentPane.add(mainScrollPane);

		friendPanel = new JPanel();
		mainScrollPane.setViewportView(friendPanel);
		friendPanel.setLayout(new BorderLayout(0, 0));

		friendListHeading = new JPanel();
		FlowLayout flowLayout = (FlowLayout) friendListHeading.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		friendPanel.add(friendListHeading, BorderLayout.NORTH);

		friendListLabel = new JLabel("친구");
		friendListLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		friendListHeading.add(friendListLabel);

		friendMainPanel = new JPanel();
		friendPanel.add(friendMainPanel, BorderLayout.CENTER);
		friendMainPanel.setLayout(new BoxLayout(friendMainPanel, BoxLayout.Y_AXIS));

		for (int i = 0; i < userList.size(); i++) {
			JButton item = new JButton(basicProfileIcon);
			item.setText(userList.get(i));
			item.setBorderPainted(false);
			item.setContentAreaFilled(false);
			item.setFocusPainted(false);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String userId = item.getText().split(" ")[0];
					if (!roomMembers.contains(userId)) {
						roomMembers.add(userId);
						item.setText(userId + " (선택됨)");
					} else {
						roomMembers.remove(roomMembers.indexOf(userId));
						item.setText(userId);
					}

				}

			});
			friendMainPanel.add(item);
		}

		btnEstablish = new JButton("방 만들기");
		btnEstablish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}

		});
		friendMainPanel.add(btnEstablish);

		setVisible(true);
	}

	public List<String> getRoomMembers() {
		return roomMembers;
	}

}
