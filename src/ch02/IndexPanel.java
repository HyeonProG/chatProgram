package ch02;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Data;

@Data
public class IndexPanel extends JPanel {

	// 백그라운드 이미지 컴포넌트
	private Image backgroundImage;
	private JPanel backgroundPanel;

	// 보더 컴포넌트
	private JPanel borderPanel;

	// ip 컴포넌트
	private JPanel ipPanel;
	private JLabel ipLabel;
	private JTextField inputIp;

	// port 컴포넌트
	private JPanel portPanel;
	private JLabel portLabel;
	private JTextField inputPort;

	// id 컴포넌트
	private JPanel idPanel;
	private JLabel idLabel;
	private JTextField inputId;

	// 로그인 버튼
	private JButton connectBtn;

	private CallBackClientService callBackService;

	public IndexPanel(CallBackClientService callBackService) {
		this.callBackService = callBackService;
		initObject();
		initSetting();
		initListener();
	}

	private void initObject() {
		// 백그라운드 이미지 컴포넌트
		backgroundImage = new ImageIcon("images/backgroundimage.jpg").getImage();
		backgroundPanel = new JPanel();

		// 보더 컴포넌트
		borderPanel = new JPanel();

		// IP 컴포넌트
		ipPanel = new JPanel();
		ipLabel = new JLabel("접속 IP");
		inputIp = new JTextField(10);

		// PORT 컴포넌트
		portPanel = new JPanel();
		portLabel = new JLabel("포트 넘버");
		inputPort = new JTextField(10);

		// ID 컴포넌트
		idPanel = new JPanel();
		idLabel = new JLabel("아이디");
		inputId = new JTextField(10);

		// 로그인 버튼
		connectBtn = new JButton("연결");
	}

	private void initSetting() {
		setSize(getWidth(), getHeight());
		setLayout(null);

		// 백그라운드 이미지 패널
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// 보더 컴포넌트
		borderPanel.setBounds(100, 60, 190, 380);
		borderPanel.setLayout(null);
		borderPanel.setBackground(Color.white);
		borderPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 5), "Login"));
		add(borderPanel);

		// IP 컴포넌트
		ipPanel.setBounds(30, 40, 120, 100);
		ipPanel.setBackground(new Color(0, 0, 0, 0));
		ipPanel.add(ipLabel);
		ipPanel.add(inputIp);
		borderPanel.add(ipPanel);

		// PORT 컴포넌트
		portPanel.setBounds(30, 140, 120, 100);
		portPanel.setBackground(new Color(0, 0, 0, 0));
		portPanel.add(portLabel);
		portPanel.add(inputPort);
		borderPanel.add(portPanel);

		// ID 컴포넌트
		idPanel.setBounds(30, 240, 120, 100);
		idPanel.setBackground(new Color(0, 0, 0, 0));
		idPanel.add(idLabel);
		idPanel.add(inputId);
		borderPanel.add(idPanel);

		// LoginBtn 컴포넌트
		connectBtn.setBackground(Color.WHITE);
		connectBtn.setBounds(30, 340, 120, 20);
		borderPanel.add(connectBtn);

		// 코드 테스트
		inputIp.setText("127.0.0.1");
		inputPort.setText("5000");
	}

	private void initListener() {
		connectBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clickConnectBtn();
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					clickConnectBtn();
				}
			}
		});
	}

	/**
	 * 각 입력칸이 null이 아닐때에 실행된다. 각 입력칸의 Text를 (ip, port, id) 가지고 와서 메소드 호출
	 */
	private void clickConnectBtn() {
		if ((!inputIp.getText().equals(null)) && (!inputPort.getText().equals(null))
				&& (!inputId.getText().equals(null))) {

			String ip = inputIp.getText();
			String stringPort = inputPort.getText();
			int port = Integer.parseInt(stringPort);
			String id = inputId.getText();

			callBackService.clickConnectServerBtn(ip, port, id);
		} else {
			JOptionPane.showMessageDialog(null, "입력한 정보를 확인하세요", "알림", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}
}
