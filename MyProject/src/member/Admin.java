package member;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import cart.Cart;
import cart.CartItem;
import main.Main;
import performance.Performance;

public class Admin {
	public static Scanner sc = new Scanner(System.in);
	private static boolean adminLogin = false;
	public static final int PERINFONUM = 9;
	public static ArrayList<Performance> performanceList = new ArrayList<Performance>();
	public static ArrayList<Customer> CustomerList = new ArrayList<Customer>();
	public static ArrayList<CartItem> withoutUserPaymentList = new ArrayList<CartItem>();
	static boolean removeUserflag = false;
	static boolean removePayflag = false;
	static boolean removeflag = false;

	// 관리자 로그인
	public static void adminLogin() {
		BufferedReader reader = null;
		String id = null;
		String pw = null;
		try {
			reader = new BufferedReader(new FileReader("admin.txt"));
				id = reader.readLine();
				pw = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("관리자 정보를 입력하세요");

		System.out.print("아이디: ");
		String adminId = sc.nextLine();
		System.out.print("비밀번호: ");
		String adminPW = sc.nextLine();

		Admin admin = new Admin();
		if (adminId.equals(id) && adminPW.equals(pw)) {

			System.out.println("관리자 로그인에 성공 하였습니다.");
			adminLogin = true;
			adminMenu();// 관리자 메뉴로
		} else {
			System.out.println("아이디나 비밀번호가 맞지 않습니다.");
		}

	}

	// 관리자 메뉴
	public static void adminMenu() {
		boolean quit = false;
		while (!quit) {
			int num = adminMenuInfo();
			if (num < 1 || num > 5) {
				System.out.println("메뉴를 바르게 입력해주세요.");
				continue;
			} // end of if
			String[] menu = { "공연 추가", "공연 삭제", "회원 출력", "회원 삭제", "종료" };
			switch (menu[num - 1]) {
			case "공연 추가":
				addPerformance();
				break;
			case "공연 삭제":
				deletePerformance();
				break;
			case "회원 출력":
				printCustomer();
				break;
			case "회원 삭제":
				deleteUser();
				break;
			case "종료":
				quit = true;
				System.out.println("관리자 메뉴를 종료합니다.");
				break;

			}// end of switch
		} // end of while
	}// end of admiMenu

	// 회원 삭제
	private static void deleteUser() {
		searchUser();
		// 다시 파일로 바꿔서 저장
		if (removeUserflag == true) {
			saveUserToFile();
		}

	}

	// 고객 파일 덮어씌우기
	public static void saveUserToFile() {
		FileWriter fw = null;
		try {
			// 공연 파일 덮어씌우기
			fw = new FileWriter("user.txt");
			for (int i = 0; i < Main.userList.size(); i++) {
				fw.write(Main.userList.get(i).getId() + "\n");
				fw.write(Main.userList.get(i).getPw() + "\n");
				fw.write(Main.userList.get(i).getName() + "\n");
				fw.write(Main.userList.get(i).getPhone() + "\n");
				fw.write(Main.userList.get(i).getAddress() + "\n");
				fw.write(Main.userList.get(i).getAge() + "\n");
				fw.write(Main.userList.get(i).getGrade() + "\n");
				fw.write(Main.userList.get(i).getAccumulatedPayment() + "\n");
				fw.write(Main.userList.get(i).getMileage() + "\n");
				fw.write(Main.userList.get(i).getBuyNum() + "\n");
				fw.write(Main.userList.get(i).getCartNum() + "\n");

			}

			System.out.println("고객 파일이 저장 되었습니다.");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 유저 검색
	private static void searchUser() {
		if (Main.userList.size() == 0) {
			System.out.println("저장된 고객이 없습니다.");
		} else {
			boolean quit = false;
			while (!quit) {
				System.out.print("삭제할 고객 계정의 ID를 입력하세요 :");
				String inputID = sc.nextLine();
				boolean flag = false;
				int idIndex = -1;

				for (int i = 0; i < Main.userList.size(); i++) {
					if (inputID.equals(Main.userList.get(i).getId())) {
						idIndex = i;
						flag = true;
						break;
					} // end of if
				} // end of for
				if (flag) {
					System.out.println("해당 계정을 찾았습니다.");

				} else {
					System.out.println("해당 계정을 찾지 못했습니다.");
					quit = true;
					break;
				}
				boolean removeflag = false;

				removeflag = removeCustomer(idIndex, removeflag);
				if (removeflag) {
					System.out.println("계정이 삭제 되었습니다.");
					quit = true;
					removeUserflag = true;
				} else {
					System.out.println("계정 삭제가 취소되었습니다.");
					quit = true;
				} // end of else if

			} // end of while
		} // end of else if

	}

	// 유저 삭제
	private static boolean removeCustomer(int idIndex, boolean flag) {
		System.out.println("ID\t이름\t연락처\t\t주소\t\t나이\t등급\t누적결제금액\t마일리지");
		System.out.println(Main.userList.get(idIndex).toString());
		System.out.println("해당 계정을 삭제하겠습니까? Y|N ");
		String str = sc.nextLine();
		if (str.toUpperCase().equals("Y")) {
			boolean removePaymentflag = false;

			removePaymentflag = removePayment(idIndex, removePaymentflag);// 계정 삭제 전에 구매 내역도 제거해줌
			if (removePaymentflag) {
				System.out.println("해당 계정의 구매 내역이 삭제 되었습니다.");
				removeUserflag = true;
			} else {
				System.out.println("해당 계정의 구매 내역 삭제가 실패했습니다.");
			} // end of else if

			Main.userList.remove(idIndex);
			flag = true;
		} else {
			flag = false;
		} // end of else if
		return flag;
	}

	// (탈퇴하는 회원의) 구매 내역 삭제
	private static boolean removePayment(int idIndex, boolean flag) {
		Main.setPaymaentToList(Main.totalPaymentList, Main.userList.get(idIndex));// 일단 총 리스트 만듬...
		Cart.printTotalPayment(Main.totalPaymentList, Main.userList.get(idIndex));// 삭제될 내용 보여줌
		System.out.println("위 내역을 삭제 합니다.");
		Main.totalPaymentList.clear();// 다 보여줬으니 비워줌
		// 보여준 부분 제외한 리스트 만들어야함
		setWithoutUserPaymentList(Main.userList.get(idIndex));
		// 그걸을 파일에 저장
		savePaymentFile(withoutUserPaymentList);
		// 리스트 초기화(다시 넣어줄 필요는 없음)
		withoutUserPaymentList.clear();
		flag = true;

		return flag;
	}

	// 결제 내역 파일 덮어씌우기
	private static void savePaymentFile(ArrayList<CartItem> list) {
		FileWriter fw = null;
		try {
			// 결제 내역 파일 덮어씌우기
			fw = new FileWriter("payment.txt");
			for (int i = 0; i < list.size(); i++) {
				fw.write(list.get(i).getCostomerID() + "\n");
				fw.write(list.get(i).getPerformanceId() + "\n");
				fw.write(list.get(i).getPerformanceName() + "\n");
				fw.write(list.get(i).getQuantity() + "\n");
				fw.write(list.get(i).getSeatNum() + "\n");
				fw.write(list.get(i).getTotalPrice() + "\n");
			}

			System.out.println("구매 내역 파일이 저장 되었습니다.");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 받은 고객외 다른 고객들의 결제 내역 리스트로
	private static void setWithoutUserPaymentList(Customer customer) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("payment.txt"));
			String line;
			String[] read = new String[CartItem.CARTIFONUM];
			while ((line = reader.readLine()) != null) {
				read[0] = line;
				for (int i = 1; i < CartItem.CARTIFONUM; i++) {
					read[i] = reader.readLine();
				}
				CartItem payment = new CartItem(read[0], read[1], read[2], Integer.parseInt(read[3]),
						Integer.parseInt(read[4]), read[5]);
				if (!(read[0].equals(customer.getId()))) {
					withoutUserPaymentList.add(payment);
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

	// 관리자 메뉴 안내
	public static int adminMenuInfo() {
		System.out.println("==============================================");
		System.out.println("반갑습니다. KH 티켓 관리자 메뉴 입니다.");
		System.out.println("----------------------------------------------");
		System.out.println("1.공연 추가 2.공연 삭제");
		System.out.println("3.회원 출력 4.회원 삭제");
		System.out.println("5.종료");
		System.out.println("==============================================");

		System.out.print("메뉴 선택>> ");
		String input = sc.nextLine().replaceAll("[^1-5]", "0");// 1~4 이외는 0, 메뉴에 0 없어야함
		int num = Integer.parseInt(input); // 형 변환
		return num;

	}

	// 공연 추가
	private static void addPerformance() {
		boolean ageFlag = false;
		boolean seatFlag = false;
		boolean priceFlag = false;
		boolean genreFlag = false;
		boolean yFlag = false;
		if (adminLogin = true) {
			String[] writePerformance = new String[Performance.PERIFONUM];
			System.out.println("공연 정보를 추가하겠습니까? Y|N");
			String str = sc.nextLine();

			if (str.toUpperCase().equals("Y")) {
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("YYMMDDHHmmss");
				String strDate = formatter.format(date);
				// 공연 id는 추가하는 시간(초단위까지)일치 하지 않으면 중복되지 않음
				writePerformance[0] = "pID" + strDate;
				System.out.println("공연ID: " + writePerformance[0]);
				System.out.print("공연명 : ");
				writePerformance[1] = sc.nextLine();
				// -----------------------------------------------------------
				while (!genreFlag) {
					System.out.println("[뮤지컬, 연극, 콘서트]");
					System.out.print("장르 :");
					String input = sc.nextLine();

					if (!(input.equals("뮤지컬") || input.equals("연극") || input.equals("콘서트"))) {// 장르 바르게입력하지 않으면
						System.out.println("장르를 바르게 입력해주세요.");
						continue;
					}
					writePerformance[2] = input;
					genreFlag = true;
				}
				// -----------------------------------------------------------
				System.out.print("공연일 예)2024-01-01 :");
				writePerformance[3] = sc.nextLine();
				System.out.print("공연 장소: ");
				writePerformance[4] = sc.nextLine();
				// -----------------------------------------------------------
				while (!ageFlag) {
					System.out.print("관람제한연령 (숫자 만): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// 숫자 이외 공백 처리

					if (input.length() == 0) {// 숫자를 한번도 입력하지 않으면
						input = "0";// null 방지 (사실 필요 없으니 보험삼아)
						System.out.println("숫자만 입력해주세요.");
						continue;
					}
					writePerformance[5] = input;
					ageFlag = true;
				}
				// -----------------------------------------------------------
				while (!seatFlag) {
					System.out.print("총 좌석수 (숫자만): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// 숫자 이외 공백 처리

					if (input.length() == 0) {// 숫자를 한번도 입력하지 않으면
						input = "0";
						System.out.println("숫자만 입력해주세요.");
						continue;
					}
					if (input.equals("0")) {
						System.out.println("0석 미만은 입력이 불가합니다.");
						continue;
					} else {
						writePerformance[6] = input;
						seatFlag = true;
					}
				}
				// -----------------------------------------------------------
				writePerformance[7] = "0"; // 공연 추가시 판매좌석수 무조건 0으로
				// -----------------------------------------------------------
				while (!priceFlag) {
					System.out.print("티켓 가격 (숫자만): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// 숫자 이외 공백 처리

					if (input.length() == 0) {// 숫자를 한번도 입력하지 않으면
						input = "0";
						System.out.println("숫자만 입력해주세요.");
						continue;
					}
					if (input.equals("0")) {
						System.out.println("0원 미만은 입력이 불가합니다.");
						continue;
					} else {
						writePerformance[8] = input;
						priceFlag = true;
					}
				}
				// -----------------------------------------------------------
				while (!yFlag) {
					System.out.print("공연관 좌석 열(숫자만): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// 숫자 이외 공백 처리

					if (input.length() == 0) {// 숫자를 한번도 입력하지 않으면
						input = "0";
						System.out.println("숫자만 입력해주세요.");
						continue;
					}
					if (input.equals("0")) {
						System.out.println("0열 미만은 입력이 불가합니다.");
						continue;
					} else {
						writePerformance[10] = input;
						yFlag = true;
					}
				}
				// -----------------------------------------------------------
				int total = Integer.parseInt(writePerformance[6]);// 총 좌석수
				int y = Integer.parseInt(writePerformance[10]); // 좌석 열 수
				int x = 0;// 좌석 행 수
				int unable = 0;// 앉을 수 없는 좌석 수
				// 나머지가 있으면 한칸 늘려주기
				if (total % y > 0) {
					writePerformance[10] = y + "";// int->String
					unable = total % y;

					x = (int) (total / y) + 1;
					writePerformance[11] = x + "";// int->String
				} else {
					x = (int) (total / y);
					writePerformance[11] = x + "";// int->String
				}
				// -----------------------------------------------------------
				StringBuffer seat = new StringBuffer(); // 수정 횟수가 많으므로 String 대신 StringBuffer 사용
				for (int i = 0; i < Integer.parseInt(writePerformance[6]); i++) {
					seat.append("□");// 총 좌석수 만큼 빈자리 만들어준다.
				}
				for (int i = 0; i < unable; i++) {
					seat.append("x");// 앉을 수 없는 자리는 x로 표시
				}
				writePerformance[9] = seat.toString();
				// -----------------------------------------------------------
				FileWriter fw = null;
				try {
					// 새 공연 정보를 파일에 이어쓰기 위해 생성자에 true 옵션 설정
					fw = new FileWriter("performance.txt", true);

					for (int i = 0; i < Performance.PERIFONUM; i++) {
						fw.write(writePerformance[i] + "\n");
					}

					System.out.println("새 공연 저장 되었습니다.");
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
				performanceList.clear();// 변경 사항 있으므로 초기화
				setPerformanceToList();// 다시 저장

			} else {
				System.out.println("공연을 추가하지 않습니다.");
			}
		} else {
			System.out.println("관리자 로그인이 필요합니다.");
		}

	}

	// 공연 삭제
	private static void deletePerformance() {
		// 파일->리스트
		System.out.println("==============================================");
		System.out.println("현재 공연 목록");
		System.out.println("----------------------------------------------");
		// 현재 존재하는 공연(요약본)출력
		printPerformanceList(performanceList);
		// 공연 아이디로 검색
		searchID();
		// 공연 삭제 되었다면 다시 파일로 바꿔서 저장
		if (removeflag == true) {
			savePerformanceFile(performanceList);
		}
	}

	// 공연 파일 덮어씌우기
	public static void savePerformanceFile(ArrayList<Performance> performanceList) {
		FileWriter fw = null;
		try {
			// 공연 파일 덮어씌우기
			fw = new FileWriter("performance.txt");
			for (int i = 0; i < performanceList.size(); i++) {
				fw.write(performanceList.get(i).getPerformanceID() + "\n");
				fw.write(performanceList.get(i).getName() + "\n");
				fw.write(performanceList.get(i).getGenre() + "\n");
				fw.write(performanceList.get(i).getDayOfPerformance() + "\n");
				fw.write(performanceList.get(i).getVenue() + "\n");
				fw.write(performanceList.get(i).getLimitAge() + "\n");
				fw.write(performanceList.get(i).getTotalSeats() + "\n");
				fw.write(performanceList.get(i).getSoldSeats() + "\n");
				fw.write(performanceList.get(i).getTicketPrice() + "\n");

				for (int k = 0; k < performanceList.get(i).getYseats(); k++) {
					for (int l = 0; l < performanceList.get(i).getXseats(); l++) {
						fw.write(performanceList.get(i).getSeats()[k][l]);
					}
				}

				fw.write("\n");// 줄 바꿈
				fw.write(performanceList.get(i).getYseats() + "\n");
				fw.write(performanceList.get(i).getXseats() + "\n");
			}

			System.out.println("공연 파일이 저장 되었습니다.");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 공연(요약본)출력
	private static void printPerformanceList(ArrayList<Performance> list) {
		System.out.println("공연ID\t\t  공연일\t\t장르\t공연명");
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getPerformanceID() + "   ");
			System.out.print(list.get(i).getDayOfPerformance() + "\t");
			System.out.print(list.get(i).getGenre() + "\t");
			System.out.print(list.get(i).getName() + "\n");
		} // end of for
	}// end of printPerformanceList

	// 아이디로 검색
	private static void searchID() {
		if (performanceList.size() == 0) {
			System.out.println("저장된 공연이 없습니다.");
		} else {
			boolean quit = false;
			while (!quit) {
				System.out.print("삭제할 공연의 ID를 입력하세요 :");
				String inputID = sc.nextLine();
				boolean flag = false;
				int idIndex = -1;

				for (int i = 0; i < performanceList.size(); i++) {
					if (inputID.equals(performanceList.get(i).getPerformanceID())) {
						idIndex = i;
						flag = true;
					} // end of if
				} // end of for
				if (flag) {
					System.out.println("해당 공연을 찾았습니다.");
				} else {
					System.out.println("해당 공연을 찾지 못했습니다.");
					continue;
				}

				removeflag = removePerformance(idIndex, removeflag);
				if (removeflag) {
					System.out.println("공연이 삭제 되었습니다.");
					quit = true;
				} else {
					System.out.println("공연 삭제가 취소되었습니다.");
					quit = true;
				} // end of else if

			} // end of while
		} // end of else if

	}

	// 공연 삭제
	private static boolean removePerformance(int idIndex, boolean flag) {
		System.out.println(performanceList.get(idIndex).toString());
		System.out.println("해당 공연을 삭제하겠습니까? Y|N ");
		String str = sc.nextLine();
		if (str.toUpperCase().equals("Y")) {
			performanceList.remove(idIndex);
			flag = true;
		} else {
			flag = false;
		} // end of else if
		return flag;

	}// end of removePerformance

	// 공연 파일에 저장된 내용 리스트로...
	public static void setPerformanceToList() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("performance.txt"));
			String performanceID;
			String[] readUser = new String[Performance.PERIFONUM];

			String[][] setSeat;// 좌석 2차원 배열로

			while ((performanceID = reader.readLine()) != null) {
				readUser[0] = performanceID;
				for (int i = 1; i < Performance.PERIFONUM; i++) {
					readUser[i] = reader.readLine();
				}
				String[] seatsSplit = new String[Integer.parseInt(readUser[6])];// 여기에 끊어서 하나씩 넣어줄거임(총 좌석수로 크기 잡음)
				seatsSplit = readUser[9].split("");// 한줄로 된거 하나씩 끊음
				setSeat = new String[Integer.parseInt(readUser[10])][Integer.parseInt(readUser[11])];
				int index = 0;
				for (int j = 0; j < Integer.parseInt(readUser[10]); j++) {
					for (int k = 0; k < Integer.parseInt(readUser[11]); k++) {
						setSeat[j][k] = seatsSplit[index];
						index++;
					}
				}
				Performance performance = new Performance(readUser[0], readUser[1], readUser[2], readUser[3],
						readUser[4], Integer.parseInt(readUser[5]), Integer.parseInt(readUser[6]),
						Integer.parseInt(readUser[7]), Integer.parseInt(readUser[8]), setSeat,
						Integer.parseInt(readUser[10]), Integer.parseInt(readUser[11]));
				performanceList.add(performance);
				// 배열로 만들어주기

//				System.out.println(performance);
			} // end of while
		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}// end of setPerformanceToList

	// 모든 고객 정보 출력
	private static void printCustomer() {
		System.out.println("ID\tPW\t이름\t연락처\t\t주소\t\t나이\t등급\t누적결제금액\t마일리지");
		for (int i = 0; i < Main.userList.size(); i++) {
			System.out.print(Main.userList.get(i).getId() + "  ");
			System.out.print(Main.userList.get(i).getPw() + "\t");
			System.out.print(Main.userList.get(i).getName() + "\t");
			System.out.print(Main.userList.get(i).getPhone() + "\t");
			System.out.print(Main.userList.get(i).getAddress() + "\t");
			System.out.print(Main.userList.get(i).getAge() + "\t");
			System.out.print(Main.userList.get(i).getGrade() + "\t");
			System.out.print(Main.userList.get(i).getAccumulatedPayment() + "\t\t");
			System.out.print(Main.userList.get(i).getMileage() + "\n");

		} // end of for
	}// end of printCusetomer
}