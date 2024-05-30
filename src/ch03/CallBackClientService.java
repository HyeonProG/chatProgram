package ch03;

// 클라이언트 화면 부분에서 이벤트가 일어났을때 데이터를 가져오기 위한 인터페이스
public interface CallBackClientService {

	void clickConnectServerBtn(String id);

	void clickSendMessageBtn(String messageText);

}
