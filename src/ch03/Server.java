package ch03;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Server {

	// 프레임 창
	private ServerFrame serverFrame;

	private JTextArea mainBoard;

	// 소켓
	private ServerSocket serverSocket;
	private Socket socket;

	// 입출력 장치
	private BufferedReader reader;
	private PrintWriter writer;

	// 유저 리스트
	private Vector<ConnectedUser> connectedUsers = new Vector<>();

	// 로그 저장 장치
	private FileWriter fileWriter;

	// 서버측에서 읽어온 요청을 프로토콜 별로 구별해서 해당 메소드를 호출한다.
	// 요청이 문자열로 넘어오고 / 슬러시를 기준으로 문자열을 나눈다.
	// protocol ㅡ> 어떤 작업을 해야하는지 알려주는 약속.
	// from ㅡ> 보내는 측이 담겨 있다.
	// message ㅡ> 보내고자 하는 메세지를 담는다.

	private String protocol;
	private String from;
	private String message;

	public Server() {
		serverFrame = new ServerFrame(this);
		mainBoard = serverFrame.getMainBoard();
	}

	// 서버 시작
	public void startServer() {
		try {
			// 서버 소켓 장치
			serverSocket = new ServerSocket(5000);
			serverViewAppendWriter("관리자 - 서버 시작\n");
			serverFrame.getConnectBtn().setEnabled(false);
			connectClient();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트입니다.", "알림", JOptionPane.ERROR_MESSAGE);
			serverFrame.getConnectBtn().setEnabled(true);
		}
	}

	private void connectClient() {
		new Thread(() -> {
			while (true) {
				try {
					// 소켓 장치
					socket = serverSocket.accept();
					serverViewAppendWriter("알림 - 사용자 접속 대기\n");

					// 연결을 대기하다 유저가 들어오면 유저 생성
					ConnectedUser user = new ConnectedUser(socket);
					user.start();

				} catch (IOException e) {
					// 서버 중지
					serverViewAppendWriter("에러 - 접속 에러!\n");
				}
			}
		}).start();
	}

	// 전체 유저에게 출력
	private void broadCast(String msg) {
		for (int i = 0; i < connectedUsers.size(); i++) {
			ConnectedUser user = connectedUsers.elementAt(i);
			user.writer(msg);
		}
	}

	// 서버로 들어오는 요청 모두 저장되는 fileWriter
	private void serverViewAppendWriter(String str) {
		try {
			fileWriter = new FileWriter("TalkTalk_log.txt", true);
			mainBoard.append(str);
			fileWriter.write(str);
			fileWriter.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 클라이언트가 연결이 되면 ConnectUser 클래스 생성
	private class ConnectedUser extends Thread implements ProtocolImpl {
		// 소켓 장치
		private Socket socket;

		// 입출력 장치
		private BufferedReader reader;
		private PrintWriter writer;

		// 유저 정보
		private String id;

		public ConnectedUser(Socket socket) {
			this.socket = socket;
			connectIO();
		}

		// 입출력 장치 연결
		private void connectIO() {
			try {
				// 입출력 장치
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				sendInformation();

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "서버 입출력 장치 에러!", "알림", JOptionPane.ERROR_MESSAGE);
				serverViewAppendWriter("에러 - 서버 입출력 장치 에러!\n");
			}
		}

		// 처음 유저가 접속했을때 화면 부분 명단 업데이트
		// 접속되어 있는 유저에게 새로운 유저를 알리기
		private void sendInformation() {
			try {
				// 유저의 아이디를 가져온다.
				id = reader.readLine();
				serverViewAppendWriter("[접속]" + id + "님\n");
				// 이미 접속한 유저들에게 유저 명단 업데이트를 위한 출력
				newUser();
				// 방금 연결된 유저측에서 유저 명단 업데이트를 위한 출력
				connectedUser();

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "접속 에러!", "알림", JOptionPane.ERROR_MESSAGE);
				serverViewAppendWriter("에러 - 접속 에러!\n");
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					String str = reader.readLine();
					checkProtocol(str);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "유저 접속 끊김!", "알림", JOptionPane.ERROR_MESSAGE);
				serverViewAppendWriter("에러 - 유저 " + id + " 접속 끊김\n");

				connectedUsers.remove(this);
				broadCast("UserOut/" + id);
			}
		}

		// 프로토콜을 구별해서 해당 메서드 호출
		private void checkProtocol(String str) {
			StringTokenizer tokenizer = new StringTokenizer(str, "/");

			protocol = tokenizer.nextToken();
			from = tokenizer.nextToken();

			if (protocol.equals("Chatting")) {
				message = tokenizer.nextToken();
				chatting();
			}

		}

		// 클라이언트 측으로 보내는 응답
		private void writer(String str) {
			try {
				writer.write(str + "\n");
				writer.flush();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "서버 출력 에러!", "알림", JOptionPane.ERROR_MESSAGE);
			}
		}

		// 프로토콜 인터페이스

		@Override
		public void chatting() {
			serverViewAppendWriter("메세지 - " + from + "_" + message + "\n");
			broadCast("Chatting/" + id + "/" + message);
		}

		@Override
		public void newUser() {
			// 본인을 벡터에 추가
			connectedUsers.add(this);
			broadCast("NewUser/" + id);
		}

		@Override
		public void connectedUser() {
			for (int i = 0; i < connectedUsers.size(); i++) {
				ConnectedUser user = connectedUsers.elementAt(i);
				writer("ConnectedUser/" + user.id);
			}
		}
	}

	public static void main(String[] args) {
		new Server();
	}

}
