import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatClientInChat extends JFrame {

	private boolean isMaster = false;
	private boolean isTurn = false;

	private String roomId;
	private String userId;

	private String word;

	private int score = 0;

	private ObjectOutputStream oos;

	private ImageIcon drawLineIcon = new ImageIcon("./drawLine_icon.png");
	private ImageIcon drawStraightLineIcon = new ImageIcon("./drawStraightLine_icon.png");
	private ImageIcon drawSquareIcon = new ImageIcon("./drawSquare_icon.png");
	private ImageIcon drawFilledSquareIcon = new ImageIcon("./drawFilledSquare_icon.png");
	private ImageIcon drawOvalIcon = new ImageIcon("./drawOval_icon.png");
	private ImageIcon drawFilledOvalIcon = new ImageIcon("./drawFilledOval_icon.png");

	private JButton btnDrawLine = new JButton(drawLineIcon);
	private JButton btnDrawSquare = new JButton(drawSquareIcon);
	private JButton btnDrawFilledSquare = new JButton(drawFilledSquareIcon);
	private JButton btnDrawOval = new JButton(drawOvalIcon);
	private JButton btnDrawFilledOval = new JButton(drawFilledOvalIcon);
	private JButton btnDrawStraightLine = new JButton(drawStraightLineIcon);

	private JButton btnClear;
	private JButton btnColorRed;
	private JButton btnColorBlue;
	private JButton btnColorYellow;
	private JButton btnColorGreen;
	private JButton btnColorBlack;

	private JButton btnDraw;

	private JPanel contentPane;
	private JTextField txtInput;
	private JTextPane textArea;
	private JButton btnGameStart;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;

	private JPanel drawingPanel;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null;
	private Graphics gc2 = null;
	private Image tmpImage = null;
	private Graphics gc3 = null;
	private JLabel lblWordHeading;
	private JLabel lblWord;
	private JLabel lblDrawingUser;

	private int ox, oy, x1, y1, x2, y2;
	private Color penColor = Color.black;
	private JLabel lblScore;
	private JLabel lblUser;

	/**
	 * @wbp.parser.constructor
	 */
	public ChatClientInChat(String user_id, ObjectOutputStream oos, String room_id) {
		setTitle("캐치마인드");
		this.roomId = room_id;
		this.userId = user_id;
		this.oos = oos;
		initialize();
	}

	public ChatClientInChat(String user_id, ObjectOutputStream oos, String room_id, boolean is_master) {
		this.roomId = room_id;
		this.userId = user_id;
		this.isMaster = is_master;
		this.oos = oos;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 611);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 70, 193, 411);
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
		btnExit.setFont(new Font("Dialog", Font.BOLD, 12));
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
		drawingPanel.setBounds(217, 70, 474, 500);
		contentPane.add(drawingPanel);

		if (isMaster()) {
			btnGameStart = new JButton("게임시작");
			btnGameStart.setBounds(85, 530, 120, 40);
			contentPane.add(btnGameStart);
			btnGameStart.addActionListener(new StartAction());
		}

		lblWordHeading = new JLabel("제시어");
		lblWordHeading.setVerticalAlignment(SwingConstants.TOP);
		lblWordHeading.setFont(new Font("Dialog", Font.BOLD, 26));
		lblWordHeading.setBounds(217, 8, 80, 50);
		contentPane.add(lblWordHeading);

		lblWord = new JLabel();
		lblWord.setVerticalAlignment(SwingConstants.TOP);
		lblWord.setFont(new Font("Dialog", Font.BOLD, 22));
		lblWord.setBounds(306, 12, 385, 50);
		contentPane.add(lblWord);
		btnDrawLine.setBackground(Color.WHITE);

		btnDrawLine.setBounds(703, 70, 40, 40);
		contentPane.add(btnDrawLine);
		btnDrawSquare.setBackground(Color.WHITE);

		btnDrawSquare.setBounds(703, 112, 40, 40);
		contentPane.add(btnDrawSquare);
		btnDrawFilledSquare.setBackground(Color.WHITE);

		btnDrawFilledSquare.setBounds(703, 154, 40, 40);
		contentPane.add(btnDrawFilledSquare);
		btnDrawOval.setBackground(Color.WHITE);

		btnDrawOval.setBounds(703, 196, 40, 40);
		contentPane.add(btnDrawOval);
		btnDrawFilledOval.setBackground(Color.WHITE);

		btnDrawFilledOval.setBounds(703, 238, 40, 40);
		contentPane.add(btnDrawFilledOval);
		btnDrawStraightLine.setBackground(Color.WHITE);

		btnDrawStraightLine.setBounds(703, 280, 40, 40);
		contentPane.add(btnDrawStraightLine);

		btnClear = new JButton("clear");
		btnClear.setBounds(703, 332, 132, 40);
		contentPane.add(btnClear);
		btnClear.addActionListener(new ClearButtonAction());

		btnColorRed = new JButton("Red");
		btnColorRed.setBounds(755, 70, 80, 40);
		btnColorRed.setBackground(Color.RED);
		contentPane.add(btnColorRed);

		btnColorBlue = new JButton("Blue");
		btnColorBlue.setBounds(755, 112, 80, 40);
		btnColorBlue.setBackground(Color.BLUE);
		contentPane.add(btnColorBlue);

		btnColorYellow = new JButton("Yellow");
		btnColorYellow.setBounds(755, 154, 80, 40);
		btnColorYellow.setBackground(Color.YELLOW);
		contentPane.add(btnColorYellow);

		btnColorGreen = new JButton("Green");
		btnColorGreen.setBounds(755, 196, 80, 40);
		btnColorGreen.setBackground(Color.GREEN);
		contentPane.add(btnColorGreen);

		btnColorBlack = new JButton("Black");
		btnColorBlack.setBounds(755, 238, 80, 40);
		btnColorBlack.setBackground(Color.BLACK);
		contentPane.add(btnColorBlack);

		btnColorRed.setForeground(Color.WHITE);
		btnColorBlue.setForeground(Color.WHITE);
		btnColorBlack.setForeground(Color.WHITE);

		imgBtn = new JButton("+");
		imgBtn.setBounds(755, 280, 80, 40);
		contentPane.add(imgBtn);

		JLabel lblNewLabel = new JLabel("작성자");
		lblNewLabel.setBounds(703, 10, 74, 18);
		contentPane.add(lblNewLabel);

		lblDrawingUser = new JLabel("");
		lblDrawingUser.setFont(new Font("Dialog", Font.BOLD, 18));
		lblDrawingUser.setBounds(703, 40, 132, 18);
		contentPane.add(lblDrawingUser);

		lblScore = new JLabel("SCORE : 0");
		lblScore.setFont(new Font("Dialog", Font.BOLD, 14));
		lblScore.setBounds(709, 384, 126, 18);
		contentPane.add(lblScore);

		lblUser = new JLabel("");
		lblUser.setFont(new Font("Dialog", Font.BOLD, 20));
		lblUser.setBounds(12, 8, 193, 50);
		contentPane.add(lblUser);
		imgBtn.addActionListener(new ImageSendAction());

		btnDrawLine.addActionListener(new DrawButtonAction());
		btnDrawSquare.addActionListener(new DrawButtonAction());
		btnDrawFilledSquare.addActionListener(new DrawButtonAction());
		btnDrawOval.addActionListener(new DrawButtonAction());
		btnDrawFilledOval.addActionListener(new DrawButtonAction());
		btnDrawStraightLine.addActionListener(new DrawButtonAction());

		btnColorRed.addActionListener(new ColorButtonAction());
		btnColorBlue.addActionListener(new ColorButtonAction());
		btnColorYellow.addActionListener(new ColorButtonAction());
		btnColorGreen.addActionListener(new ColorButtonAction());
		btnColorBlack.addActionListener(new ColorButtonAction());

		MyMouseEvent mouse = new MyMouseEvent();
		drawingPanel.addMouseMotionListener(mouse);
		drawingPanel.addMouseListener(mouse);

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
				tmpImage = createImage(drawingPanel.getWidth(), drawingPanel.getHeight());
				gc3 = tmpImage.getGraphics();
			}
		});

		setVisible(true);

	}

	public class SendAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AppendTextR(txtInput.getText());
			SendChat("200", txtInput.getText());
			if (txtInput.getText().equals(word)) {
				SendChat("204", txtInput.getText());
			}
			txtInput.setText("");
		}

	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				// frame.setVisible(true);
				// fd.setDirectory(".\\");
				fd.setVisible(true);
				// System.out.println(fd.getDirectory() + fd.getFile());
				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					ChatMsg obcm = new ChatMsg(userId, "202", "IMG", roomId);
					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.img = img;
					SendObject(obcm);
				}
			}
		}
	}

	public class StartAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SendChat("203", "Start Game");
			btnGameStart.setEnabled(false);
		}

	}

	public class ClearButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			clearDrawingPanel();
			ChatMsg cm = new ChatMsg(userId, "201", "Clear", getRoomId());
			SendObject(cm);
		}

	}

	public class DrawButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			btnDrawLine.setBackground(Color.WHITE);
			btnDrawSquare.setBackground(Color.WHITE);
			btnDrawFilledSquare.setBackground(Color.WHITE);
			btnDrawOval.setBackground(Color.WHITE);
			btnDrawFilledOval.setBackground(Color.WHITE);
			btnDrawStraightLine.setBackground(Color.WHITE);

			JButton btn = (JButton) e.getSource();
			btn.setBackground(Color.YELLOW);
			btnDraw = btn;
		}

	}

	public class ColorButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			penColor = btn.getBackground();
		}

	}

	public void setWord(String word) {
		this.word = word;
		if (isTurn()) {
			lblWord.setText(word);
		} else {
			lblWord.setText("");
		}
	}

	public void setCurrUser(String user_id) {
		lblUser.setText(user_id);
	}

	public void setDrawingUser(String user_id) {
		lblDrawingUser.setText(user_id);
	}

	public void clearDrawingPanel() {
		gc2.setColor(drawingPanel.getBackground());
		gc2.fillRect(0, 0, drawingPanel.getWidth() - 1, drawingPanel.getHeight() - 1);
		gc.drawImage(panelImage, 0, 0, drawingPanel);
	}

	public void enableStartBtn(boolean b) {
		btnGameStart.setEnabled(b);
	}

	public void enableTextField(boolean b) {
		txtInput.setEnabled(b);
	}

	public void enableImgBtn(boolean b) {
		imgBtn.setEnabled(b);
	}

	public void enableClearBtn(boolean b) {
		btnClear.setEnabled(b);
	}

	public void AppendText(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();

		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setFontSize(left, 12);
		StyleConstants.setBold(left, false);
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
		StyleConstants.setFontSize(right, 12);
		StyleConstants.setBold(right, false);
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

	public void AppendNotice(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();

		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setFontSize(center, 12);
		StyleConstants.setBold(center, true);
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setForeground(center, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, center, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", center);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
	}

	public void AppendImage(ImageIcon ori_icon) {
		Image ori_img = ori_icon.getImage();

		gc2.drawImage(ori_img, 0, 0, drawingPanel.getWidth(), drawingPanel.getHeight(), drawingPanel);
		gc.drawImage(panelImage, 0, 0, drawingPanel.getWidth(), drawingPanel.getHeight(), drawingPanel);

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
		int nx, ny;
		Color c;
		if (cm.userId.matches(userId)) // 본인 것은 이미 Local 로 그렸다.
			return;
		c = cm.pen_color;
		gc2.setColor(c);

		if (cm.data.equals("Draw line")) {
			if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
				ox = cm.mouse_e.getX();
				oy = cm.mouse_e.getY();
			} else if (cm.mouse_e.getID() == MouseEvent.MOUSE_DRAGGED) {
				nx = cm.mouse_e.getX();
				ny = cm.mouse_e.getY();
				gc2.drawLine(ox, oy, nx, ny);
				ox = nx;
				oy = ny;
			}
		} else if (cm.data.equals("Draw square")) {
			if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
				x1 = cm.mouse_e.getX();
				y1 = cm.mouse_e.getY();
			} else if (cm.mouse_e.getID() == MouseEvent.MOUSE_RELEASED) {
				x2 = cm.mouse_e.getX();
				y2 = cm.mouse_e.getY();
				gc2.drawRect(x1, y1, x2 - x1, y2 - y1);
			}
		} else if (cm.data.equals("Draw filled square")) {
			if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
				x1 = cm.mouse_e.getX();
				y1 = cm.mouse_e.getY();
			} else if (cm.mouse_e.getID() == MouseEvent.MOUSE_RELEASED) {
				x2 = cm.mouse_e.getX();
				y2 = cm.mouse_e.getY();
				gc2.fillRect(x1, y1, x2 - x1, y2 - y1);
			}
		} else if (cm.data.equals("Draw oval")) {
			if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
				x1 = cm.mouse_e.getX();
				y1 = cm.mouse_e.getY();
			} else if (cm.mouse_e.getID() == MouseEvent.MOUSE_RELEASED) {
				x2 = cm.mouse_e.getX();
				y2 = cm.mouse_e.getY();
				gc2.drawOval(x1, y1, x2 - x1, y2 - y1);
			}
		} else if (cm.data.equals("Draw filled oval")) {
			if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
				x1 = cm.mouse_e.getX();
				y1 = cm.mouse_e.getY();
			} else if (cm.mouse_e.getID() == MouseEvent.MOUSE_RELEASED) {
				x2 = cm.mouse_e.getX();
				y2 = cm.mouse_e.getY();
				gc2.fillOval(x1, y1, x2 - x1, y2 - y1);
			}
		} else if (cm.data.equals("Draw straight line")) {
			if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
				x1 = cm.mouse_e.getX();
				y1 = cm.mouse_e.getY();
			} else if (cm.mouse_e.getID() == MouseEvent.MOUSE_RELEASED) {
				x2 = cm.mouse_e.getX();
				y2 = cm.mouse_e.getY();
				gc2.drawLine(x1, y1, x2, y2);
			}
		} else if (cm.data.equals("Clear")) {
			gc2.setColor(drawingPanel.getBackground());
			gc2.fillRect(0, 0, drawingPanel.getWidth() - 1, drawingPanel.getHeight() - 1);
		}
		gc.drawImage(panelImage, 0, 0, drawingPanel);
	}

	public void SendMouseEvent(MouseEvent e, String drawing_type) {
		ChatMsg cm = new ChatMsg(userId, "201", drawing_type, getRoomId());
		cm.mouse_e = e;
		cm.pen_size = pen_size;
		cm.pen_color = penColor;
		SendObject(cm);
	}

	public String getRoomId() {
		return roomId;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public boolean isTurn() {
		return isTurn;
	}

	public void setTurn(boolean isTurn) {
		enableTextField(!isTurn);
		enableImgBtn(isTurn);
		enableClearBtn(isTurn);
		this.isTurn = isTurn;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		lblScore.setText("SCORE : " + score);
	}

	public String getWord() {
		return word;
	}

	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {

		int mox, moy, nx, ny;

		@Override
		public void mouseDragged(MouseEvent e) {
			if (isTurn()) {
				Color c = penColor;
				gc2.setColor(c);

				if (btnDraw == btnDrawLine) {
					nx = e.getX();
					ny = e.getY();
					gc2.drawLine(mox, moy, nx, ny);
					mox = nx;
					moy = ny;
					gc.drawImage(panelImage, 0, 0, drawingPanel);
					SendMouseEvent(e, "Draw line");
				} else if (btnDraw == btnDrawSquare) {
					x2 = e.getX();
					y2 = e.getY();
					gc2.drawImage(tmpImage, 0, 0, drawingPanel);
					gc2.drawRect(x1, y1, x2 - x1, y2 - y1);
					gc.drawImage(panelImage, 0, 0, drawingPanel);
				} else if (btnDraw == btnDrawFilledSquare) {
					x2 = e.getX();
					y2 = e.getY();
					gc2.drawImage(tmpImage, 0, 0, drawingPanel);
					gc2.fillRect(x1, y1, x2 - x1, y2 - y1);
					gc.drawImage(panelImage, 0, 0, drawingPanel);
				} else if (btnDraw == btnDrawOval) {
					x2 = e.getX();
					y2 = e.getY();
					gc2.drawImage(tmpImage, 0, 0, drawingPanel);
					gc2.drawOval(x1, y1, x2 - x1, y2 - y1);
					gc.drawImage(panelImage, 0, 0, drawingPanel);
				} else if (btnDraw == btnDrawFilledOval) {
					x2 = e.getX();
					y2 = e.getY();
					gc2.drawImage(tmpImage, 0, 0, drawingPanel);
					gc2.fillOval(x1, y1, x2 - x1, y2 - y1);
					gc.drawImage(panelImage, 0, 0, drawingPanel);
				} else if (btnDraw == btnDrawStraightLine) {
					x2 = e.getX();
					y2 = e.getY();
					gc2.drawImage(tmpImage, 0, 0, drawingPanel);
					gc2.drawLine(x1, y1, x2, y2);
					gc.drawImage(panelImage, 0, 0, drawingPanel);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isTurn()) {
				if (btnDraw == btnDrawLine) {
					mox = e.getX();
					moy = e.getY();
					SendMouseEvent(e, "Draw line");
				} else if (btnDraw == btnDrawSquare) {
					gc3.drawImage(panelImage, 0, 0, drawingPanel);
					x1 = e.getX();
					y1 = e.getY();
					SendMouseEvent(e, "Draw square");
				} else if (btnDraw == btnDrawFilledSquare) {
					gc3.drawImage(panelImage, 0, 0, drawingPanel);
					x1 = e.getX();
					y1 = e.getY();
					SendMouseEvent(e, "Draw filled square");
				} else if (btnDraw == btnDrawOval) {
					gc3.drawImage(panelImage, 0, 0, drawingPanel);
					x1 = e.getX();
					y1 = e.getY();
					SendMouseEvent(e, "Draw oval");
				} else if (btnDraw == btnDrawFilledOval) {
					gc3.drawImage(panelImage, 0, 0, drawingPanel);
					x1 = e.getX();
					y1 = e.getY();
					SendMouseEvent(e, "Draw filled oval");
				} else if (btnDraw == btnDrawStraightLine) {
					gc3.drawImage(panelImage, 0, 0, drawingPanel);
					x1 = e.getX();
					y1 = e.getY();
					SendMouseEvent(e, "Draw straight line");
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isTurn()) {
				if (btnDraw == btnDrawLine) {

				} else if (btnDraw == btnDrawSquare) {
					x2 = e.getX();
					y2 = e.getY();
					SendMouseEvent(e, "Draw square");
				} else if (btnDraw == btnDrawFilledSquare) {
					x2 = e.getX();
					y2 = e.getY();
					SendMouseEvent(e, "Draw filled square");
				} else if (btnDraw == btnDrawOval) {
					x2 = e.getX();
					y2 = e.getY();
					SendMouseEvent(e, "Draw oval");
				} else if (btnDraw == btnDrawFilledOval) {
					x2 = e.getX();
					y2 = e.getY();
					SendMouseEvent(e, "Draw filled oval");
				} else if (btnDraw == btnDrawStraightLine) {
					x2 = e.getX();
					y2 = e.getY();
					SendMouseEvent(e, "Draw straight line");
				}
			}
		}
	}
}
