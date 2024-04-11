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

	public static ArrayList<CartItem> cartItem = new ArrayList<>();
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
		String input = Main.sc.nextLine().replaceAll("[^1-4]", "0");// 1~4 이외는 0, 메뉴에 0 없어야함
		int num = Integer.parseInt(input); // 형 변환
		return num;

	}

	// 전체 카트 파일에서 로그인 한 고객의 카트 정보만 찾아오기
	public void setCartToList(Customer nowUser) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("cart.txt"));

			String line;
			// 현재 고객의 아이디와 카트 파일에 아이디가 같다면...
			while ((line = reader.readLine()) != null) {
				if (line.contains(nowUser.getId())) {
					String[] readCart = new String[CartItem.CARTIFONUM];
					readCart[0] = line;
					for (int i = 1; i < CartItem.CARTIFONUM; i++) {
						readCart[i] = reader.readLine();
					}
					CartItem cart = new CartItem(readCart[0], readCart[1], readCart[2], Integer.parseInt(readCart[3]),
							Integer.parseInt(readCart[4]), readCart[5]);

					// 이미 카트에 항목 있으면 수량과 좌석 번호만 업데이트
					if (!isCartInPerformance(readCart[1], Integer.parseInt(readCart[3]), readCart[5])) {
						cartItem.add(cart); // 아니면 카트에 항목 추가
					} // end of if
				} // end of if
			} // end of while
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				cartCount=cartItem.size();
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
	public boolean isCartInPerformance(String id, int quantity, String seatNum) {
		boolean flag = false;
		for (int i = 0; i < cartItem.size(); i++) {
			if (id.equals(cartItem.get(i).getPerformanceId())) {// 카트리스트 안에 이미 해당 공연이 들어있다면...
				cartItem.get(i).setQuantity(cartItem.get(i).getQuantity() + quantity);// 수량만 증가
				cartItem.get(i).setSeatNum(cartItem.get(i).getSeatNum() + "," + seatNum);// 좌석 번호 추가해줌
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public void insertPerformance(Performance p, int quantity, Customer nowUser, String seatNum) {
		CartItem pItem = new CartItem(p, quantity, nowUser, seatNum);
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
			totalPrice -= ((int) (totalPrice * discountRate));// 할인 적용된 최종 가격
			System.out.println("----------------------------------------------");
			System.out.println("할인: " + (int) (totalPrice * discountRate));
			System.out.println("합계: " + totalPrice);
			System.out.println("----------------------------------------------");
		}
		return totalPrice;
	}

	public static void printTotalPayment(ArrayList<CartItem> list, Customer nowUser) {
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
	
	public void setWithoutUserCartList(Customer customer) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("Cart.txt"));
			String line;
			String[] read = new String[CartItem.CARTIFONUM];
			
			while ((line = reader.readLine()) != null) {
				read[0] = line;
				for (int i = 1; i < CartItem.CARTIFONUM; i++) {
					read[i] = reader.readLine();
				}
				CartItem cart = new CartItem(read[0], read[1], read[2], Integer.parseInt(read[3]),
						Integer.parseInt(read[4]), read[5]);
				if (!(read[0].equals(customer.getId()))) {
					Main.totalCartList.add(cart);
				}
			} // end of while

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}