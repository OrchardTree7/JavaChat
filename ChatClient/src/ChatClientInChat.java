import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatClientInChat extends JFrame {

	private int roomId;
	private String userId;

	private ObjectOutputStream oos;

	private JPanel contentPane;
	private JTextField txtInput;
	private JTextPane textArea;

	JPanel drawingPanel;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null;
	private Graphics gc2 = null;

	public ChatClientInChat(String userId, ObjectOutputStream oos, int roomId) {
		this.roomId = roomId;
		this.userId = userId;
		this.oos = oos;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 705, 611);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 193, 469);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(12, 485, 193, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);
		txtInput.addActionListener(new SendAction());

		JButton btnExit = new JButton("종 료");
		btnExit.setFont(new Font("굴림", Font.PLAIN, 14));
		btnExit.setBounds(12, 530, 69, 40);
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);

			}

		});
		contentPane.add(btnExit);

		drawingPanel = new JPanel();
		drawingPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		drawingPanel.setBackground(Color.WHITE);
		drawingPanel.setBounds(217, 12, 474, 565);
		contentPane.add(drawingPanel);

		MyMouseEvent mouse = new MyMouseEvent();
		drawingPanel.addMouseMotionListener(mouse);
		drawingPanel.addMouseListener(mouse);
		MyMouseWheelEvent wheel = new MyMouseWheelEvent();
		drawingPanel.addMouseWheelListener(wheel);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				gc = drawingPanel.getGraphics();
				panelImage = createImage(drawingPanel.getWidth(), drawingPanel.getHeight());
				gc2 = panelImage.getGraphics();
				gc2.setColor(drawingPanel.getBackground());
				gc2.fillRect(0, 0, drawingPanel.getWidth(), drawingPanel.getHeight());
				gc2.setColor(Color.BLACK);
				gc2.drawRect(0, 0, drawingPanel.getWidth() - 1, drawingPanel.getHeight() - 1);
			}
		});

		setVisible(true);

	}

	public class SendAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AppendTextR(txtInput.getText());
			SendChat("200", txtInput.getText());
			txtInput.setText("");
		}

	}

	public void AppendText(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();

		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", left);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);

	}

	public void AppendTextR(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);
		doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", right);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);

	}

	public void AppendImage(ImageIcon ori_icon) {

		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else {
			textArea.insertIcon(ori_icon);
			new_img = ori_img;
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);

	}

	public void SendChat(String code, String msg) {
		try {
			ChatMsg obcm = new ChatMsg(userId, code, msg, getRoomId());
			oos.writeObject(obcm);
		} catch (IOException e1) {

			e1.printStackTrace();
			System.exit(0);
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
		}
	}

	// Mouse Event 수신 처리
	public void DoMouseEvent(ChatMsg cm) {
		Color c;
		if (cm.userId.matches(userId)) // 본인 것은 이미 Local 로 그렸다.
			return;
		c = new Color(255, 0, 0); // 다른 사람 것은 Red
		gc2.setColor(c);
		gc2.fillOval(cm.mouse_e.getX() - pen_size / 2, cm.mouse_e.getY() - cm.pen_size / 2, cm.pen_size, cm.pen_size);
		gc.drawImage(panelImage, 0, 0, drawingPanel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(userId, "500", "MOUSE", roomId);
		cm.mouse_e = e;
		cm.pen_size = pen_size;
		SendObject(cm);
	}

	class MyMouseWheelEvent implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if (e.getWheelRotation() < 0) { // 위로 올리는 경우 pen_size 증가
				if (pen_size < 20)
					pen_size++;
			} else {
				if (pen_size > 2)
					pen_size--;
			}
		}
	}

	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			Color c = new Color(0, 0, 255);
			gc2.setColor(c);
			gc2.fillOval(e.getX() - pen_size / 2, e.getY() - pen_size / 2, pen_size, pen_size);
			// panelImnage는 paint()에서 이용한다.
			gc.drawImage(panelImage, 0, 0, drawingPanel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Color c = new Color(0, 0, 255);
			gc2.setColor(c);
			gc2.fillOval(e.getX() - pen_size / 2, e.getY() - pen_size / 2, pen_size, pen_size);
			gc.drawImage(panelImage, 0, 0, drawingPanel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}
	}

	public int getRoomId() {
		return roomId;
	}
}
