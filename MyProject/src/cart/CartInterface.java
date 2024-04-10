package cart;

import java.util.ArrayList;

import member.Customer;
import performance.Performance;

public interface CartInterface {
	void printPerformanceList(ArrayList<Performance> pList); // ��ü ���� ���� ��� ���
//	void printCart(CartItem cartItem);

	boolean isCartInPerformance(String id,int quantity,String seatNum); // ��ٱ��Ͽ� ��� ������ �� ���� ���� �¼����� ����

	void insertPerformance(Performance p,int quantity,Customer nowUser,String seatNum); // CartItem�� ���� ������ ���

	void removeCart(int numId); // ��ٱ��� ���� numId�� �׸��� ����

	void deleteCart(); // ��ٱ����� ��� �׸��� ����
	
	void printCart();
//	void payPerformance(); //����(��ٱ��� ����, �������� ����Ʈ�� add�ϱ�)
	//(���� �ȸ� �¼� == ���¼� �̸� �����Ǿ� ������ �� �����ϴ�.) �ڲ�
	
	
	
	
	//������ ������ ���� �ȵ�
	// �켱 �¼� �������� Ƽ�� �����ڰ� ���������� �ɱ�(���߿� �ð��ִ� ����� 2���� �迭)
}
