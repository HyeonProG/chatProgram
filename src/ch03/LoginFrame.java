package ch03;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginFrame extends JFrame {

	private Client client;

	private ChatFrame chatFrame;

	// 배경화면
	private BackgroundPanel backgroundPanel;

	private JLabel nameLabel;

	// 텍스트필드
	private JTextField inputName;
	private JButton connectBtn;
	private String name;

	public LoginFrame(Client client) {
		this.client = client;
		initData();
		setInitLayout();
		initListener();
	}

	private void initData() {
		// 백그라운드 패널
		backgroundPanel = new BackgroundPanel();

		nameLabel = new JLabel("닉네임");

		// 이름 텍스트 필드
		inputName = new JTextField();
		connectBtn = new JButton("접속");

	}

	private void setInitLayout() {
		setTitle("Talk Talk - 로그인");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);

		// 백그라운드 패널
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// 이름
		nameLabel.setBounds(175, 250, 50, 50);
		backgroundPanel.add(nameLabel);

		inputName.setBounds(140, 290, 110, 20);
		backgroundPanel.add(inputName);

		connectBtn.setBounds(160, 320, 70, 20);
		backgroundPanel.add(connectBtn);

	}

	private void initListener() {

		connectBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				name = inputName.getText();
				client.clickConnectServerBtn(name);
				setVisible(false);
			}
		});

		connectBtn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					name = inputName.getText();
					client.clickConnectServerBtn(name);
					setVisible(false);
				}
			}
		});

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

//	private void connectToClient() {
//		name = inputName.getText();
//		setVisible(false);
//		new Client(name);
//		(client = new Client()).setName(name);
//		client.clickConnectServerBtn(name);

//	}

//	public static void main(String[] args) {
//		new LoginFrame();
//	}

}
