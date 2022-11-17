import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChatClientView extends JFrame {

	private static final long serialVersionUID = 1L;

	private ImageIcon friendListIcon = new ImageIcon("./friendList_icon.png");
	private ImageIcon talkListIcon = new ImageIcon("./talkList_icon.png");
	private ImageIcon dotsIcon = new ImageIcon("./dots_icon.png");

	private ImagePanel friendListBtn = new ImagePanel(friendListIcon.getImage());
	private ImagePanel talkListBtn = new ImagePanel(talkListIcon.getImage());
	private ImagePanel dotsBtn = new ImagePanel(dotsIcon.getImage());

	private JLabel listHeading = new JLabel("친구");
	private JScrollPane mainScrollPane = new JScrollPane();

	private JPanel friendListPanel = new JPanel();

	private JPanel contentPane;

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

		friendListBtn.setBorder(null);
		friendListBtn.setBounds(0, 0, 40, 40);
		contentPane.add(friendListBtn);

		talkListBtn.setBounds(0, 50, 40, 40);
		contentPane.add(talkListBtn);

		dotsBtn.setBounds(0, 100, 40, 40);
		contentPane.add(dotsBtn);

		mainScrollPane.setBounds(50, 0, 339, 514);
		contentPane.add(mainScrollPane);

		listHeading.setFont(new Font("Dialog", Font.BOLD, 24));
		mainScrollPane.setColumnHeaderView(listHeading);
		friendListPanel.setBorder(null);

		mainScrollPane.setViewportView(friendListPanel);
		friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));

		setVisible(true);

	}

	class ImagePanel extends JPanel {
		private Image img;

		public ImagePanel(Image img) {
			this.img = img;
		}

		@Override
		public void paintComponent(Graphics g) {
			Dimension d = getSize();
			g.drawImage(img, 0, 0, d.width, d.height, null);
		}
	}
}
