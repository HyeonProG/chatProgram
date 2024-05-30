package ch03;


// 서버와 클라이언트 둘 사이의 기능 인터페이스
public interface ProtocolImpl {

	void chatting();
	
	void newUser();
	
	void connectedUser();
	
}
