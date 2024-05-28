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

import javax.swing.JTextArea;

public class Server {

	// 접속한 유저 벡터
	private Vector<ConnectedUser> connectedUsers = new Vector<>();
	// 만들어진 방 벡터
	private Vector<MyRoom> madeRooms = new Vector<>();

	// 프레임 창
	private ServerFrame serverFrame;

	private JTextArea mainBoard;

	// 소켓 장치
	private ServerSocket serverSocket;
	private Socket socket;
	
	// 파일 저장 장치
	private FileWriter fileWriter;

	private String protocol;
	private String from;
	private String message;

	private boolean roomCheck;

	public Server() {
		serverFrame = new ServerFrame(this);
		mainBoard = serverFrame.getMainBoard();
	}

	// 포트 번호 입력후 서버 시작
	public void startServer() {
		try {
			serverSocket = new ServerSocket(5000);
			serverViewAppendWriter("[알림] 서버 시작\n");
			serverFrame.getConnectBtn().setEnabled(false);
			connectClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 서버가 대기하여 소켓 연결, 스레드 실행
	private void connectClient() {
		new Thread(() -> {
			while (true) {
				try {
					socket = serverSocket.accept();
					serverViewAppendWriter("[알림] 사용자 접속 대기\n");
				} catch (Exception e) {
					e.printStackTrace();
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

	// 서버로 들어오는 모든 요청이 저장되는 filewriter
	private void serverViewAppendWriter(String str) {
		try {
			fileWriter = new FileWriter("talktalk_log.txt", true);
			mainBoard.append(str);
			fileWriter.write(str);
			fileWriter.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ConnectedUser extends Thread implements ProtocolImpl {
		// 소켓 장치
		private Socket socket;

		// 입출력 장치
		private BufferedReader reader;
		private PrintWriter writer;

		// 유저 정보
		private String id;
		private String myRoomName;

		public ConnectedUser(Socket socket) {
			this.socket = socket;
			connectIO();
		}

		// 입출력 장치 연결
		private void connectIO() {
			try {
				// 입출력 장치 연결
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());

				sendInformation();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 유저 명단 업데이트
		private void sendInformation() {
			try {
				id = reader.readLine();
				// 접속된 유저들에게 유저 명단 업데이트를 위한 출력
				newUser();

				// 방금 연결된 유저측에서 유저 명단 업데이트를 위한 출력
				connectedUser();

				// 방금 연결된 유저측에서 룸 명단 업데이트를 위한 출력
				madeRoom();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					String str = reader.readLine();
					checkProtocol(str);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		private void checkProtocol(String str) {
			StringTokenizer tokenizer = new StringTokenizer(str, "/");
			protocol = tokenizer.nextToken();
			from = tokenizer.nextToken();

			if (protocol.equals("Chatting")) {
				message = tokenizer.nextToken();
				chatting();

			} else if (protocol.equals("MakeRoom")) {
				makeRoom();

			} else if (protocol.equals("OutRoom")) {
				outRoom();

			} else if (protocol.equals("EnterRoom")) {
				enterRoom();
			}

		}

		// 클라이언트 측으로 보내는 응답
		private void writer(String str) {
			try {
				writer.write(str + "\n");
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 프로토콜 인터페이스
		@Override
		public void chatting() {
			for (int i = 0; i < madeRooms.size(); i++) {
				MyRoom myRoom = madeRooms.elementAt(i);

				if (myRoom.roomName.equals(from)) {
					myRoom.roomBroadCast("Chatting/" + id + "/" + message);
				}
			}
		}

		@Override
		public void makeRoom() {
			for (int i = 0; i < madeRooms.size(); i++) {
				MyRoom room = madeRooms.elementAt(i);

				if (room.roomName.equals(from)) {
					writer("FailMakeRoom/" + from);
					roomCheck = false;
				} else {
					roomCheck = true;
				}
			}

			if (roomCheck) {
				myRoomName = from;
				MyRoom myRoom = new MyRoom(from, this);
				madeRooms.add(myRoom);

				newRoom();
				writer("MakeRoom/" + from);
			}
		}

		@Override
		public void madeRoom() {
			for (int i = 0; i < madeRooms.size(); i++) {
				MyRoom myRoom = madeRooms.elementAt(i);
				writer("MadeRoom/" + myRoom.roomName);
			}
		}

		@Override
		public void newRoom() {
			broadCast("NewRoom/" + from);
		}

		@Override
		public void outRoom() {
			for (int i = 0; i < madeRooms.size(); i++) {
				MyRoom myRoom = madeRooms.elementAt(i);

				if (myRoom.roomName.equals(from)) {
					myRoomName = null;
					myRoom.roomBroadCast("Chatting/퇴장/" + id + "님 퇴장");
					myRoom.removeRoom(this);
					writer("OutRoom/" + from);
				}
			}
		}

		@Override
		public void enterRoom() {
			for (int i = 0; i < madeRooms.size(); i++) {
				MyRoom myRoom = madeRooms.elementAt(i);

				if (myRoom.roomName.equals(from)) {
					myRoomName = from;
					myRoom.addUser(this);
					myRoom.roomBroadCast("Chatting/입장/" + id + "님 입장");
					writer("EnterRoom/" + from);
				}
			}
		}

		@Override
		public void newUser() {
			// 자기자신을 벡터에 추가
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

	private class MyRoom {
		private String roomName;
		// myRoom 에 들어온 사람들의 정보가 담긴다.
		private Vector<ConnectedUser> myRoom = new Vector<>();

		public MyRoom(String roomName, ConnectedUser connectedUser) {
			this.roomName = roomName;
			this.myRoom.add(connectedUser);
			connectedUser.myRoomName = roomName;
		}

		// 방에 있는사람들에게 출력
		private void roomBroadCast(String msg) {
			for (int i = 0; i < myRoom.size(); i++) {
				ConnectedUser user = myRoom.elementAt(i);

				user.writer(msg);
			}
		}

		private void addUser(ConnectedUser connectedUser) {
			myRoom.add(connectedUser);
		}

		private void removeRoom(ConnectedUser user) {
			myRoom.remove(user);
			boolean empty = myRoom.isEmpty();
			if (empty) {
				for (int i = 0; i < madeRooms.size(); i++) {
					MyRoom myRoom = madeRooms.elementAt(i);

					if (myRoom.roomName.equals(roomName)) {
						madeRooms.remove(this);
						roomBroadCast("OutRoom/" + from);
						broadCast("EmptyRoom/" + from);
						break;
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		new Server();
	}

}
