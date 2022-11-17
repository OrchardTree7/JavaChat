import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ChatClientMain extends JFrame {

	private String localhost = "127.0.0.1";
	private String port = "30000";

	private static final long serialVersionUID = 1L;

	private ImageIcon basicProfileIcon = new ImageIcon("./basic_profile.jpeg");

	private JPanel contentPane;
	private JPanel profilePanel;

	private HintTextField userIDTextField;
	private HintTextField passwordTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ChatClientMain frame = new ChatClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatClientMain() {
		setBackground(new Color(247, 229, 0));
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(247, 229, 0));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		userIDTextField = new HintTextField("아이디 입력");
		userIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		userIDTextField.setBounds(78, 252, 244, 39);
		contentPane.add(userIDTextField);
		userIDTextField.setColumns(10);

		JButton btnConnect = new JButton("확인");
		btnConnect.setForeground(new Color(154, 153, 150));
		btnConnect.setBounds(78, 352, 244, 39);
		contentPane.add(btnConnect);

		passwordTextField = new HintTextField("비밀번호 입력");
		passwordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTextField.setBounds(78, 302, 244, 39);
		contentPane.add(passwordTextField);
		passwordTextField.setColumns(10);

		profilePanel = new ImagePanel(basicProfileIcon.getImage());
		profilePanel.setBounds(150, 100, 100, 100);
		contentPane.add(profilePanel);

		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
	}

	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String user_id = userIDTextField.getText().trim();
			String ip_addr = localhost;
			String port_no = port;
			ChatClientView view = new ChatClientView(user_id, ip_addr, port_no);
			setVisible(false);
		}
	}

	class HintTextField extends JTextField {

		Font gainFont = new Font("Tahoma", Font.PLAIN, 11);
		Font lostFont = new Font("Tahoma", Font.ITALIC, 11);

		public HintTextField(final String hint) {

			setText(hint);
			setFont(lostFont);
			setForeground(Color.GRAY);

			this.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (getText().equals(hint)) {
						setText("");
						setFont(gainFont);
					} else {
						setText(getText());
						setFont(gainFont);
					}
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (getText().equals(hint) || getText().length() == 0) {
						setText(hint);
						setFont(lostFont);
						setForeground(Color.GRAY);
					} else {
						setText(getText());
						setFont(gainFont);
						setForeground(Color.BLACK);
					}
				}
			});
		}
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
