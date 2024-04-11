package cart;

import java.util.ArrayList;

import member.Customer;
import performance.Performance;

public interface CartInterface {
	void printPerformanceList(ArrayList<Performance> pList); // 전체 공연 정보 목록 출력
//	void printCart(CartItem cartItem);

	boolean isCartInPerformance(String id,int quantity,String seatNum); // 장바구니에 담긴 갯수를 고객 임의 지정 좌석갯수 증가

	void insertPerformance(Performance p,int quantity,Customer nowUser,String seatNum); // CartItem에 공연 정보를 등록

	void removeCart(int numId); // 장바구니 순번 numId의 항목을 삭제

	void deleteCart(); // 장바구니의 모든 항목을 삭제
	
	void printCart();
//	void payPerformance(); //결제(장바구니 비우고, 결제내역 리스트에 add하기)
	//(만약 팔린 좌석 == 총좌석 이면 매진되어 구매할 수 없습니다.) ★꼭
	
	
	
	
	//공연일 지나면 구매 안됨
	// 우선 좌석 지정없이 티켓 구매자가 선착순으로 앉기(나중에 시간있는 사람은 2차원 배열)
}