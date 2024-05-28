package ch03;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.Data;

@Data
public class ChatFrame extends JFrame implements ActionListener {
	
	private LoginFrame loginFrame;

	private BackgroundPanel backgroundPanel;
	private JPanel userListPanel;
	private JLabel userListLabel;
	private JPanel roomListPanel;
	private JLabel roomListLabel;
	private JPanel chatListPanel;
	private JLabel chatListLabel;
	private JPanel roomBtnPanel;
	private JPanel sendMessagePanel;
	private JTextField sendMessage;

	private JList<String> userList;
	private JList<String> roomList;

	private JButton makeRoomBtn;
	private JButton outRoomBtn;
	private JButton enterRoomBtn;
	private JButton enterBtn;

	private Vector<String> userIdVector = new Vector<>();
	private Vector<String> roomNameVector = new Vector<>();

	private CallBackClientService callBackService;

	public ChatFrame(String name) {
		initData();
		setInitLayout();
		addListener();

	}

	private void initData() {
		setTitle("TalkTalk");
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 배경화면
		backgroundPanel = new BackgroundPanel();

		// 유저 리스트
		userListPanel = new JPanel();
		userListLabel = new JLabel("유저 목록");

		// 방 리스트
		roomListPanel = new JPanel();
		roomListLabel = new JLabel("방 리스트");

		// 채팅
		chatListPanel = new JPanel();
		chatListLabel = new JLabel("대기실 채팅");
		sendMessage = new JTextField();
		enterBtn = new JButton("전송");

		sendMessagePanel = new JPanel();

		userList = new JList<>();
		roomList = new JList<>();

		makeRoomBtn = new JButton("방 만들기");
		outRoomBtn = new JButton("방 나가기");
		enterRoomBtn = new JButton("방 들어가기");

	}

	private void setInitLayout() {
		setLayout(null);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);

		// 배경화면
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// 유저 목록
		userListPanel.setBounds(550, 30, 120, 260);
		userListPanel.setLayout(null);
		userListLabel.setBounds(582, 10, 120, 20);
		backgroundPanel.add(userListPanel);
		backgroundPanel.add(userListLabel);
		// 방 목록
		roomListPanel.setBounds(400, 30, 120, 260);
		roomListPanel.setLayout(null);
		roomListLabel.setBounds(430, 10, 120, 20);
		backgroundPanel.add(roomListPanel);
		backgroundPanel.add(roomListLabel);
		// 채팅
		chatListPanel.setBounds(20, 30, 350, 260);
		chatListPanel.setLayout(null);
		chatListLabel.setBounds(160, 10, 120, 20);
		backgroundPanel.add(chatListPanel);
		backgroundPanel.add(chatListLabel);

		// 메세지 보내기
		sendMessage.setBounds(20, 295, 280, 20);
		sendMessage.setLayout(null);
		backgroundPanel.add(sendMessage);

		enterBtn.setBounds(310, 295, 60, 20);
		enterBtn.setLayout(null);
		backgroundPanel.add(enterBtn);

		// 방생성 버튼
		makeRoomBtn.setBounds(400, 295, 120, 20);
		makeRoomBtn.setLayout(null);
		backgroundPanel.add(makeRoomBtn);

	}

	private void addListener() {
		makeRoomBtn.addActionListener(this);
		outRoomBtn.addActionListener(this);
		enterRoomBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == makeRoomBtn) {
			String roomName = JOptionPane.showInputDialog("[방 만들기] - 방 이름 설정");
			if (!roomName.equals(null)) {
				callBackService.clickMakeRoomBtn(roomName);
			}
		}

	}

	// 배경 이미지
	private class BackgroundPanel extends JPanel {
		private JPanel backgroundPanel;
		private Image backgroundImage;

		public BackgroundPanel() {
			backgroundImage = new ImageIcon("images/backgroundImage.jpg").getImage();
			backgroundPanel = new JPanel();
			add(backgroundPanel);

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}

	}

}
