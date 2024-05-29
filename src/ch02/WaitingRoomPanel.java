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

	// 백그라운드
	private Image backgroundImage;
	private JPanel backgroundPanel;
	// 유저, 방 리스트
	private JPanel userListPanel;
	private Image userListImage;
	private JPanel roomListPanel;
	private Image roomListImage;
	private JPanel roomBtnPanel;
	private Image roomBtnImage;

	private JList<String> userList;
	private JList<String> roomList;
	
	private JPanel sendMessagePanel;
	// 비밀 메세지
	private JTextField inputSecretMsg;
	private JButton secretMsgBtn;
	// 버튼
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
		userListImage = new ImageIcon("images/backgroundimage.jpg").getImage();
		roomListPanel = new JPanel();
		roomListImage = new ImageIcon("images/backgroundimage.jpg").getImage();
		roomBtnPanel = new JPanel();
		roomBtnImage = new ImageIcon("images/backgroundimage.jpg").getImage();
		sendMessagePanel = new JPanel();

		userList = new JList<>();
		roomList = new JList<>();

		inputSecretMsg = new JTextField();
		secretMsgBtn = new JButton("Send");
		makeRoomBtn = new JButton("MakeRoom");
		outRoomBtn = new JButton("OutRoom");
		enterRoomBtn = new JButton("EnterRoom");
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

		roomBtnPanel.setSize(getWidth(), getHeight());
		roomBtnPanel.setLayout(null);
		
		roomBtnPanel.setBounds(50, 310, 300, 30);
		roomBtnPanel.setBackground(Color.WHITE);

		makeRoomBtn.setBackground(Color.WHITE);
		makeRoomBtn.setBounds(0, 5, 100, 25);
		makeRoomBtn.setEnabled(false);

		outRoomBtn.setBackground(Color.WHITE);
		outRoomBtn.setBounds(105, 5, 90, 25);
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

			String roomName = roomList.getSelectedValue(); // getSelectedValue() : 선택된 항목 반환
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
