package ch01;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.TextField;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import lombok.Data;

@Data
public class ServerGui {

	private BackgroundPanel backgroundPanel;

	private JFrame serverFrame;
	private TextArea chatTextArea;
	protected TextField chatTextField;
	private JList list; // 여러 개의 선택 항목 중에서 하나를 선택하기 위한 컴포넌트
	private DefaultListModel model; // 문자열 같은 아이템 추가

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

	public ServerGui() {

		serverFrame = new JFrame();
		serverFrame.setDefaultCloseOperation(serverFrame.EXIT_ON_CLOSE);
		serverFrame.setBounds(100, 100, 825, 475);

		backgroundPanel = new BackgroundPanel();
		backgroundPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		serverFrame.setContentPane(backgroundPanel);
		backgroundPanel.setLayout(null);

		chatTextArea = new TextArea();
		chatTextArea.setEditable(false);
		chatTextArea.setBounds(18, 12, 567, 384);
		chatTextArea.setBackground(Color.WHITE);
		backgroundPanel.add(chatTextArea);

		chatTextField = new TextField();
		chatTextField.setColumns(30);
		chatTextField.setBounds(17, 403, 572, 22);
		backgroundPanel.add(chatTextField);

		JLabel userListLabel = new JLabel("유저 목록");
		userListLabel.setBounds(670, 16, 100, 16);
		backgroundPanel.add(userListLabel);

		model = new DefaultListModel();

		list = new JList(model);
		list.setBounds(602, 41, 199, 373);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED)); // 컴포넌트가 들어가거나 튀어나오게 하는 기능
		backgroundPanel.add(list);

		serverFrame.setResizable(false);
		serverFrame.setTitle("채팅 프로그램 서버");
		serverFrame.setVisible(true);

	}

	public void setFrameVisible() {
		serverFrame.setVisible(true);
	}

	public void setTextFieldBlank() {
		chatTextField.setText(null);
	}

	public void appendMessage(String message) {
		chatTextArea.append(message + "\n");
	}

	public void appendUserList(String user) {
		model.addElement(user);
	}

	public void removeUserList(String user) {
		model.removeElement(user);
	}

	public String getChatMessage() {
		return chatTextField.getText();
	}

}
