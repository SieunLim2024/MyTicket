package cart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.UserDataHandler;

import main.Main;
import member.Customer;
import performance.Performance;

public class Cart implements CartInterface {

	public ArrayList<CartItem> cartItem = new ArrayList<>();
	public static int cartCount = 0;
	public static int totalcount = 0;// 구매한 항목 수

	public Cart() {
		super();
	}

	public Cart(ArrayList<CartItem> cartItem, ArrayList<CartItem> paymentItem) {
		super();
		this.cartItem = cartItem;
//		this.paymentItem=paymentItem;
	}

	// 카트 메뉴 선택
	public static int cartInfo() {
		System.out.println("==============================================");
		System.out.println("1.장바구니 항목 삭제 2.장바구니 모두 비우기");
		System.out.println("3.결제 하기 4.이전 메뉴로");
		System.out.println("==============================================");
		System.out.print("메뉴 선택>> ");
		int num = Main.sc.nextInt();
		Main.sc.nextLine();// 버퍼클리어
		return num;

	}

	public static void setCartToList() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("cart.txt"));

			String costomerID;
			int count = 0;

			while ((costomerID = reader.readLine()) != null) {
				String[] readCart = new String[CartItem.CARTIFONUM * Main.nowUser.getCartNum()];
				readCart[0] = costomerID;

				for (int i = 1; i < CartItem.CARTIFONUM * Main.nowUser.getCartNum(); i++) {
					readCart[i] = reader.readLine();
				}
				CartItem cart = new CartItem(readCart[0], readCart[1], readCart[2], Integer.parseInt(readCart[3]),
						Integer.parseInt(readCart[4]),readCart[5]);
				count++;
			} // end of while
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // end of catch
		} // end of finally

	}// end of setCartToList

	@Override
	public void printPerformanceList(ArrayList<Performance> pList) {
		for (int i = 0; i < pList.size(); i++) {
			Performance performance = pList.get(i);
			System.out.print(performance.getPerformanceID() + "|");
			System.out.print(performance.getGenre() + "|");
			System.out.print(performance.getDayOfPerformance() + "|");
			System.out.print(performance.getLimitAge() + "|");
			System.out.print(performance.getVenue() + "|");
			System.out.print(performance.getSoldSeats() + "/" + performance.getTotalSeats() + "|");
			System.out.print(performance.getSoldSeats() + "|");
			System.out.print(performance.getName());
			System.out.println("");
		}

	}

	@Override
	public boolean isCartInPerformance(String id, int quantity) {
		boolean flag = false;
		for (int i = 0; i < cartItem.size(); i++) {
			if (id.equals(cartItem.get(i).getPerformanceId())) {
				cartItem.get(i).setQuantity(cartItem.get(i).getQuantity() + quantity);
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public void insertPerformance(Performance p, int quantity, Customer nowUser,String seatNum) {
		CartItem pItem = new CartItem(p, quantity, nowUser,seatNum);
		cartItem.add(pItem);
		cartCount = cartItem.size();

	}

	@Override
	public void removeCart(int numId) {
		cartItem.remove(numId);
		cartCount = cartItem.size();
	}

	@Override
	public void deleteCart() {
		cartItem.clear();
		cartCount = 0;
	}

	@Override
	public void printCart() {
		System.out.println("==============================================");
		System.out.println("장바구니 등록 목록: ");
		System.out.println("----------------------------------------------");
		if (cartCount == 0) {
			System.out.println("장바구니가 비어있습니다.");
		} else {
			System.out.println("수량\t총액\t좌석 번호\t공연ID\t\t    공연명");
			for (int i = 0; i < cartItem.size(); i++) {
				System.out.print(cartItem.get(i).getQuantity() + "\t:");
				System.out.print(cartItem.get(i).getTotalPrice() + "\t:");
				System.out.print(cartItem.get(i).getSeatNum() + "\t:");
				System.out.print(cartItem.get(i).getPerformanceId() + "   :");
				System.out.print(cartItem.get(i).getPerformanceName());
				System.out.println("\t");
			}
		}
	}

	public static int printPayment(ArrayList<CartItem> list) {
		int totalPrice = 0;
		double discountRate = 0;// 할인율
		if (Main.nowUser.getGrade().equals("VIP")) {
			discountRate = 0.01;// vip면 할인 해줌
		}
		System.out.println("==============================================");
		System.out.println("영수증: ");
		System.out.println("----------------------------------------------");
		if (list.size() == 0) {
			System.out.println("아직 구매 내역이 없습니다.");
		} else {
			System.out.println("수량\t총액\t좌석 번호\t\t공연ID\t\t    공연명");
			for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i).getQuantity() + "\t:");
				System.out.print(list.get(i).getTotalPrice() + "\t:");
				System.out.print(list.get(i).getSeatNum() + "\t:");
				System.out.print(list.get(i).getPerformanceId() + "   :");
				System.out.print(list.get(i).getPerformanceName());
				System.out.println("\t");
				totalPrice += list.get(i).getTotalPrice();// 결제할 금액
				totalcount++;
			}
			System.out.println("----------------------------------------------");
			System.out.println("할인: " + (int) (totalPrice * discountRate));
			System.out.println("합계: " + (totalPrice - ((int) (totalPrice * discountRate))));// 최종 가격
			System.out.println("----------------------------------------------");
		}
		return totalPrice;
	}

	public static void printTotalPayment(ArrayList<CartItem> list,Customer nowUser) {
		System.out.println("==============================================");
		System.out.println("누적 구매 내역: ");
		System.out.println("----------------------------------------------");
		if (list.size() == 0) {
			System.out.println("아직 구매 내역이 없습니다.");
		} else {
			System.out.println("수량\t총액\t좌석 번호\t\t공연ID\t\t    공연명");
			for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i).getQuantity() + "\t:");
				System.out.print(list.get(i).getTotalPrice() + "\t:");
				System.out.print(list.get(i).getSeatNum() + "\t:");
				System.out.print(list.get(i).getPerformanceId() + "   :");
				System.out.print(list.get(i).getPerformanceName());
				System.out.println("\t");
			}
			System.out.println("----------------------------------------------");
			System.out.println("누적 구매 금액: " + nowUser.getAccumulatedPayment());
			System.out.println("----------------------------------------------");
		}
	}
}
