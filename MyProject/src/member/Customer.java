package member;

import java.util.Scanner;

public class Customer {
	private String id;// key
	private String pw;
	private String name;// compare
	private String phone;
	private String address;
	private int age;// 여기까지 회원가입

	private String grade;
	private int accumulatedPayment; // 누적결제금액, 등급 평가시 사용할 것
	private int mileage; // 기본 고객부터 마일리지 적립
	private int buyNum;//누적 구매 항목수(수량 x)
	private int cartNum;//
	
	public static final int CUSTOMERINFONUM=11;
	public String getName() {
		return name;
	}
	public Customer(String id, String pw,String name, String phone,  String address, int age) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.age = age;
		
		this.grade="Basic";
		this.accumulatedPayment=0;
		this.mileage=0;
		this.buyNum=0;
		this.cartNum=0;
	}
	public Customer( String id, String pw,String name, String phone, String address, int age, String grade,
			int accumulatedPayment, int mileage,int buyNum,int cartNum) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.age = age;
		this.grade = grade;
		this.accumulatedPayment = accumulatedPayment;
		this.mileage = mileage;
		this.buyNum=buyNum;
		this.cartNum=cartNum;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public int getAccumulatedPayment() {
		return accumulatedPayment;
	}
	public void setAccumulatedPayment(int accumulatedPayment) {
		this.accumulatedPayment = accumulatedPayment;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public int getCartNum() {
		return cartNum;
	}
	public void setCartNum(int cartNum) {
		this.cartNum = cartNum;
	}
	@Override
	public String toString() {
		return id +"\t"+name +"\t"+phone +"\t"+ address+"\t"+ age +"\t"+ grade +"\t"+ accumulatedPayment +"\t\t"+ mileage;
	}
}
