package ch03;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Data;

@Data
public class WaitingRoomFrame extends JFrame implements ActionListener {

	// 배경 이미지
	private BackgroundPanel backgroundPanel;
	
	// 유저, 방 리스트
	private JPanel userListPanel;
	private JPanel roomListPanel;
	private JPanel roomBtnPanel;
	
	private JList<String> userList;
	private JList<String> roomList;
	
	// 버튼
	private JButton makeRoomBtn;
	private JButton enterRoomBtn;
	
	private Vector<String> userIdVector = new Vector<>();
	private Vector<String> roomNameVector = new Vector<>();
	
	public WaitingRoomFrame() {

		initData();
		setInitLayout();
		initListener();
	}
	
	private void initData() {
		backgroundPanel = new BackgroundPanel();	
		
		userListPanel = new JPanel();
		roomListPanel = new JPanel();
		roomBtnPanel = new JPanel();

		userList = new JList<>();
		roomList = new JList<>();
		
		makeRoomBtn = new JButton("방 생성");
		enterRoomBtn = new JButton("방 입장");
		
	}
	
	private void setInitLayout() {
		setTitle("Talk Talk");
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// 배경 이미지
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);
		
		// 방 목록
		roomListPanel.setBounds(50, 30, 350, 350);
		roomListPanel.setLayout(null);
		roomListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "방 목록"));
		roomListPanel.setOpaque(false);
		roomListPanel.setForeground(Color.WHITE);
		backgroundPanel.add(roomListPanel);

		// 유저 리스트
		userListPanel.setBounds(500, 30, 250, 350);
		userListPanel.setLayout(null);
		userListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "유저 목록"));
		userListPanel.setOpaque(false);
		userListPanel.setForeground(Color.WHITE);
		backgroundPanel.add(userListPanel);
		
		// 방 패널
		roomBtnPanel.setBounds(50, 380, 350, 30);
		roomBtnPanel.setLayout(null);
		roomBtnPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3)));
		roomBtnPanel.setOpaque(false);
		roomBtnPanel.setForeground(Color.WHITE);
		backgroundPanel.add(roomBtnPanel);
		
		// 방 생성 버튼
		makeRoomBtn.setBounds(0, 3, 100, 25);
		makeRoomBtn.setBackground(Color.WHITE);
		makeRoomBtn.setOpaque(false);
		makeRoomBtn.setForeground(Color.WHITE);
		makeRoomBtn.setEnabled(true);
		roomBtnPanel.add(makeRoomBtn);
		
		// 방 입장 버튼
		enterRoomBtn.setBounds(250, 3, 100, 25);
		enterRoomBtn.setBackground(Color.WHITE);
		enterRoomBtn.setOpaque(false);
		enterRoomBtn.setForeground(Color.WHITE);
		enterRoomBtn.setEnabled(true);
		roomBtnPanel.add(enterRoomBtn);
		
	}
	
	private void initListener() {
		makeRoomBtn.addActionListener(this);
		enterRoomBtn.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == makeRoomBtn) {
			String roomName = JOptionPane.showInputDialog("[방 이름 설정]");
			
			if (!roomName.equals(null)) {
				//callBackService.clickMakeRoomBtn(roomName);
			}
			
		} else if (e.getSource() == enterRoomBtn) {
			String roomName = roomList.getSelectedValue();
			//callBackService.clickEnterRoomBtn(roomName);
			roomList.setSelectedValue(null, false);
		}
	}
	
	
	// 배경 이미지
	private class BackgroundPanel extends JPanel {
		private JPanel backgroundPanel;
		private Image backgroundImage;
		
		public BackgroundPanel() {
			backgroundImage = new ImageIcon("images/backgroundimage.jpg").getImage();
			backgroundPanel = new JPanel();
			add(backgroundPanel);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}
		
	}
	
//	public static void main(String[] args) {
//		new WaitingRoomFrame();
//	}


}
