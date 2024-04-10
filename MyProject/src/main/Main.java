package main;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.Collectors;

import cart.Cart;
import cart.CartItem;
import exception.CartException;
import member.Admin;
import member.Customer;
import performance.Performance;

public class Main {
	public static final int INFONUM = 10; // 회원 정보 항목 개수
	public static Scanner sc = new Scanner(System.in);
	public static ArrayList<Customer> userList = new ArrayList<Customer>();
	public static ArrayList<CartItem> paymentList = new ArrayList<>();
	public static ArrayList<CartItem> totalPaymentList = new ArrayList<>();
	public static Cart cart = new Cart();
	public static Cart payment = new Cart();
	static boolean checkLogin = false;
	static boolean duplicateID = false;
	public static Customer nowUser = null; // 로그인 한 계정 저장
	public static int nowUserIndex = 0; // 로그인 한 계정 저장

	public static void main(String[] args) {
		setUserToList(userList);
		Admin.setPerformanceToList();
		login();
		if (checkLogin) { // 로그인 성공시에 메인 메뉴로ss
			setPaymaentToList(totalPaymentList, nowUser);
			mainMenu(nowUser);
		}
	}

	// 로그인
	private static void login() {
		boolean quit = false;
		while (!quit) {
			int num = loginInfo();
			if (num < 1 || num > 4) {
				System.out.println("메뉴를 바르게 입력해주세요.");
				continue;
			} // end of if
			String[] menu = { "로그인", "회원가입", "관리자 로그인", "종료" };
			switch (menu[num - 1]) {
			case "로그인":
				nowUser = userLogin();
				if (checkLogin) {
					quit = true;
				}
				break;
			case "회원가입":
				joinMembership();
				break;
			case "관리자 로그인":
				Admin.adminLogin();
				break;
			case "종료":
				quit = true;
				System.out.println("종료합니다.");
				break;
			}// end of switch
		} // end of while
	}// end of login

	// 로그인 메뉴 안내
	private static int loginInfo() {
		System.out.println("==============================================");
		System.out.println("반갑습니다. KH 티켓 입니다.");
		System.out.println("----------------------------------------------");
		System.out.println("1.로그인 2.회원가입");
		System.out.println("3.관리자 로그인 4.종료");
		System.out.println("==============================================");

		System.out.print("메뉴 선택>> ");
		int num = sc.nextInt();
		sc.nextLine();// 버퍼클리어
		return num;

	}

	// 유저 로그인
	private static Customer userLogin() {
		System.out.println("유저 로그인");
		// 유저 로그인 성공시 checkLogin true로 변경
		System.out.print("아이디:");
		String inputId = sc.nextLine();
		System.out.print("비밀번호:");
		String inputPw = sc.nextLine();
		for (int i = 0; i < userList.size(); i++) {
			if (inputId.equals(userList.get(i).getId()) && inputPw.equals(userList.get(i).getPw())) {
				checkLogin = true;
				nowUserIndex = i;
				System.out.println("로그인 성공!");
				break;
			}
		}
		if (checkLogin == false) {
			System.out.println("로그인 실패");
		}
		return userList.get(nowUserIndex);
	}

	private static void setUserToList(ArrayList<Customer> userList) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("user.txt"));
			String userName;
			String[] readUser = new String[Customer.CUSTOMERINFONUM];
			while ((userName = reader.readLine()) != null) { // null이 아닐때까지 읽기
				readUser[0] = userName;
				for (int i = 1; i < Customer.CUSTOMERINFONUM; i++) {
					readUser[i] = reader.readLine();
				}
				Customer customer = new Customer(readUser[0], readUser[1], readUser[2], readUser[3], readUser[4],
						Integer.parseInt(readUser[5]), readUser[6], Integer.parseInt(readUser[7]),
						Integer.parseInt(readUser[8]), Integer.parseInt(readUser[9]), Integer.parseInt(readUser[10]));
				userList.add(customer);
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

	// 회원가입
	private static void joinMembership() {
		String[] writeCustomer = new String[Customer.CUSTOMERINFONUM];
//		String[] writeCart = new String[CartItem.CARTIFONUM];
		System.out.println("회원 가입 하시겠습니까? Y|N");
		String str = sc.nextLine();
		boolean quit = false;
		if (str.toUpperCase().equals("Y")) {
			while (!quit) {
				System.out.print("아이디: ");
				String id = sc.nextLine();
				duplicateID = serchId(id);
				if (duplicateID) {
					quit = true;
					break;
				}
				writeCustomer[0] = id;
				System.out.print("비밀번호: ");
				writeCustomer[1] = sc.nextLine();
				System.out.print("이름:");
				writeCustomer[2] = sc.nextLine();
				System.out.print("연락처:");
				writeCustomer[3] = sc.nextLine();
				System.out.print("주소: ");
				writeCustomer[4] = sc.nextLine();
				System.out.print("나이 (숫자 만): ");
				writeCustomer[5] = sc.nextLine();
				writeCustomer[6] = "Basic";// 회원 가입시 등급 무조건 Basic
				writeCustomer[7] = "0";// 회원 가입시 구매 총금액 무조건 0
				writeCustomer[8] = "0";// 회원 가입시 마일리지 무조건 0
				writeCustomer[9] = "0";// 회원 가입시 구매회수 무조건 0
				writeCustomer[10] = "0";// 회원 가입시 카트담아 둔 것 무조건 0
				FileWriter fw = null;
				try {
					// 새 고객 정보를 파일에 이어쓰기 위해 생성자에 true 옵션 설정
					fw = new FileWriter("user.txt", true);

					for (int i = 0; i < Customer.CUSTOMERINFONUM; i++) {
						fw.write(writeCustomer[i] + "\n");
					}
					System.out.println("회원가입이 완료 되었습니다!");
					quit = true;
				} catch (Exception e) {
					System.out.println(e);
				} finally {
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} // end of while
			userList.clear();
			setUserToList(userList);
		} else {
			System.out.println("회원을 추가하지 않습니다.");
			quit = true;
		}
	}// end of joinMembership

	private static boolean serchId(String id) {
		boolean flag = false;
		for (int i = 0; i < userList.size(); i++) {
			if (id.equals(userList.get(i).getId())) {
				System.out.println("이미 사용중인 아이디 입니다.");
				flag = true;
				break;
			} // end of if
		} // end of for
		if (flag == false) {
			System.out.println("사용 가능한 아이디 입니다.");
		}
		return flag;
	}

	// 메인 메뉴
	private static void mainMenu(Customer nowUser) {
		boolean quit = false;
		while (!quit) {
			int num = mainInfo();
			if (num < 1 || num > 4) {
				System.out.println("메뉴를 바르게 입력해주세요.");
				continue;
			} // end of if
			String[] menu = { "내 정보", "장바구니", "공연보기", "종료" };
			switch (menu[num - 1]) {
			case "내 정보":
				System.out.println("==============================================");
				System.out.println("내 정보");
				System.out.println("----------------------------------------------");
				System.out.println("아이디\t이름\t연락처\t\t주소\t\t나이\t등급\t누적결제금액\t마일리지");
				System.out.println(nowUser); // 회원 정보 출력
				Cart.printTotalPayment(totalPaymentList, nowUser);// 누적 구매 내역 출력
				break;
			case "장바구니":
				cart();
				break;
			case "공연보기":
				selectGenre(); // 장르 고르게 하기
				break;
			case "종료":
				quit = true;
				System.out.println("종료합니다.");
				break;
			}// end of switch
		} // end of while
	}// end of mainMenu

	private static void addCart(ArrayList<Performance> performanceList) {
//		Admin.performanceList.clear();//체크
//		Admin.setPerformanceToList();//체크
		boolean quit = false;
		while (!quit) {
			System.out.print("장바구니에 추가할 공연의 ID를 입력하세요 : ");
			String inputID = sc.nextLine();

			boolean flag = false; // 일치여부
			int numIndex = -1; // 인덱스 번호
			int numTicket = 0;

			for (int i = 0; i < performanceList.size(); i++) {
				if (inputID.equals(performanceList.get(i).getPerformanceID())) {
					numIndex = i;
					flag = true;
					break;
				}
			} // end of for

			// 일치하면 장바구니 추가 여부를 묻는다.
			if (flag) {
				System.out.println("장바구니에 추가하겠습니까? Y|N ");
				String str = sc.nextLine();
				if (str.toUpperCase().equals("Y")) {
					System.out.print("추가할 수량을 입력해주세요: ");
					numTicket = sc.nextInt();
					sc.nextLine();// 버퍼클리어

					// 잔여 좌석과 살 티켓 수량 비교
					if (Admin.performanceList.get(numIndex).getSoldSeats() + numTicket > Admin.performanceList
							.get(numIndex).getTotalSeats()) {
						System.out.println("잔여 좌석보다 구매 수량이 많습니다.");
					} else {

//						Performance.printSeats(Admin.performanceList, inputID);// 좌석 보여주기
						// 좌석 고르기
						String seatNum = askSeatNum(numTicket, inputID, numIndex);// 고른 좌석 받음.
						System.out.println(performanceList.get(numIndex).getName() + " 공연이 장바구니에 추가되었습니다.");
						System.out.println("선택한 좌석은 " + seatNum.toUpperCase() + " 입니다.");

						// 장바구니에 넣기이미 카트에 항목 있으면 수량만 업데이트
						if (!cart.isCartInPerformance(performanceList.get(numIndex).getPerformanceID(), numTicket,
								seatNum)) { // 이미 카트에 항목 있으면 수량만 업데이트
							cart.insertPerformance(performanceList.get(numIndex), numTicket, nowUser, seatNum); // 아니면
																												// 카트에
																												// 항목 추가
						} // end of if
					} // end of else if(수량이 잔여 좌석 내인지...)

				} // end of if(Y인지 검사)
				quit = true;
			} else {
				System.out.println("다시 입력해주세요");
			}
		}
	}

	private static String askSeatNum(int numTicket, String inputID, int numIndex) {
		StringBuffer seatNum = new StringBuffer();// 수량 만큼 문자 수정해야해서 String 대신 StringBuffer
		boolean flag = false;// 이미 고른 좌석인지 검사
		for (int i = 0; i < numTicket; i++) {// 수량 만큼 반복문 돌리기
			Performance.printSeats(Admin.performanceList, inputID);
			while (!flag) {
				System.out.print("좌석 번호를 골라주세요 (ex:1A): ");
				String input = sc.nextLine();
				seatNum.append(input);
				String[] seatYX = input.split("");// seatYX[0]은 열번호 seatYX[1]은 행 번호
				String[][] seats = Admin.performanceList.get(numIndex).getSeats();// 좌석 받아옴
				String xNum = seatYX[1].toUpperCase();// 일단 대문자로 바꿔줌
				if (seats[Integer.parseInt(seatYX[0].toString())-1][((int) (xNum.charAt(0))) - 65].equals("■")) {
					System.out.println("이미 선택된 좌석입니다.");
					seatNum=null;
					seatNum = new StringBuffer();//StringBuffer 초기화
					
				}else {
					System.out.println("선택 가능한 좌석입니다.");
					flag=true;
					break;
				}
			}
			if (i != numTicket - 1) {
				seatNum.append(",");
			}
		}
		return seatNum.toString();
	}

	// 공연 파일 읽어 공연 개수 계산 함
	private static int countPerformance() {
		try {
			FileReader fr = new FileReader("performance.txt");
			BufferedReader reader = new BufferedReader(fr);
			String str;
			int num = 0; // 공연 개수
			while ((str = reader.readLine()) != null) {
				if (str.contains("pID")) {
					++num;
				}
			}
			reader.close();
			fr.close();
			return num;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	private static void selectGenre() {
		ArrayList<Performance> showList = new ArrayList<Performance>();

		showList.addAll(Admin.performanceList); // 깊은 복사해둠.
		boolean quit = false;
		while (!quit) {

			int num = GenreInfo();
			if (num < 1 || num > 5) {
				System.out.println("메뉴를 바르게 입력해주세요.");
				continue;
			} // end of if
			String[] menu = { "전체 보기", "뮤지컬", "연극", "콘서트", "이전 화면으로" };
			switch (menu[num - 1]) {
			case "전체 보기":
				Performance.printPerformance(showList);
//				고른거 보여주고...
				sortOrCart(showList);
				break;
			case "뮤지컬":
				ArrayList<Performance> showMList = null;
				showMList = (ArrayList<Performance>) showList.stream().filter(s -> s.getGenre().equals("뮤지컬"))
						.collect(Collectors.toList());
				Performance.printPerformance(showMList);
//				고른거 보여주고...
				sortOrCart(showMList);
				break;
			case "연극":
				ArrayList<Performance> showPList = null;
				showPList = (ArrayList<Performance>) showList.stream().filter(s -> s.getGenre().equals("연극"))
						.collect(Collectors.toList());
				Performance.printPerformance(showPList);
//				고른거 보여주고...
				sortOrCart(showPList);
				break;
			case "콘서트":
				ArrayList<Performance> showCList = null;
				showCList = (ArrayList<Performance>) showList.stream().filter(s -> s.getGenre().equals("콘서트"))
						.collect(Collectors.toList());
				Performance.printPerformance(showCList);
//				고른거 보여주고...
				sortOrCart(showCList);
				break;
			case "이전 화면으로":
				quit = true;
				System.out.println("종료합니다.");
				showList.clear();// 초기화
				break;
			}// end of switch
		} // end of while
	}

	private static void sortOrCart(ArrayList<Performance> showList) {
//		ArrayList<Performance> sortList= new ArrayList<Performance>();
//		sortList.addAll(showList); //깊은 복사해둠.
		boolean quit = false;
		while (!quit) {
			// print
			int num = sortOrCartInfo();
			if (num < 1 || num > 4) {
				System.out.println("메뉴를 바르게 입력해주세요.");
				continue;
			} // end of if
			String[] menu = { "가격 낮은 순", "가격 높은 순", "장바구니 담기", "이전 메뉴로" };
			switch (menu[num - 1]) {
			case "가격 낮은 순":
				ArrayList<Performance> showLowList = new ArrayList<Performance>();
				showLowList.addAll(showList); // 깊은 복사해둠.
				Collections.sort(showLowList);
				Performance.printPerformance(showLowList);
				break;
			case "가격 높은 순":
				ArrayList<Performance> showHighList = new ArrayList<Performance>();
				showHighList.addAll(showList); // 깊은 복사해둠.
				Collections.sort(showHighList, Comparator.reverseOrder());
				Performance.printPerformance(showHighList);
				break;
			case "장바구니 담기":
				askaddCart();
				break;
			case "이전 메뉴로":
				quit = true;
				System.out.println("이전 메뉴로 돌아갑니다.");
				break;
			}// end of switch
		} // end of while
	}

	private static int sortOrCartInfo() {
		System.out.println("==============================================");
		System.out.println("원하시는 메뉴 선택해주세요.");
		System.out.println("----------------------------------------------");
		System.out.println("1.가격 낮은 순 2.가격 높은 순");
		System.out.println("3.장바구니 담기 4.이전 메뉴로");
		System.out.println("==============================================");

		System.out.print("메뉴 선택>> ");
		int num = sc.nextInt();
		sc.nextLine();// 버퍼클리어
		return num;
	}

	private static void askaddCart() {
		countPerformance();
		addCart(Admin.performanceList);
//		}
	}

	private static int GenreInfo() {
		System.out.println("==============================================");
		System.out.println("원하시는 장르를 선택해주세요.");
		System.out.println("----------------------------------------------");
		System.out.println("1.전체 보기 2.뮤지컬");
		System.out.println("3.연극 4.콘서트");
		System.out.println("5.이전 화면으로");
		System.out.println("==============================================");

		System.out.print("메뉴 선택>> ");
		int num = sc.nextInt();
		sc.nextLine();// 버퍼클리어
		return num;
	}

	// 메인 메뉴 선택
	private static int mainInfo() {
		System.out.println("==============================================");
		System.out.println("환영합니다. KH 티켓 입니다.");
		System.out.println("----------------------------------------------");
		System.out.println("1.내 정보 2.장바구니");
		System.out.println("3.공연보기 4.종료");
		System.out.println("==============================================");

		System.out.print("메뉴 선택>> ");
		int num = sc.nextInt();
		sc.nextLine();// 버퍼클리어
		return num;
	}// end of mainInfo

	// 메인 메뉴에서 카트 선택시 ...
	private static void cart() {
		boolean quit = false;
		while (!quit) {
			cart.printCart();
			int num = Cart.cartInfo();
			if (num < 1 || num > 4) {
				System.out.println("메뉴를 바르게 입력해주세요.");
				continue;
			} // end of if
			String[] menu = { "장바구니 항목 삭제", "장바구니 모두 비우기", "결제 하기", "이전 메뉴로" };
			switch (menu[num - 1]) {
			case "장바구니 항목 삭제":
				try {
					cartRemoveItem();
				} catch (CartException e1) {
					e1.printStackTrace();
				}
				break;
			case "장바구니 모두 비우기":
				try {
					cartClear();
				} catch (CartException e) {
//					e.printStackTrace();
					System.out.println(e.getMessage());

				}
				break;
			case "결제 하기":
				buy();
				break;
			case "이전 메뉴로":
				quit = true;
				System.out.println("이전 메뉴로 돌아갑니다.");
				break;
			}// end of switch
		} // end of while
	}// end of cart

	private static void buy() {
		int count = 0;// 구매 개수
		System.out.println("장바구니에 모든 항목을 결제합니다.");
		System.out.println("결제하시겠습니까? Y|N ");
		String str = sc.nextLine();
		if (str.toUpperCase().equals("Y")) {
			paymentList.addAll(cart.cartItem);
			System.out.println("감사합니다. 결제가 완료 되었습니다.");
			try {
				cartClear();
			} catch (CartException e) {
				e.printStackTrace();
			}
		} // end of it

		int price = payment.printPayment(paymentList);

		nowUser.setAccumulatedPayment(nowUser.getAccumulatedPayment() + price);// 누적 구매액 수정
		nowUser.setMileage((int) (nowUser.getMileage() + price * 0.01));// 마일리지 1%씩 적립
		nowUser.setBuyNum(nowUser.getBuyNum() + payment.totalcount);// 누적 구매 항목수 수정
		if (nowUser.getAccumulatedPayment() > 150000) {
			nowUser.setGrade("VIP");
		}
		Admin.saveUserToFile();// 변경된 내용이 있으므로 저장

		for (int j = 0; j < paymentList.size(); j++) {
			for (int i = 0; i < Admin.performanceList.size(); i++) {// 공연리스트에 들어가 있는 것 만큼 돌려서
				if (Admin.performanceList.get(i).getPerformanceID().equals(paymentList.get(j).getPerformanceId())) {// 구매한
																													// 것과
																													// 같은
																													// 공연Id이
																													// 있다면..
					Admin.performanceList.get(i).setSoldSeats(
							Admin.performanceList.get(i).getSoldSeats() + paymentList.get(j).getQuantity());

					String[][] updateSeats = Admin.performanceList.get(i).getSeats();// 기존 리스트에 좌석 2차원 배열 가져옴
					String[] seatNumSplit = paymentList.get(j).getSeatNum().split(",");// 좌석 별로 끊어서 배열 생성
					for (int k = 0; k < seatNumSplit.length; k++) {
						String[] seatsYX = seatNumSplit[k].split("");// seatsYX[0]에 열 번호 seatsYX[1]에 행 번호
						String xNum = seatsYX[1].toUpperCase();// 일단 대문자로 바꿔줌
						updateSeats[Integer.parseInt(seatsYX[0].toString())-1][((int) (xNum.charAt(0))) - 65] = "■";// 아스키코드로
																													// 바꿔서
																													// A->1
						Admin.performanceList.get(i).setSeats(updateSeats);
					}
				}
			}
		}
		Admin.savePerformanceFile(Admin.performanceList);// 파일에 다시 저장
		Admin.performanceList.clear();// 초기화
		System.out.println("초기화 완료!");
		Admin.setPerformanceToList();// 팔린 티켓 수량 반영
		System.out.println("리스트 로딩 완료!");

		writePaymentFile(paymentList);
		System.out.println("구매 내역 작성 완료!");
		payment.totalcount = 0;// 초기화
		paymentList.clear();// 초기화
		totalPaymentList.clear();// 초기화
		setPaymaentToList(totalPaymentList, nowUser);// 다시 저장
	}

	private static void cartRemoveItem() throws CartException {
		if (cart.cartCount == 0) {
			throw new CartException("장바구니 항목이 없습니다.");
		} else {
			if (cart.cartCount >= 0) {
				cart.printCart();
			}
			boolean quit = false;
			while (!quit) {
				System.out.print("장바구니에서 삭제할 공연의 ID를 입력하세요 :");
				Scanner input = new Scanner(System.in);
				String str = input.nextLine();
				boolean flag = false;
				int numId = -1;

				for (int i = 0; i < cart.cartCount; i++) {
					if (str.equals(cart.cartItem.get(i).getPerformanceId())) {
						numId = i;
						flag = true;
						break;
					}
				}
				if (flag) {
					System.out.println("장바구니에서 삭제하겠습니까? Y|N ");
					str = input.nextLine();
					if (str.toUpperCase().equals("Y")) {
						System.out.println(cart.cartItem.get(numId).getPerformanceName() + " 장바구니에서 공연이 삭제 되었습니다.");
						cart.removeCart(numId);
					}
					quit = true;
				} else {
					System.out.println("다시 입력해 주세요");
				}
			}
		}
	}

	// 카트 모두 비우기
	private static void cartClear() throws CartException {
		if (cart.cartCount == 0) {
			throw new CartException("장바구니에 항목이 없습니다.");
		} else {
			System.out.println("장바구니에 모든 항목을 삭제 하겠습니까? Y|N ");
			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			if (str.toUpperCase().equals("Y")) {
				System.out.println("장바구니에 모든 항목을 삭제했습니다.");
				cart.deleteCart();
			}
		} // end of if else
	}// end of cartClear

	private static void writePaymentFile(ArrayList<CartItem> list) {
		String[] writePayment = new String[CartItem.CARTIFONUM];

		for (int i = 0; i < list.size(); i++) {
			writePayment[0] = list.get(i).getCostomerID() + "\n";
			writePayment[1] = list.get(i).getPerformanceId() + "\n";
			writePayment[2] = list.get(i).getPerformanceName() + "\n";
			writePayment[3] = list.get(i).getQuantity() + "\n";
			writePayment[4] = list.get(i).getTotalPrice() + "\n";
			writePayment[5] = list.get(i).getSeatNum() + "\n";
		}
		FileWriter fw = null;
		try {
			// 새 고객 정보를 파일에 이어쓰기 위해 생성자에 true 옵션 설정
			fw = new FileWriter("payment.txt", true);

			for (int i = 0; i < CartItem.CARTIFONUM; i++) {
				fw.write(writePayment[i]);
			}
			System.out.println("구매 내역이 파일에 저장되었습니다.");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}// end of writePaymentFile()

	// 파일에서 로그인 한 사람의 구매 내역만 뽑아서 list에 넣음
	public static void setPaymaentToList(ArrayList<CartItem> totalPaymentList, Customer nowUser) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("payment.txt"));
			String line;
			String[] read = new String[CartItem.CARTIFONUM];

			while ((line = reader.readLine()) != null) {
				if (line.contains(nowUser.getId())) {
					read[0] = line;
					for (int i = 1; i < CartItem.CARTIFONUM; i++) {
						read[i] = reader.readLine();
					}
					CartItem payment = new CartItem(read[0], read[1], read[2], Integer.parseInt(read[3]),
							Integer.parseInt(read[4]), read[5]);
					totalPaymentList.add(payment);
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
	}// end of setPaymentToList
}
