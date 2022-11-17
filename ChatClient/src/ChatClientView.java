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

		ImagePanel panel = new ImagePanel(friendListIcon.getImage());
		panel.setBorder(null);
		panel.setBounds(0, 0, 40, 40);
		contentPane.add(panel);

		ImagePanel panel_1 = new ImagePanel(talkListIcon.getImage());
		panel_1.setBounds(0, 50, 40, 40);
		contentPane.add(panel_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 0, 349, 514);
		contentPane.add(scrollPane);

		JLabel lblNewLabel = new JLabel("친구");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		scrollPane.setColumnHeaderView(lblNewLabel);

		JPanel panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

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
