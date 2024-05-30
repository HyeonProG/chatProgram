package ch03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client implements CallBackClientService, ProtocolImpl {

	private LoginFrame loginFrame;

	// 프레임 창
	private ChatFrame chatFrame;

	// 클라이언트 화면 부분의 컴포넌트를 멤버 변수로 가져와 담을 변수
	private JTextArea mainMessageBox;
	private JList<String> userList;
	private JButton sendMessageBtn;

	// 소켓
	private Socket socket;

	// 입출력 장치
	private BufferedReader reader;
	private PrintWriter writer;

	// 연결 주소
	private String ip;
	private int port;

	// 유저 정보
	private String name;

	// 토크나이저 사용 변수
	private String protocol;
	private String from;
	private String message;

	// 유저 명단, 방 명단을 업데이트 하기 위한 벡터
	private Vector<String> userIdList = new Vector<>();

	public Client() {
		loginFrame = new LoginFrame(this);
		// chatFrame = new ChatFrame();
	}

	@Override
	public void chatting() {
		if (name.equals(from)) {
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
	public void newUser() {
		if (!from.equals(this.name)) {
			userIdList.add(from);
			userList.setListData(userIdList);
			uploadText(name + "님이 입장하였습니다.");
		}
	}

	@Override
	public void connectedUser() {
		userIdList.add(from);
		userList.setListData(userIdList);
		uploadText(name + "님이 입장하였습니다.");
	}

	// ConncetServerBtn 을 눌렀을때 실행
	// 소켓 연결, 입출력 장치 호출
	// id 를 서버로 보내고, 화면부분 제목 변경, 버튼 활성화

	@Override
	public void clickConnectServerBtn(String name) {
		ip = "localhost";
		port = 5000;
		this.name = name;
		chatFrame = new ChatFrame(this);
		chatFrame.setTitle("Talk Talk " + name + "님");
		userList = chatFrame.getUserList();
		mainMessageBox = chatFrame.getMainMessageBox();
		try {
			connectNetWork();
			connectIO();

			writer.write(name.trim() + "\n");
			writer.flush();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "접속 에러!!", "알림", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// 소켓 장치 연결
	private void connectNetWork() {
		try {
			socket = new Socket("localhost", 5000);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "접속 에러!!!", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 입출력 장치 연결 후 readThread 호출
	private void connectIO() {
		try {
			// 입출력 장치
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			// 입력 스레드
			readThread();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "클라이언트 입출력 장치 에러!", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 서버측의 요청을 받아올 reader, 요청을 받아서 message 에 저장하고 checkProtocol() 호출
	private void readThread() {
		new Thread(() -> {
			while (true) {
				try {
					String msg = reader.readLine();
					checkProtocol(msg);
					System.out.println(msg);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "클라이언트 입력 장치 에러!", "알림", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					break;
				}
			}
		}).start();
	}

	// 클라이언트 측에서 서버 측으로 보내는 writer
	private void writer(String str) {
		try {
			writer.write(str + "\n");
			writer.flush();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "클라이언트 출력 장치 에러!", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 서버측에서 읽어온 요청을 프로토콜 별로 구별해서 해당 메소드를 호출한다.
	// 요청이 문자열로 넘어오고 / 슬러시를 기준으로 문자열을 나눈다.
	// protocol ㅡ> 어떤 작업을 해야하는지 알려주는 약속.
	// from ㅡ> 보내는 측이 담겨 있다.
	// message ㅡ> 보내고자 하는 메세지를 담는다.
	private void checkProtocol(String msg) {
		StringTokenizer tokenizer = new StringTokenizer(msg, "/");

		protocol = tokenizer.nextToken();
		from = tokenizer.nextToken();

		if (protocol.equals("Chatting")) {
			message = tokenizer.nextToken();
			chatting();

		} else if (protocol.equals("NewUser")) {
			newUser();

		} else if (protocol.equals("ConnectedUser")) {
			connectedUser();

		} else if (protocol.equals("UserOut")) {
			userIdList.remove(from);
			userList.setListData(userIdList);
		}

	}

	// 받은 메세지 채팅창에 업로드
	private void uploadText(String msg) {
		mainMessageBox.append(msg + "\n");
	}

	// 클라이언트 화면에 정보를 받아오는 콜백 인터페이스

	@Override
	public void clickSendMessageBtn(String messageText) {
		writer("Chatting/" + name + "/" + messageText);
	}

	public static void main(String[] args) {
		new Client();
	}

}
