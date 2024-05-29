package ch01;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginGui extends JFrame {

	private BackgroundPanel backgroundPanel; // 전체 틀 생성
	private JTextField textField; // 닉네임 읽어들이는 텍스트 필드
	private JButton enterButton; // 입력 버튼
	private String name; // 닉네임

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

	public LoginGui() {
		enterPanel(); // 전체 틀 생성
		nickNameLabel(); // 닉네임 입력 라벨, 텍스트 필드 생성
		enterButtonEvent(); // 입장 버튼 생성, 처리
		setVisible(true);
		setResizable(false);
	}

	public void enterPanel() {
		setTitle("Talk Talk");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300); // 패널 사이즈
		backgroundPanel = new BackgroundPanel();
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);
	}

	public void nickNameLabel() {
		JLabel nickName = new JLabel("닉네임 입력");
		nickName.setBounds(180, 60, 80, 15);
		backgroundPanel.add(nickName);

		textField = new JTextField();
		textField.setBounds(155, 75, 115, 20);
		backgroundPanel.add(textField);
		textField.setColumns(10); // 글자 수 제한
	}

	public void enterButtonEvent() {
		enterButton = new JButton("입장");
		enterButton.addActionListener(new ActionListener() {
			// 입장 버튼 클릭시 이벤트 처리
			@Override
			public void actionPerformed(ActionEvent e) {
				sendToChatGui();
			}
		});
		enterButton.setBounds(165, 134, 97, 23);
		backgroundPanel.add(enterButton);
		// 엔터키 입력시 입장 처리
		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendToChatGui();
				}
			}

		});

	}

	public void sendToChatGui() {
		name = textField.getText(); // 닉네임 name 읽어오기
		setVisible(false); // EnterGui 창 끄기
		new ChatGui(name); // ChatGui에 name 값 전달
	}

	public static void main(String[] args) {
		new LoginGui();
	}

}
