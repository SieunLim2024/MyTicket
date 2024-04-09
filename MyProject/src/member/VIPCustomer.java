package member;

public class VIPCustomer extends Customer {
	public static double discountRate=0.01;

	public VIPCustomer(String id, String pw, String name, String phone, String address, int age) {
		super(id, pw, name, phone, address, age);
	}

	public VIPCustomer(String id, String pw, String name, String phone, String address, int age, double discountRate) {
		super(id, pw, name, phone, address, age);
		this.discountRate = discountRate;
	}
	public VIPCustomer(String id, String pw, String name, String phone, String address, int age, String grade,
			int accumulatedPayment, int mileage, int buyNum, int cartNum) {
		super(id, pw, name, phone, address, age, grade, accumulatedPayment, mileage, buyNum, cartNum);
	}
	public VIPCustomer(String id, String pw, String name, String phone, String address, int age, String grade,
			int accumulatedPayment, int mileage, int buyNum, int cartNum, double discountRate) {
		super(id, pw, name, phone, address, age, grade, accumulatedPayment, mileage, buyNum, cartNum);
		this.discountRate = discountRate;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	

}
