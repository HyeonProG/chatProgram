package ch03;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Data;

@Data
public class ChatFrame extends JFrame {

	private CallBackClientService callBackService;
	private Client mContext;

	private WaitingRoomFrame waitingRoomFrame;

	// 배경 이미지
	private BackgroundPanel backgroundPanel;

	// 패털
	private JPanel mainPanel;
	private JPanel bottomPanel;

	// 채팅 창
	private JTextArea mainMessageBox;
	private JTextField writeMessageBox;

	// 보내기 버튼
	private JButton sendBtn;

	// 유저 리스트
	private JPanel userListPanel;

	private JList<String> userList;

	private Vector<String> userIdVector = new Vector<>();

	public ChatFrame(Client mContext) {
		this.mContext = mContext;
		initData();
		setInitLayout();
		initListener();
	}

	private void initData() {
		backgroundPanel = new BackgroundPanel();

		mainPanel = new JPanel();
		bottomPanel = new JPanel();

		mainMessageBox = new JTextArea();
		writeMessageBox = new JTextField();
		sendBtn = new JButton("전송");

		userListPanel = new JPanel();
		userList = new JList<>();

	}

	private void setInitLayout() {
		// setTitle("Talk Talk");
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// 배경 이미지
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// 채팅 창
		mainMessageBox.setBounds(50, 30, 420, 350);
		mainMessageBox.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 5), "채팅창"));
		mainMessageBox.setOpaque(false); // textArea 투명하게
		mainMessageBox.setForeground(Color.WHITE);
		mainMessageBox.setEnabled(false);
		backgroundPanel.add(mainMessageBox);

		// 채팅 보내는 창
		writeMessageBox.setBounds(55, 390, 340, 20);
		writeMessageBox.setBorder(new LineBorder(Color.WHITE, 2));
		writeMessageBox.setOpaque(false);
		writeMessageBox.setForeground(Color.WHITE);
		backgroundPanel.add(writeMessageBox);

		sendBtn.setBounds(400, 390, 70, 20);
		backgroundPanel.add(sendBtn);

		// 유저 리스트 패털
		userListPanel.setBounds(550, 30, 200, 350);
		userListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 5), "유저 리스트"));
		userListPanel.setOpaque(false);
		userListPanel.setForeground(Color.WHITE);
		backgroundPanel.add(userListPanel);

		// 유저 리스트
		userList.setOpaque(false);
		// 리스트 배경 없애기
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setOpaque(false);
		userList.setCellRenderer(renderer);
		userListPanel.add(userList);

	}

	private void initListener() {
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendMessage();
			}
		});

		writeMessageBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

	}

	private void sendMessage() {
		if (!writeMessageBox.getText().equals(null)) {
			String msg = writeMessageBox.getText();
			mContext.clickSendMessageBtn(msg);
			writeMessageBox.setText("");
			writeMessageBox.requestFocus();
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

}
