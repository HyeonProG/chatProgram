package ch01;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    ServerSocket serverSocket;
    Socket socket;
    private ServerGui serverGui;
    Vector<ChatThread> chatlist = new Vector<>();

    public Server() {
        start();
    }

    public void start() {
        try {
            // Gui 켜기
            serverGui = new ServerGui();

            serverGui.chatTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String msg = serverGui.getChatMessage().trim(); // 공백 제거
                        if (!msg.isEmpty()) {
                            serverGui.appendMessage("서버 : " + msg);
                            sendToAll("서버 : " + msg);
                        }
                        serverGui.setTextFieldBlank();
                    }
                }
            });

            // 서버 소켓 생성
            serverSocket = new ServerSocket(5000);
            serverGui.appendMessage("Server Start...");

            // 클라이언트 연결 대기
            while (true) {
                socket = serverSocket.accept();
                ChatThread chatThread = new ChatThread(); // 스레드 생성
                chatlist.add(chatThread); // ArrayList 에 스레드 추가
                serverGui.appendMessage("클라이언트가 연결 되었습니다.");
                chatThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String msg) {
        for (ChatThread thread : chatlist) {
            thread.writer.println(msg);
        }
    }

    class ChatThread extends Thread {
        String msg;
        String[] msgs;
        private BufferedReader reader = null;
        private PrintWriter writer = null;
        private User user;

        @Override
        public void run() {
            boolean login = true;
            user = new User(); // user 객체 생성
            user.setThreadName(this.getName()); // user 스레드 이름 입력
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("서버에 연결 하였습니다");

                while (login) {
                    msg = reader.readLine();
                    msgs = msg.split("/"); // 구분자 기준으로 나누기

                    serverGui.appendMessage(user.getId() + " : " + msg);

                    if (msgs[0].equals("login")) { // 로그인 처리
                        user.setId(msgs[1]);
                        sendToAll("login/" + user.getId() + "/" + "님이 입장하였습니다.");
                        for (int i = 0; i < chatlist.size(); i++) {
							ChatThread chatThread = chatlist.elementAt(i);
							writer.println("login/" + chatThread.user.getId());
						}
                        // sendToAll("userlist/add/" + user.getId());
                        serverGui.appendMessage(user.getId() + "님이 입장하였습니다.");
                        serverGui.appendUserList(user.getId());
                    } else if (msg.equals("quit")) { // 로그아웃 처리
                        sendToAll(user.getId() + "님이 나갔습니다.");
                        // sendToAll("userlist/remove/" + user.getId());
                        serverGui.appendMessage(user.getId() + "님이 나갔습니다.");
                        login = false; // 클라이언트가 quit 을 치면 나감
                    } else {
                        sendToAll(user.getId() + " : " + msg); // 클라이언트에 클라이언트 메세지 전송
                    }

                }

                // 클라이언트 챗스레드 종료
                chatlist.remove(this);
                serverGui.removeUserList(user.getId());
                this.interrupt(); // 스레드 정상 종료

            } catch (Exception e) {
                // 클라이언트와 연결이 끊어짐
                serverGui.appendMessage("유저 " + user.getId() + " 의 연결이 끊어졌습니다.");
                serverGui.removeUserList(user.getId());
                chatlist.remove(this);
            }
        }

    }

    public static void main(String[] args) {
        new Server();
    }

}
