package ch02;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Data;

@Data
public class WaitingRoomPanel extends JPanel implements ActionListener {

	private Image backgroundImage;
	private JPanel backgroundPanel;

	private JPanel userListPanel;
	private JPanel roomListPanel;
	private JPanel roomBtnPanel;
	private JPanel sendMessagePanel;

	private JList<String> userList;
	private JList<String> roomList;

	private JTextField inputSecretMsg;
	private JButton secretMsgBtn;

	private JButton makeRoomBtn;
	private JButton outRoomBtn;
	private JButton enterRoomBtn;

	private Vector<String> userIdVector = new Vector<>();
	private Vector<String> roomNameVector = new Vector<>();

	private CallBackClientService callBackService;

	public WaitingRoomPanel(CallBackClientService callBackService) {
		this.callBackService = callBackService;
		initObject();
		initSetting();
		initListener();
	}

	private void initObject() {
		backgroundImage = new ImageIcon("images/backgroundimage.jpg").getImage();
		backgroundPanel = new JPanel();

		userListPanel = new JPanel();
		roomListPanel = new JPanel();
		roomBtnPanel = new JPanel();
		sendMessagePanel = new JPanel();

		userList = new JList<>();
		roomList = new JList<>();

		inputSecretMsg = new JTextField();
		secretMsgBtn = new JButton("send Message");
		makeRoomBtn = new JButton("makeRoom");
		outRoomBtn = new JButton("outRoom");
		enterRoomBtn = new JButton("enterRoom");
	}

	private void initSetting() {
		setSize(getWidth(), getHeight());
		setLayout(null);

		userListPanel.setBounds(50, 30, 120, 260);
		userListPanel.setBackground(Color.WHITE);
		userListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "user List"));

		userListPanel.add(userList);
		add(userListPanel);

		roomListPanel.setBounds(230, 30, 120, 260);
		roomListPanel.setBackground(Color.WHITE);
		roomListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "room List"));
		roomListPanel.add(roomList);
		add(roomListPanel);

		roomBtnPanel.setBounds(50, 310, 300, 30);
		roomBtnPanel.setBackground(Color.WHITE);
		roomBtnPanel.setLayout(null);

		makeRoomBtn.setBackground(Color.WHITE);
		makeRoomBtn.setBounds(0, 5, 100, 25);
		makeRoomBtn.setEnabled(false);

		outRoomBtn.setBackground(Color.WHITE);
		outRoomBtn.setBounds(108, 5, 85, 25);
		outRoomBtn.setEnabled(false);

		enterRoomBtn.setBackground(Color.WHITE);
		enterRoomBtn.setBounds(200, 5, 100, 25);
		enterRoomBtn.setEnabled(false);

		roomBtnPanel.add(makeRoomBtn);
		roomBtnPanel.add(outRoomBtn);
		roomBtnPanel.add(enterRoomBtn);
		add(roomBtnPanel);

		inputSecretMsg.setBounds(30, 5, 240, 23);
		secretMsgBtn.setBounds(30, 35, 240, 20);
		secretMsgBtn.setBackground(Color.WHITE);
		secretMsgBtn.setEnabled(false);

		sendMessagePanel.setBounds(50, 360, 300, 60);
		sendMessagePanel.setBackground(Color.WHITE);
		sendMessagePanel.setLayout(null);
		sendMessagePanel.add(inputSecretMsg);
		sendMessagePanel.add(secretMsgBtn);
		add(sendMessagePanel);
	}

	private void initListener() {
		makeRoomBtn.addActionListener(this);
		outRoomBtn.addActionListener(this);
		secretMsgBtn.addActionListener(this);
		enterRoomBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == secretMsgBtn) {

			String msg = inputSecretMsg.getText();
			if(!msg.equals(null)) {
				callBackService.clickSendSecretMessageBtn(msg);
				inputSecretMsg.setText("");
				userList.setSelectedValue(null, false);
			}

		} else if (e.getSource() == makeRoomBtn) {

			String roomName = JOptionPane.showInputDialog("[ 방 이름 설정 ]");

			if (!roomName.equals(null)) {
				callBackService.clickMakeRoomBtn(roomName);
			}

		} else if (e.getSource() == outRoomBtn) {

			String roomName = roomList.getSelectedValue();
			callBackService.clickOutRoomBtn(roomName);
			roomList.setSelectedValue(null, false);

		} else if (e.getSource() == enterRoomBtn) {

			String roomName = roomList.getSelectedValue();
			callBackService.clickEnterRoomBtn(roomName);
			roomList.setSelectedValue(null, false);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}
}
