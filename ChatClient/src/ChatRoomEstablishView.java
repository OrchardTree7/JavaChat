import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ChatRoomEstablishView extends JFrame {

	private JPanel contentPane;
	private JButton establishBtn;
	public JTextField textField;
	public JRadioButton rdbtnNewRadioButton;
	public JRadioButton rdbtnNewRadioButton_1;
	public JRadioButton rdbtnNewRadioButton_2;
	public ButtonGroup group;

	public ChatRoomEstablishView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 100);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 221, 218));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(56, 12, 159, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.addActionListener(new EstablishAction());

		establishBtn = new JButton("생성");
		establishBtn.setBounds(227, 23, 59, 28);
		contentPane.add(establishBtn);
		establishBtn.addActionListener(new EstablishAction());

		JLabel lblNewLabel = new JLabel("방 이름");
		lblNewLabel.setBounds(12, 14, 43, 18);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("라운드 수");
		lblNewLabel_1.setBounds(12, 41, 53, 18);
		contentPane.add(lblNewLabel_1);

		rdbtnNewRadioButton = new JRadioButton("3");
		rdbtnNewRadioButton.setBounds(66, 37, 43, 26);
		contentPane.add(rdbtnNewRadioButton);

		rdbtnNewRadioButton_1 = new JRadioButton("5");
		rdbtnNewRadioButton_1.setSelected(true);
		rdbtnNewRadioButton_1.setBounds(113, 37, 43, 26);
		contentPane.add(rdbtnNewRadioButton_1);

		rdbtnNewRadioButton_2 = new JRadioButton("7");
		rdbtnNewRadioButton_2.setBounds(159, 37, 43, 26);
		contentPane.add(rdbtnNewRadioButton_2);

		group = new ButtonGroup();
		group.add(rdbtnNewRadioButton);
		group.add(rdbtnNewRadioButton_1);
		group.add(rdbtnNewRadioButton_2);

		setVisible(true);
	}

	public String getRound() {
		if (rdbtnNewRadioButton.isSelected()) {
			return "3";
		} else if (rdbtnNewRadioButton_2.isSelected()) {
			return "7";
		} else {
			return "5";
		}
	}

	class EstablishAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!textField.getText().isBlank()) {
				setVisible(false);
			}
		}

	}
}
