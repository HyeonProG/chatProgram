package ch01;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class ChatGui extends JFrame {

	private BackgroundPanel backgroundPanel;
	private JTextField textMsg;
	private TextArea chatLog;
	private BufferedReader reader;
	private PrintWriter writer;
	private JList list;
	private DefaultListModel model;

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

	// ChatGui 구현
	public ChatGui(String id) {
		// EnterGui 에서 보내는 name 값을 매개변수로 받음
		String msg = "login/" + id;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 825, 500);
		backgroundPanel = new BackgroundPanel();
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// chatLabel, chatlog
		JLabel chatLabel = new JLabel("채팅방");
		chatLabel.setBounds(240, 10, 95, 15);
		backgroundPanel.add(chatLabel);

		chatLog = new TextArea();
		chatLog.setEditable(false);
		chatLog.setText("채팅 로그");
		chatLog.setBounds(18, 25, 500, 400);
		backgroundPanel.add(chatLog);
		
		JLabel userListLabel = new JLabel("유저 목록");
		userListLabel.setBounds(670, 16, 100, 16);
		backgroundPanel.add(userListLabel);
		
		model = new DefaultListModel();
		
		list = new JList(model);
		list.setBounds(602, 41, 199, 373);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED)); // 컴포넌트가 들어가거나 튀어나오게 하는 기능
		backgroundPanel.add(list);

		textMsg = new JTextField();
		//textMsg.setText("메세지를 입력하세요");
		textMsg.setBounds(18, 430, 500, 20);
		backgroundPanel.add(textMsg);
		textMsg.setColumns(10);

		// 메세지 전송 엔터키 이벤트 처리
		textMsg.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String text = textMsg.getText();
					writer.println(text); // 텍스트를 서버로 전송
					textMsg.setText(""); // 텍스트 필드의 값 지움 - 초기화
				}
			}
		});
		setVisible(true);
		try {
			// 소켓통신
			String serverIP = "localhost";
			int serverPort = 5000;

			// 서버에 연결
			Socket socket = new Socket(serverIP, serverPort);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 서버로부터 메세지를 읽는 스레드
			Thread readThread = new Thread(new ServerMessageReader());
			readThread.start();

			// 서버로 메세지 전송
			OutputStream outputStream = socket.getOutputStream();
			writer = new PrintWriter(outputStream, true);
			writer.println(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 서버에서 주는 메세지 처리
	private class ServerMessageReader implements Runnable {

		@Override
		public void run() {
			try {
				String message;
				while ((message = reader.readLine()) != null) {
					System.out.println("서버로부터 메세지 : " + message);
					uploadText(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 받은 메세지 채팅창에 업로드
		public void uploadText(String message) {
			chatLog.append(message + "\n");
		}

	}

}
