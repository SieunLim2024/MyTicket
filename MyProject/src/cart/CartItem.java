package cart;

import member.Admin;
import member.Customer;
import performance.Performance;

public class CartItem {
	public static final int CARTIFONUM = 6;
	private Performance item;
	private String costomerID; // ������ id �߰��ϰ� cart�� id �� pw ������
	private String performanceId; // ����id
	private String performanceName; // ������
	private int quantity; // �ο���
	private int totalPrice; // �ѱ��űݾ�
	private String seatNum;	//�¼� ��ȣ

//Cart.insertPerformance���� ���
	public CartItem(Performance item, int quantity,Customer nowUser,String seatNum) {
		super();
		this.item = item;
		this.costomerID=nowUser.getId();
		this.performanceId=item.getPerformanceID();
		this.performanceName=item.getName();
		this.quantity=quantity;
		updateTotalPrice();
		this.seatNum=seatNum;
		updateSeat();
	}
	
	//��� ���� �޾Ƽ� ������(Admin.setWithoutUserPaymentList,Main.setPaymaentToList,Cart.setCartToList()���� ���
	public CartItem(String costomerID, String performanceId, String performanceName, int quantity, int totalPrice,String seatNum) {
		super();
		this.costomerID = costomerID;
		this.performanceId = performanceId;
		this.performanceName = performanceName;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.seatNum=seatNum;
	}
	public String getCostomerID() {
		return costomerID;
	}
	public void setCostomerID(String costomerID) {
		this.costomerID = costomerID;
	}
	public String getPerformanceName() {
		return performanceName;
	}
	public void setPerformanceName(String performanceName) {
		this.performanceName = performanceName;
	}
	public Performance getItem() {
		return item;
	}
	public void setItem(Performance item) {
		this.item = item;
	}
	public String getPerformanceId() {
		return performanceId;
	}
	public void setPerformanceId(String performanceId) {
		this.performanceId = performanceId;
	}
	 public String getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void updateTotalPrice() {
		totalPrice = this.item.getTicketPrice() * this.quantity;
	}
	
	//�� �ڸ��� �˰� ĥ������
	private void updateSeat() {
		
		
	}

}
