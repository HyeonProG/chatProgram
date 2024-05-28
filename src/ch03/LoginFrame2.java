package ch03;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginFrame2 extends JFrame implements ActionListener {

	private BackgroundPanel backgroundPanel;
	private JLabel loginText;
	private JTextField loginMsg;
	private BufferedReader reader;
	private PrintWriter writer;
	private JButton loginBtn;
	private String name;
	
	private ChatFrame chatFrame;
	
	public LoginFrame2() {
		initData();
		setInitLayout();
		addListener();
	}
	
	private void initData() {
		setTitle("TalkTalk 로그인");
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 배경화면
		backgroundPanel = new BackgroundPanel();
		
		// 로그인 버튼
		loginBtn = new JButton();
		
		// ID 입력 칸
		loginMsg = new JTextField();		
		loginText = new JLabel("아이디를 입력하세요");
		
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
		// 로그인 버튼
		loginBtn.setText("Login");
		loginBtn.setBounds(110, 200, 70, 20);
		loginBtn.setLayout(null);
		backgroundPanel.add(loginBtn);
		// 로그인 메세지
		loginMsg.setBounds(110, 170, 70, 20);
		loginMsg.setLayout(null);
		backgroundPanel.add(loginMsg);
		// 로그인 텍스트
		loginText.setBounds(90, 150, 200, 20);
		loginText.setForeground(Color.WHITE);
		backgroundPanel.add(loginText);
		
		
	}
	
	private void addListener() {
		loginBtn.addActionListener(this);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.loginBtn) {
			System.out.println("로그인 버튼 작동");
			if (name.equals(null)) {
				sendToChatFrame();
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
	
	private void sendToChatFrame() {
		name = loginMsg.getText();
		setVisible(false);
		new ChatFrame(name);
	}
	
	public static void main(String[] args) {
		new LoginFrame2();
	}

	
}
