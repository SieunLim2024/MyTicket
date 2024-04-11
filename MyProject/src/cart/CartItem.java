package cart;

import member.Admin;
import member.Customer;
import performance.Performance;

public class CartItem {
	public static final int CARTIFONUM = 6;
	private Performance item;
	private String costomerID; // 구매자 id 추가하고 cart에 id 와 pw 제거함
	private String performanceId; // 공연id
	private String performanceName; // 공연명
	private int quantity; // 인원수
	private int totalPrice; // 총구매금액
	private String seatNum;	//좌석 번호

	//Cart.insertPerformance에서 사용
	public CartItem(Performance item, int quantity,Customer nowUser,String seatNum) {
		super();
		this.item = item;
		this.costomerID=nowUser.getId();
		this.performanceId=item.getPerformanceID();
		this.performanceName=item.getName();
		this.quantity=quantity;
		updateTotalPrice();
		this.seatNum=seatNum;
	}
	
	//모든 변수 받아서 생성함(Admin.setWithoutUserPaymentList,Main.setPaymaentToList,Cart.setCartToList()에서 사용
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
	

}