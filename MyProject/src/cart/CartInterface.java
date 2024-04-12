package cart;

import java.util.ArrayList;

import member.Customer;
import performance.Performance;

public interface CartInterface {
//	void printPerformanceList(ArrayList<Performance> pList); // 전체 공연 정보 목록 출력

	boolean isCartInPerformance(String id,int quantity,String seatNum); // 장바구니에 담긴 갯수를 고객 임의 지정 좌석갯수 증가

	void insertPerformance(Performance p,int quantity,Customer nowUser,String seatNum); // CartItem에 공연 정보를 등록

	void removeCart(int numId); // 장바구니 순번 numId의 항목을 삭제

	void deleteCart(); // 장바구니의 모든 항목을 삭제
	
	void printCart();
	//공연일 지나면 구매 안됨
}