package ch03;

import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Data;

@Data
public class ServerFrame extends JFrame {
	
	private Server mContext;

	// 백그라운드 패널
	private BackgroundPanel backgroundPanel;

	// 포트
	private JLabel portLabel;
	private JTextField inputPort;
	private JButton connectBtn;

	// 메인 보드
	private JTextArea mainBoard;

	public ServerFrame(Server mContext) {
		this.mContext = mContext;
		initData();
		setInitLayout();
		initListener();
	}

	private void initData() {
		// 백그라운드 패널
		backgroundPanel = new BackgroundPanel();
		// 포트 패널
		portLabel = new JLabel("포트 번호");
		inputPort = new JTextField(10);
		connectBtn = new JButton("연결");

		inputPort.setText("5000");

		// 메인 보드
		mainBoard = new JTextArea();

	}

	private void setInitLayout() {
		setTitle("Talk Talk - 서버 관리자");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);

		// 백그라운드 패널
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// 포트 라벨
		portLabel.setBounds(170, 10, 100, 50);
		backgroundPanel.add(portLabel);
		inputPort.setBounds(150, 50, 100, 20);
		backgroundPanel.add(inputPort);
		connectBtn.setBounds(150, 80, 100, 20);
		backgroundPanel.add(connectBtn);

		mainBoard.setBounds(50, 120, 290, 300);
		mainBoard.setEditable(false);
		mainBoard.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "관리자 로그"));
		mainBoard.setForeground(Color.WHITE);
		mainBoard.setOpaque(false); // TextArea 투명하게
		backgroundPanel.add(mainBoard);

	}

	private void initListener() {
		connectBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mContext.startServer();
			}
		});

		connectBtn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					mContext.startServer();
				}
			}
		});

	}

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
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}

	}

}
