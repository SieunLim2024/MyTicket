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
	public static final int INFONUM = 10; // ȸ�� ���� �׸� ����
	public static Scanner sc = new Scanner(System.in);
	public static ArrayList<Customer> userList = new ArrayList<Customer>();
	public static ArrayList<CartItem> paymentList = new ArrayList<>();
	public static ArrayList<CartItem> totalPaymentList = new ArrayList<>();
	public static Cart cart = new Cart();
	public static Cart payment = new Cart();
	static boolean checkLogin = false;
	static boolean duplicateID = false;
	public static Customer nowUser = null; // �α��� �� ���� ����
	public static int nowUserIndex = 0; // �α��� �� ���� ����

	public static void main(String[] args) {
		setUserToList(userList);
		Admin.setPerformanceToList();
		login();
		if (checkLogin) { // �α��� �����ÿ� ���� �޴���ss
			setPaymaentToList(totalPaymentList, nowUser);
			mainMenu(nowUser);
		}
	}

	// �α���
	private static void login() {
		boolean quit = false;
		while (!quit) {
			int num = loginInfo();
			if (num < 1 || num > 4) {
				System.out.println("�޴��� �ٸ��� �Է����ּ���.");
				continue;
			} // end of if
			String[] menu = { "�α���", "ȸ������", "������ �α���", "����" };
			switch (menu[num - 1]) {
			case "�α���":
				nowUser = userLogin();
				if (checkLogin) {
					quit = true;
				}
				break;
			case "ȸ������":
				joinMembership();
				break;
			case "������ �α���":
				Admin.adminLogin();
				break;
			case "����":
				quit = true;
				System.out.println("�����մϴ�.");
				break;
			}// end of switch
		} // end of while
	}// end of login

	// �α��� �޴� �ȳ�
	private static int loginInfo() {
		System.out.println("==============================================");
		System.out.println("�ݰ����ϴ�. KH Ƽ�� �Դϴ�.");
		System.out.println("----------------------------------------------");
		System.out.println("1.�α��� 2.ȸ������");
		System.out.println("3.������ �α��� 4.����");
		System.out.println("==============================================");

		System.out.print("�޴� ����>> ");
		int num = sc.nextInt();
		sc.nextLine();// ����Ŭ����
		return num;

	}

	// ���� �α���
	private static Customer userLogin() {
		System.out.println("���� �α���");
		// ���� �α��� ������ checkLogin true�� ����
		System.out.print("���̵�:");
		String inputId = sc.nextLine();
		System.out.print("��й�ȣ:");
		String inputPw = sc.nextLine();
		for (int i = 0; i < userList.size(); i++) {
			if (inputId.equals(userList.get(i).getId()) && inputPw.equals(userList.get(i).getPw())) {
				checkLogin = true;
				nowUserIndex = i;
				System.out.println("�α��� ����!");
				break;
			}
		}
		if (checkLogin == false) {
			System.out.println("�α��� ����");
		}
		return userList.get(nowUserIndex);
	}

	private static void setUserToList(ArrayList<Customer> userList) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("user.txt"));
			String userName;
			String[] readUser = new String[Customer.CUSTOMERINFONUM];
			while ((userName = reader.readLine()) != null) { // null�� �ƴҶ����� �б�
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

	// ȸ������
	private static void joinMembership() {
		String[] writeCustomer = new String[Customer.CUSTOMERINFONUM];
//		String[] writeCart = new String[CartItem.CARTIFONUM];
		System.out.println("ȸ�� ���� �Ͻðڽ��ϱ�? Y|N");
		String str = sc.nextLine();
		boolean quit = false;
		if (str.toUpperCase().equals("Y")) {
			while (!quit) {
				System.out.print("���̵�: ");
				String id = sc.nextLine();
				duplicateID = serchId(id);
				if (duplicateID) {
					quit = true;
					break;
				}
				writeCustomer[0] = id;
				System.out.print("��й�ȣ: ");
				writeCustomer[1] = sc.nextLine();
				System.out.print("�̸�:");
				writeCustomer[2] = sc.nextLine();
				System.out.print("����ó:");
				writeCustomer[3] = sc.nextLine();
				System.out.print("�ּ�: ");
				writeCustomer[4] = sc.nextLine();
				System.out.print("���� (���� ��): ");
				writeCustomer[5] = sc.nextLine();
				writeCustomer[6] = "Basic";// ȸ�� ���Խ� ��� ������ Basic
				writeCustomer[7] = "0";// ȸ�� ���Խ� ���� �ѱݾ� ������ 0
				writeCustomer[8] = "0";// ȸ�� ���Խ� ���ϸ��� ������ 0
				writeCustomer[9] = "0";// ȸ�� ���Խ� ����ȸ�� ������ 0
				writeCustomer[10] = "0";// ȸ�� ���Խ� īƮ��� �� �� ������ 0
				FileWriter fw = null;
				try {
					// �� �� ������ ���Ͽ� �̾�� ���� �����ڿ� true �ɼ� ����
					fw = new FileWriter("user.txt", true);

					for (int i = 0; i < Customer.CUSTOMERINFONUM; i++) {
						fw.write(writeCustomer[i] + "\n");
					}
					System.out.println("ȸ�������� �Ϸ� �Ǿ����ϴ�!");
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
			System.out.println("ȸ���� �߰����� �ʽ��ϴ�.");
			quit = true;
		}
	}// end of joinMembership

	private static boolean serchId(String id) {
		boolean flag = false;
		for (int i = 0; i < userList.size(); i++) {
			if (id.equals(userList.get(i).getId())) {
				System.out.println("�̹� ������� ���̵� �Դϴ�.");
				flag = true;
				break;
			} // end of if
		} // end of for
		if (flag == false) {
			System.out.println("��� ������ ���̵� �Դϴ�.");
		}
		return flag;
	}

	// ���� �޴�
	private static void mainMenu(Customer nowUser) {
		boolean quit = false;
		while (!quit) {
			int num = mainInfo();
			if (num < 1 || num > 4) {
				System.out.println("�޴��� �ٸ��� �Է����ּ���.");
				continue;
			} // end of if
			String[] menu = { "�� ����", "��ٱ���", "��������", "����" };
			switch (menu[num - 1]) {
			case "�� ����":
				System.out.println("==============================================");
				System.out.println("�� ����");
				System.out.println("----------------------------------------------");
				System.out.println("���̵�\t�̸�\t����ó\t\t�ּ�\t\t����\t���\t���������ݾ�\t���ϸ���");
				System.out.println(nowUser); // ȸ�� ���� ���
				Cart.printTotalPayment(totalPaymentList, nowUser);// ���� ���� ���� ���
				break;
			case "��ٱ���":
				cart();
				break;
			case "��������":
				selectGenre(); // �帣 ���� �ϱ�
				break;
			case "����":
				quit = true;
				System.out.println("�����մϴ�.");
				break;
			}// end of switch
		} // end of while
	}// end of mainMenu

	private static void addCart(ArrayList<Performance> performanceList) {
//		Admin.performanceList.clear();//üũ
//		Admin.setPerformanceToList();//üũ
		boolean quit = false;
		while (!quit) {
			System.out.print("��ٱ��Ͽ� �߰��� ������ ID�� �Է��ϼ��� : ");
			String inputID = sc.nextLine();

			boolean flag = false; // ��ġ����
			int numIndex = -1; // �ε��� ��ȣ
			int numTicket = 0;

			for (int i = 0; i < performanceList.size(); i++) {
				if (inputID.equals(performanceList.get(i).getPerformanceID())) {
					numIndex = i;
					flag = true;
					break;
				}
			} // end of for

			// ��ġ�ϸ� ��ٱ��� �߰� ���θ� ���´�.
			if (flag) {
				System.out.println("��ٱ��Ͽ� �߰��ϰڽ��ϱ�? Y|N ");
				String str = sc.nextLine();
				if (str.toUpperCase().equals("Y")) {
					System.out.print("�߰��� ������ �Է����ּ���: ");
					numTicket = sc.nextInt();
					sc.nextLine();// ����Ŭ����

					// �ܿ� �¼��� �� Ƽ�� ���� ��
					if (Admin.performanceList.get(numIndex).getSoldSeats() + numTicket > Admin.performanceList
							.get(numIndex).getTotalSeats()) {
						System.out.println("�ܿ� �¼����� ���� ������ �����ϴ�.");
					} else {

//						Performance.printSeats(Admin.performanceList, inputID);// �¼� �����ֱ�
						// �¼� ����
						String seatNum = askSeatNum(numTicket, inputID, numIndex);// �� �¼� ����.
						System.out.println(performanceList.get(numIndex).getName() + " ������ ��ٱ��Ͽ� �߰��Ǿ����ϴ�.");
						System.out.println("������ �¼��� " + seatNum.toUpperCase() + " �Դϴ�.");

						// ��ٱ��Ͽ� �ֱ��̹� īƮ�� �׸� ������ ������ ������Ʈ
						if (!cart.isCartInPerformance(performanceList.get(numIndex).getPerformanceID(), numTicket,
								seatNum)) { // �̹� īƮ�� �׸� ������ ������ ������Ʈ
							cart.insertPerformance(performanceList.get(numIndex), numTicket, nowUser, seatNum); // �ƴϸ�
																												// īƮ��
																												// �׸� �߰�
						} // end of if
					} // end of else if(������ �ܿ� �¼� ������...)

				} // end of if(Y���� �˻�)
				quit = true;
			} else {
				System.out.println("�ٽ� �Է����ּ���");
			}
		}
	}

	private static String askSeatNum(int numTicket, String inputID, int numIndex) {
		StringBuffer seatNum = new StringBuffer();// ���� ��ŭ ���� �����ؾ��ؼ� String ��� StringBuffer
		boolean flag = false;// �̹� �� �¼����� �˻�
		for (int i = 0; i < numTicket; i++) {// ���� ��ŭ �ݺ��� ������
			Performance.printSeats(Admin.performanceList, inputID);
			while (!flag) {
				System.out.print("�¼� ��ȣ�� ����ּ��� (ex:1A): ");
				String input = sc.nextLine();
				seatNum.append(input);
				String[] seatYX = input.split("");// seatYX[0]�� ����ȣ seatYX[1]�� �� ��ȣ
				String[][] seats = Admin.performanceList.get(numIndex).getSeats();// �¼� �޾ƿ�
				String xNum = seatYX[1].toUpperCase();// �ϴ� �빮�ڷ� �ٲ���
				if (seats[Integer.parseInt(seatYX[0].toString())-1][((int) (xNum.charAt(0))) - 65].equals("��")) {
					System.out.println("�̹� ���õ� �¼��Դϴ�.");
					seatNum=null;
					seatNum = new StringBuffer();//StringBuffer �ʱ�ȭ
					
				}else {
					System.out.println("���� ������ �¼��Դϴ�.");
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

	// ���� ���� �о� ���� ���� ��� ��
	private static int countPerformance() {
		try {
			FileReader fr = new FileReader("performance.txt");
			BufferedReader reader = new BufferedReader(fr);
			String str;
			int num = 0; // ���� ����
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

		showList.addAll(Admin.performanceList); // ���� �����ص�.
		boolean quit = false;
		while (!quit) {

			int num = GenreInfo();
			if (num < 1 || num > 5) {
				System.out.println("�޴��� �ٸ��� �Է����ּ���.");
				continue;
			} // end of if
			String[] menu = { "��ü ����", "������", "����", "�ܼ�Ʈ", "���� ȭ������" };
			switch (menu[num - 1]) {
			case "��ü ����":
				Performance.printPerformance(showList);
//				���� �����ְ�...
				sortOrCart(showList);
				break;
			case "������":
				ArrayList<Performance> showMList = null;
				showMList = (ArrayList<Performance>) showList.stream().filter(s -> s.getGenre().equals("������"))
						.collect(Collectors.toList());
				Performance.printPerformance(showMList);
//				���� �����ְ�...
				sortOrCart(showMList);
				break;
			case "����":
				ArrayList<Performance> showPList = null;
				showPList = (ArrayList<Performance>) showList.stream().filter(s -> s.getGenre().equals("����"))
						.collect(Collectors.toList());
				Performance.printPerformance(showPList);
//				���� �����ְ�...
				sortOrCart(showPList);
				break;
			case "�ܼ�Ʈ":
				ArrayList<Performance> showCList = null;
				showCList = (ArrayList<Performance>) showList.stream().filter(s -> s.getGenre().equals("�ܼ�Ʈ"))
						.collect(Collectors.toList());
				Performance.printPerformance(showCList);
//				���� �����ְ�...
				sortOrCart(showCList);
				break;
			case "���� ȭ������":
				quit = true;
				System.out.println("�����մϴ�.");
				showList.clear();// �ʱ�ȭ
				break;
			}// end of switch
		} // end of while
	}

	private static void sortOrCart(ArrayList<Performance> showList) {
//		ArrayList<Performance> sortList= new ArrayList<Performance>();
//		sortList.addAll(showList); //���� �����ص�.
		boolean quit = false;
		while (!quit) {
			// print
			int num = sortOrCartInfo();
			if (num < 1 || num > 4) {
				System.out.println("�޴��� �ٸ��� �Է����ּ���.");
				continue;
			} // end of if
			String[] menu = { "���� ���� ��", "���� ���� ��", "��ٱ��� ���", "���� �޴���" };
			switch (menu[num - 1]) {
			case "���� ���� ��":
				ArrayList<Performance> showLowList = new ArrayList<Performance>();
				showLowList.addAll(showList); // ���� �����ص�.
				Collections.sort(showLowList);
				Performance.printPerformance(showLowList);
				break;
			case "���� ���� ��":
				ArrayList<Performance> showHighList = new ArrayList<Performance>();
				showHighList.addAll(showList); // ���� �����ص�.
				Collections.sort(showHighList, Comparator.reverseOrder());
				Performance.printPerformance(showHighList);
				break;
			case "��ٱ��� ���":
				askaddCart();
				break;
			case "���� �޴���":
				quit = true;
				System.out.println("���� �޴��� ���ư��ϴ�.");
				break;
			}// end of switch
		} // end of while
	}

	private static int sortOrCartInfo() {
		System.out.println("==============================================");
		System.out.println("���Ͻô� �޴� �������ּ���.");
		System.out.println("----------------------------------------------");
		System.out.println("1.���� ���� �� 2.���� ���� ��");
		System.out.println("3.��ٱ��� ��� 4.���� �޴���");
		System.out.println("==============================================");

		System.out.print("�޴� ����>> ");
		int num = sc.nextInt();
		sc.nextLine();// ����Ŭ����
		return num;
	}

	private static void askaddCart() {
		countPerformance();
		addCart(Admin.performanceList);
//		}
	}

	private static int GenreInfo() {
		System.out.println("==============================================");
		System.out.println("���Ͻô� �帣�� �������ּ���.");
		System.out.println("----------------------------------------------");
		System.out.println("1.��ü ���� 2.������");
		System.out.println("3.���� 4.�ܼ�Ʈ");
		System.out.println("5.���� ȭ������");
		System.out.println("==============================================");

		System.out.print("�޴� ����>> ");
		int num = sc.nextInt();
		sc.nextLine();// ����Ŭ����
		return num;
	}

	// ���� �޴� ����
	private static int mainInfo() {
		System.out.println("==============================================");
		System.out.println("ȯ���մϴ�. KH Ƽ�� �Դϴ�.");
		System.out.println("----------------------------------------------");
		System.out.println("1.�� ���� 2.��ٱ���");
		System.out.println("3.�������� 4.����");
		System.out.println("==============================================");

		System.out.print("�޴� ����>> ");
		int num = sc.nextInt();
		sc.nextLine();// ����Ŭ����
		return num;
	}// end of mainInfo

	// ���� �޴����� īƮ ���ý� ...
	private static void cart() {
		boolean quit = false;
		while (!quit) {
			cart.printCart();
			int num = Cart.cartInfo();
			if (num < 1 || num > 4) {
				System.out.println("�޴��� �ٸ��� �Է����ּ���.");
				continue;
			} // end of if
			String[] menu = { "��ٱ��� �׸� ����", "��ٱ��� ��� ����", "���� �ϱ�", "���� �޴���" };
			switch (menu[num - 1]) {
			case "��ٱ��� �׸� ����":
				try {
					cartRemoveItem();
				} catch (CartException e1) {
					e1.printStackTrace();
				}
				break;
			case "��ٱ��� ��� ����":
				try {
					cartClear();
				} catch (CartException e) {
//					e.printStackTrace();
					System.out.println(e.getMessage());

				}
				break;
			case "���� �ϱ�":
				buy();
				break;
			case "���� �޴���":
				quit = true;
				System.out.println("���� �޴��� ���ư��ϴ�.");
				break;
			}// end of switch
		} // end of while
	}// end of cart

	private static void buy() {
		int count = 0;// ���� ����
		System.out.println("��ٱ��Ͽ� ��� �׸��� �����մϴ�.");
		System.out.println("�����Ͻðڽ��ϱ�? Y|N ");
		String str = sc.nextLine();
		if (str.toUpperCase().equals("Y")) {
			paymentList.addAll(cart.cartItem);
			System.out.println("�����մϴ�. ������ �Ϸ� �Ǿ����ϴ�.");
			try {
				cartClear();
			} catch (CartException e) {
				e.printStackTrace();
			}
		} // end of it

		int price = payment.printPayment(paymentList);

		nowUser.setAccumulatedPayment(nowUser.getAccumulatedPayment() + price);// ���� ���ž� ����
		nowUser.setMileage((int) (nowUser.getMileage() + price * 0.01));// ���ϸ��� 1%�� ����
		nowUser.setBuyNum(nowUser.getBuyNum() + payment.totalcount);// ���� ���� �׸�� ����
		if (nowUser.getAccumulatedPayment() > 150000) {
			nowUser.setGrade("VIP");
		}
		Admin.saveUserToFile();// ����� ������ �����Ƿ� ����

		for (int j = 0; j < paymentList.size(); j++) {
			for (int i = 0; i < Admin.performanceList.size(); i++) {// ��������Ʈ�� �� �ִ� �� ��ŭ ������
				if (Admin.performanceList.get(i).getPerformanceID().equals(paymentList.get(j).getPerformanceId())) {// ������
																													// �Ͱ�
																													// ����
																													// ����Id��
																													// �ִٸ�..
					Admin.performanceList.get(i).setSoldSeats(
							Admin.performanceList.get(i).getSoldSeats() + paymentList.get(j).getQuantity());

					String[][] updateSeats = Admin.performanceList.get(i).getSeats();// ���� ����Ʈ�� �¼� 2���� �迭 ������
					String[] seatNumSplit = paymentList.get(j).getSeatNum().split(",");// �¼� ���� ��� �迭 ����
					for (int k = 0; k < seatNumSplit.length; k++) {
						String[] seatsYX = seatNumSplit[k].split("");// seatsYX[0]�� �� ��ȣ seatsYX[1]�� �� ��ȣ
						String xNum = seatsYX[1].toUpperCase();// �ϴ� �빮�ڷ� �ٲ���
						updateSeats[Integer.parseInt(seatsYX[0].toString())-1][((int) (xNum.charAt(0))) - 65] = "��";// �ƽ�Ű�ڵ��
																													// �ٲ㼭
																													// A->1
						Admin.performanceList.get(i).setSeats(updateSeats);
					}
				}
			}
		}
		Admin.savePerformanceFile(Admin.performanceList);// ���Ͽ� �ٽ� ����
		Admin.performanceList.clear();// �ʱ�ȭ
		System.out.println("�ʱ�ȭ �Ϸ�!");
		Admin.setPerformanceToList();// �ȸ� Ƽ�� ���� �ݿ�
		System.out.println("����Ʈ �ε� �Ϸ�!");

		writePaymentFile(paymentList);
		System.out.println("���� ���� �ۼ� �Ϸ�!");
		payment.totalcount = 0;// �ʱ�ȭ
		paymentList.clear();// �ʱ�ȭ
		totalPaymentList.clear();// �ʱ�ȭ
		setPaymaentToList(totalPaymentList, nowUser);// �ٽ� ����
	}

	private static void cartRemoveItem() throws CartException {
		if (cart.cartCount == 0) {
			throw new CartException("��ٱ��� �׸��� �����ϴ�.");
		} else {
			if (cart.cartCount >= 0) {
				cart.printCart();
			}
			boolean quit = false;
			while (!quit) {
				System.out.print("��ٱ��Ͽ��� ������ ������ ID�� �Է��ϼ��� :");
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
					System.out.println("��ٱ��Ͽ��� �����ϰڽ��ϱ�? Y|N ");
					str = input.nextLine();
					if (str.toUpperCase().equals("Y")) {
						System.out.println(cart.cartItem.get(numId).getPerformanceName() + " ��ٱ��Ͽ��� ������ ���� �Ǿ����ϴ�.");
						cart.removeCart(numId);
					}
					quit = true;
				} else {
					System.out.println("�ٽ� �Է��� �ּ���");
				}
			}
		}
	}

	// īƮ ��� ����
	private static void cartClear() throws CartException {
		if (cart.cartCount == 0) {
			throw new CartException("��ٱ��Ͽ� �׸��� �����ϴ�.");
		} else {
			System.out.println("��ٱ��Ͽ� ��� �׸��� ���� �ϰڽ��ϱ�? Y|N ");
			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			if (str.toUpperCase().equals("Y")) {
				System.out.println("��ٱ��Ͽ� ��� �׸��� �����߽��ϴ�.");
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
			// �� �� ������ ���Ͽ� �̾�� ���� �����ڿ� true �ɼ� ����
			fw = new FileWriter("payment.txt", true);

			for (int i = 0; i < CartItem.CARTIFONUM; i++) {
				fw.write(writePayment[i]);
			}
			System.out.println("���� ������ ���Ͽ� ����Ǿ����ϴ�.");
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

	// ���Ͽ��� �α��� �� ����� ���� ������ �̾Ƽ� list�� ����
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
