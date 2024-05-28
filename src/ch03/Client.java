package ch03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Client implements ProtocolImpl, CallBackClientService {

	// 프레임 창
	private LoginFrame loginFrame;
	private ChatFrame chatFrame;

	// Client의 화면 부분의 컴포넌트를 멤버변수로 가져와 담을 변수
	private JTextArea mainMessageBox;
	private JList<String> userList;
	private JList<String> roomList;
	private JButton enterRoomBtn;
	private JButton makeRoomBtn;
	private JButton outRoomBtn;

	// 소켓 장치
	private Socket socket;

	// 입출력 장치
	private BufferedReader reader;
	private PrintWriter writer;

	private String ip;
	private int port;

	// 유저 정보
	private String id;
	private String myRoomName;

	// 토크나이저 사용 변수
	private String protocol;
	private String from;
	private String message;

	// 접속자 명단(userList), 방 명단(roomList)을 업데이트 하기 위한 문자열 벡터
	private Vector<String> userIdList = new Vector<>();
	private Vector<String> roomNameList = new Vector<>();

	public Client() {

	}

	// 소켓 장치 연결
	private void connectNetWork() {
		try {
			socket = new Socket("localhost", 5000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 입출력 장치 연결 후, readThread() 호출
	private void connectIO() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			readThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 서버측의 요청을 받아 올 reader
	private void readThread() {
		new Thread(() -> {
			while (true) {
				try {
					String msg = reader.readLine();
					checkProtocol(msg);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}).start();
	}

	// 클라이언트측에서 서버측으로 보내는 writer
	private void writer(String str) {
		try {
			writer.write(str + "\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkProtocol(String msg) {
		StringTokenizer tokenizer = new StringTokenizer(msg, "/");

		protocol = tokenizer.nextToken();
		from = tokenizer.nextToken();

		if (protocol.equals("Chatting")) {
			message = tokenizer.nextToken();
			chatting();

		} else if (protocol.equals("MakeRoom")) {
			makeRoom();

		} else if (protocol.equals("MadeRoom")) {
			madeRoom();

		} else if (protocol.equals("NewRoom")) {
			newRoom();

		} else if (protocol.equals("OutRoom")) {
			outRoom();

		} else if (protocol.equals("EnterRoom")) {
			enterRoom();

		} else if (protocol.equals("NewUser")) {
			newUser();

		} else if (protocol.equals("ConnectedUser")) {
			connectedUser();
		} else if (protocol.equals("EmptyRoom")) {
			roomNameList.remove(from);
			roomList.setListData(roomNameList);
			makeRoomBtn.setEnabled(true);
			enterRoomBtn.setEnabled(true);
			outRoomBtn.setEnabled(false);
		} else if (protocol.equals("FailMakeRoom")) {
			JOptionPane.showMessageDialog(null, "같은 이름의 방이 존재합니다 !", "[알림]", JOptionPane.ERROR_MESSAGE);
		} else if (protocol.equals("UserOut")) {
			userIdList.remove(from);
			userList.setListData(userIdList);
		}
	}

	@Override
	public void clickConnectServerBtn(String ip, int port, String id) {
		this.ip = ip;
		this.port = port;
		this.id = id;
		try {
			connectNetwork();
			connectIO();

			writer.write(id.trim() + "\n");
			writer.flush();
			chatFrame.setTitle("[ KHA Talk_" + id + "님 ]");

			chatFrame.getLoginFrame().getEnterButton().setEnabled(false);
			// chatFrame.getIndexPanel().getConnectBtn().setEnabled(false);
			makeRoomBtn.setEnabled(true);
			enterRoomBtn.setEnabled(true);
			chatFrame.setEnabled(true);
			chatFrame.setEnabled(true);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "접속 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
		}
	}

	@Override
	public void clickSendMessageBtn(String messageText) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clickMakeRoomBtn(String roomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clickOutRoomBtn(String roomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clickEnterRoomBtn(String roomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void chatting() {
		if (id.equals(from)) {
			mainMessageBox.append("[나] \n" + message + "\n");
		} else if (from.equals("입장")) {
			mainMessageBox.append("▶" + from + "◀" + message + "\n");
		} else if (from.equals("퇴장")) {
			mainMessageBox.append("▷" + from + "◁" + message + "\n");
		} else {
			mainMessageBox.append("[" + from + "] \n" + message + "\n");
		}
	}

	@Override
	public void makeRoom() {
		myRoomName = from;
		makeRoomBtn.setEnabled(false);
		enterRoomBtn.setEnabled(false);
		outRoomBtn.setEnabled(true);
	}

	@Override
	public void madeRoom() {
		roomNameList.add(from);
		if (!(roomNameList.size() == 0)) {
			roomList.setListData(roomNameList);
		}
	}

	@Override
	public void newRoom() {
		roomNameList.add(from);
		roomList.setListData(roomNameList);
	}

	@Override
	public void outRoom() {
		myRoomName = null;
		mainMessageBox.setText("");
		makeRoomBtn.setEnabled(true);
		enterRoomBtn.setEnabled(true);
		outRoomBtn.setEnabled(false);
	}

	@Override
	public void enterRoom() {
		myRoomName = from;
		makeRoomBtn.setEnabled(false);
		enterRoomBtn.setEnabled(false);
		outRoomBtn.setEnabled(true);
	}

	@Override
	public void newUser() {
		if (!from.equals(this.id)) {
			userIdList.add(from);
			userList.setListData(userIdList);
		}
	}

	@Override
	public void connectedUser() {
		userIdList.add(from);
		userList.setListData(userIdList);
	}

}
