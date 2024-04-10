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
	private String adminID = "admin";
	private String adminPW = "admin1234";
	private static boolean adminLogin = false;
	public static final int PERINFONUM = 9;
	public static ArrayList<Performance> performanceList = new ArrayList<Performance>();
	public static ArrayList<Customer> CustomerList = new ArrayList<Customer>();
	public static ArrayList<CartItem> withoutUserPaymentList = new ArrayList<CartItem>();
	static boolean removeUserflag = false;
	static boolean removePayflag = false;
	static boolean removeflag = false;

	public String getAdminID() {
		return adminID;
	}

	public String getAdminPW() {
		return adminPW;
	}

	// ������ �α���
	public static void adminLogin() {
		System.out.println("������ ������ �Է��ϼ���");

		System.out.print("���̵�: ");
		String adminId = sc.nextLine();
		System.out.print("��й�ȣ: ");
		String adminPW = sc.nextLine();

		Admin admin = new Admin();
		if (adminId.equals(admin.getAdminID()) && adminPW.equals(admin.getAdminPW())) {

			System.out.println("������ �α��ο� ���� �Ͽ����ϴ�.");
			adminLogin = true;
			adminMenu();//������ �޴���
		} else {
			System.out.println("���̵� ��й�ȣ�� ���� �ʽ��ϴ�.");
		}

	}

	// ������ �޴�
	public static void adminMenu() {
		boolean quit = false;
		while (!quit) {
			int num = adminMenuInfo();
			if (num < 1 || num > 5) {
				System.out.println("�޴��� �ٸ��� �Է����ּ���.");
				continue;
			} // end of if
			String[] menu = { "���� �߰�", "���� ����", "ȸ�� ���", "ȸ�� ����", "����" };
			switch (menu[num - 1]) {
			case "���� �߰�":
				addPerformance();
				break;
			case "���� ����":
				deletePerformance();
				break;
			case "ȸ�� ���":
				printCustomer();
				break;
			case "ȸ�� ����":
				deleteUser();
				break;
			case "����":
				quit = true;
				System.out.println("������ �޴��� �����մϴ�.");
				break;

			}// end of switch
		} // end of while
	}// end of admiMenu

	private static void deleteUser() {
		searchUser();
		// �ٽ� ���Ϸ� �ٲ㼭 ����
		if (removeUserflag == true) {
			saveUserToFile();
		}

	}

	public static void saveUserToFile() {
		FileWriter fw = null;
		try {
			// ���� ���� ������
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

			System.out.println("�� ������ ���� �Ǿ����ϴ�.");
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

	private static void searchUser() {
		if (Main.userList.size() == 0) {
			System.out.println("����� ���� �����ϴ�.");
		} else {
			boolean quit = false;
			while (!quit) {
				System.out.print("������ �� ������ ID�� �Է��ϼ��� :");
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
					System.out.println("�ش� ������ ã�ҽ��ϴ�.");

				} else {
					System.out.println("�ش� ������ ã�� ���߽��ϴ�.");
					quit = true;
					break;
				}
				boolean removeflag = false;

				removeflag = removeCustomer(idIndex, removeflag);
				if (removeflag) {
					System.out.println("������ ���� �Ǿ����ϴ�.");
					quit = true;
					removeUserflag = true;
				} else {
					System.out.println("���� ������ ��ҵǾ����ϴ�.");
					quit = true;
				} // end of else if

			} // end of while
		} // end of else if

	}

	private static boolean removeCustomer(int idIndex, boolean flag) {
		System.out.println("ID\t�̸�\t����ó\t\t�ּ�\t\t����\t���\t���������ݾ�\t���ϸ���");
		System.out.println(Main.userList.get(idIndex).toString());
		System.out.println("�ش� ������ �����ϰڽ��ϱ�? Y|N ");
		String str = sc.nextLine();
		if (str.toUpperCase().equals("Y")) {
			boolean removePaymentflag = false;

			removePaymentflag = removePayment(idIndex, removePaymentflag);// ���� ���� ���� ���� ������ ��������
			if (removePaymentflag) {
				System.out.println("�ش� ������ ���� ������ ���� �Ǿ����ϴ�.");
				removeUserflag = true;
			} else {
				System.out.println("�ش� ������ ���� ���� ������ �����߽��ϴ�.");
			} // end of else if

			Main.userList.remove(idIndex);
			flag = true;
		} else {
			flag = false;
		} // end of else if
		return flag;
	}

	//
	private static boolean removePayment(int idIndex, boolean flag) {
		Main.setPaymaentToList(Main.totalPaymentList, Main.userList.get(idIndex));// �ϴ� �� ����Ʈ ����...
		Cart.printTotalPayment(Main.totalPaymentList, Main.userList.get(idIndex));// ������ ���� ������
		System.out.println("�� ������ ���� �մϴ�.");
		Main.totalPaymentList.clear();// �� ���������� �����
		// ������ �κ� ������ ����Ʈ ��������
		setWithoutUserPaymentList(Main.userList.get(idIndex));
		// �װ��� ���Ͽ� ����
		savePaymentFile(withoutUserPaymentList);
		// ����Ʈ �ʱ�ȭ(�ٽ� �־��� �ʿ�� ����)
		withoutUserPaymentList.clear();
		flag = true;

		return flag;
	}

	private static void savePaymentFile(ArrayList<CartItem> list) {
		FileWriter fw = null;
		try {
			// ���� ���� ���� ������
			fw = new FileWriter("payment.txt");
			for (int i = 0; i < list.size(); i++) {
				fw.write(list.get(i).getCostomerID() + "\n");
				fw.write(list.get(i).getPerformanceId() + "\n");
				fw.write(list.get(i).getPerformanceName() + "\n");
				fw.write(list.get(i).getQuantity() + "\n");
				fw.write(list.get(i).getSeatNum() + "\n");
				fw.write(list.get(i).getTotalPrice() + "\n");
			}

			System.out.println("���� ���� ������ ���� �Ǿ����ϴ�.");
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

//	}

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

	public static int adminMenuInfo() {
		System.out.println("==============================================");
		System.out.println("�ݰ����ϴ�. KH Ƽ�� ������ �޴� �Դϴ�.");
		System.out.println("----------------------------------------------");
		System.out.println("1.���� �߰� 2.���� ����");
		System.out.println("3.ȸ�� ��� 4.ȸ�� ����");
		System.out.println("5.����");
		System.out.println("==============================================");

		System.out.print("�޴� ����>> ");
		String input = sc.nextLine().replaceAll("[^1-5]", "0");// 1~4 �ܴ̿� 0, �޴��� 0 �������
		int num = Integer.parseInt(input); // �� ��ȯ
		return num;

	}

	private static void addPerformance() {
		boolean ageFlag = false;
		boolean seatFlag = false;
		boolean priceFlag = false;
		boolean genreFlag = false;
		boolean yFlag = false;
		if (adminLogin = true) {
			String[] writePerformance = new String[Performance.PERIFONUM];
			System.out.println("���� ������ �߰��ϰڽ��ϱ�? Y|N");
			String str = sc.nextLine();

			if (str.toUpperCase().equals("Y")) {
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("YYMMDDHHMMS");
				String strDate = formatter.format(date);
				// ���� id�� �߰��ϴ� �ð�(�ʴ�������)��ġ ���� ������ �ߺ����� ����
				writePerformance[0] = "pID" + strDate.substring(0, 13);		//substring�� ���ʿ��� �����̳� ��½� �� ���� ���Ǹ� ���� �־��... 
				System.out.println("����ID: " + writePerformance[0]);
				System.out.print("������ : ");
				writePerformance[1] = sc.nextLine();
				// -----------------------------------------------------------
				while (!genreFlag) {
					System.out.println("[������, ����, �ܼ�Ʈ]");
					System.out.print("�帣 :");
					String input = sc.nextLine();

					if (!(input.equals("������")||input.equals("����")||input.equals("�ܼ�Ʈ"))) {// �帣 �ٸ����Է����� ������
						System.out.println("�帣�� �ٸ��� �Է����ּ���.");
						continue;
					}
					writePerformance[2] = input;
					genreFlag = true;
				}
				// -----------------------------------------------------------
				System.out.print("������ ��)2024-01-01 :");
				writePerformance[3] = sc.nextLine();
				System.out.print("���� ���: ");
				writePerformance[4] = sc.nextLine();
				// -----------------------------------------------------------
				while (!ageFlag) {
					System.out.print("�������ѿ��� (���� ��): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// ���� �̿� ���� ó��

					if (input.length() == 0) {// ���ڸ� �ѹ��� �Է����� ������
						input = "0";// null ���� (��� �ʿ� ������ ������)
						System.out.println("���ڸ� �Է����ּ���.");
						continue;
					}
					writePerformance[5] = input;
					ageFlag = true;
				}
				// -----------------------------------------------------------
				while (!seatFlag) {
					System.out.print("�� �¼��� (���ڸ�): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// ���� �̿� ���� ó��

					if (input.length() == 0) {// ���ڸ� �ѹ��� �Է����� ������
						input = "0";
						System.out.println("���ڸ� �Է����ּ���.");
						continue;
					}
					if (input.equals("0")) {
						System.out.println("0�� �̸��� �Է��� �Ұ��մϴ�.");
						continue;
					} else {
						writePerformance[6] = input;
						seatFlag = true;
					}
				}
				// -----------------------------------------------------------
				writePerformance[7] = "0"; // ���� �߰��� �Ǹ��¼��� ������ 0����
				// -----------------------------------------------------------
				while (!priceFlag) {
					System.out.print("Ƽ�� ���� (���ڸ�): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// ���� �̿� ���� ó��

					if (input.length() == 0) {// ���ڸ� �ѹ��� �Է����� ������
						input = "0";
						System.out.println("���ڸ� �Է����ּ���.");
						continue;
					}
					if (input.equals("0")) {
						System.out.println("0�� �̸��� �Է��� �Ұ��մϴ�.");
						continue;
					} else {
						writePerformance[8] = input;
						priceFlag = true;
					}
				}
				// -----------------------------------------------------------
				while (!yFlag) {
					System.out.print("������ �¼� ��(���ڸ�): ");
					String input = sc.nextLine().replaceAll("[^0-9]", "");// ���� �̿� ���� ó��

					if (input.length() == 0) {// ���ڸ� �ѹ��� �Է����� ������
						input = "0";
						System.out.println("���ڸ� �Է����ּ���.");
						continue;
					}
					if (input.equals("0")) {
						System.out.println("0�� �̸��� �Է��� �Ұ��մϴ�.");
						continue;
					} else {
						writePerformance[10] = input;
						yFlag = true;
					}
				}
				// -----------------------------------------------------------
				int total = Integer.parseInt(writePerformance[6]);// �� �¼���
				int y = Integer.parseInt(writePerformance[10]); // �¼� �� ��
				int x = 0;// �¼� �� ��
				int unable = 0;// ���� �� ���� �¼� ��
				// �������� ������ ��ĭ �÷��ֱ�
				if (total % y > 0) {
					writePerformance[10] = y + "";// int->String
					unable = total % y;

					x = (int) (total / y)+1;
					writePerformance[11] = x + "";// int->String
				} else {
					x = (int) (total / y);
					writePerformance[11] = x + "";// int->String
				}
				// -----------------------------------------------------------
				StringBuffer seat = new StringBuffer(); // ���� Ƚ���� �����Ƿ� String ��� StringBuffer ���
				for (int i = 0; i < Integer.parseInt(writePerformance[6]); i++) {
					seat.append("��");// �� �¼��� ��ŭ ���ڸ� ������ش�.
				}
				for (int i = 0; i < unable; i++) {
					seat.append("x");// ���� �� ���� �ڸ��� x�� ǥ��
				}
				writePerformance[9] = seat.toString();
				// -----------------------------------------------------------
				FileWriter fw = null;
				try {
					// �� ���� ������ ���Ͽ� �̾�� ���� �����ڿ� true �ɼ� ����
					fw = new FileWriter("performance.txt", true);

					for (int i = 0; i < Performance.PERIFONUM; i++) {
						fw.write(writePerformance[i] + "\n");
					}

					System.out.println("�� ���� ���� �Ǿ����ϴ�.");
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
				performanceList.clear();// ���� ���� �����Ƿ� �ʱ�ȭ
				setPerformanceToList();// �ٽ� ����

			} else {
				System.out.println("������ �߰����� �ʽ��ϴ�.");
			}
		} else {
			System.out.println("������ �α����� �ʿ��մϴ�.");
		}

	}

	private static void deletePerformance() {
		// ����->����Ʈ
		System.out.println("==============================================");
		System.out.println("���� ���� ���");
		System.out.println("----------------------------------------------");
		// ���� �����ϴ� ����(��ົ)���
		printPerformanceList(performanceList);
		// ���� ���̵�� �˻�
		searchID();
		// ���� ���� �Ǿ��ٸ� �ٽ� ���Ϸ� �ٲ㼭 ����
		if(removeflag==true) {
		savePerformanceFile(performanceList);
		}
	}

	public static void savePerformanceFile(ArrayList<Performance> performanceList) {
		FileWriter fw = null;
		try {
			// ���� ���� ������
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

				fw.write("\n");// �� �ٲ�
				fw.write(performanceList.get(i).getYseats() + "\n");
				fw.write(performanceList.get(i).getXseats() + "\n");
			}

			System.out.println("���� ������ ���� �Ǿ����ϴ�.");
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

	// ����(��ົ)���
	private static void printPerformanceList(ArrayList<Performance> list) {
		System.out.println("����ID\t\t  ������\t\t�帣\t������");
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getPerformanceID() + "   ");
			System.out.print(list.get(i).getDayOfPerformance() + "\t");
			System.out.print(list.get(i).getGenre() + "\t");
			System.out.print(list.get(i).getName() + "\n");
		} // end of for
	}// end of printPerformanceList

	// ���̵�� �˻�
	private static void searchID() {
		if (performanceList.size() == 0) {
			System.out.println("����� ������ �����ϴ�.");
		} else {
			boolean quit = false;
			while (!quit) {
				System.out.print("������ ������ ID�� �Է��ϼ��� :");
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
					System.out.println("�ش� ������ ã�ҽ��ϴ�.");
				} else {
					System.out.println("�ش� ������ ã�� ���߽��ϴ�.");
					continue;
				}

				removeflag = removePerformance(idIndex, removeflag);
				if (removeflag) {
					System.out.println("������ ���� �Ǿ����ϴ�.");
					quit = true;
				} else {
					System.out.println("���� ������ ��ҵǾ����ϴ�.");
					quit = true;
				} // end of else if

			} // end of while
		} // end of else if

	}

	private static boolean removePerformance(int idIndex, boolean flag) {
		System.out.println(performanceList.get(idIndex).toString());
		System.out.println("�ش� ������ �����ϰڽ��ϱ�? Y|N ");
		String str = sc.nextLine();
		if (str.toUpperCase().equals("Y")) {
			performanceList.remove(idIndex);
			flag = true;
		} else {
			flag = false;
		} // end of else if
		return flag;

	}// end of removePerformance

	// ���� ���Ͽ� ����� ���� ����Ʈ��...
	public static void setPerformanceToList() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("performance.txt"));
			String performanceID;
			String[] readUser = new String[Performance.PERIFONUM];

			String[][] setSeat;// �¼� 2���� �迭��

			while ((performanceID = reader.readLine()) != null) {
				readUser[0] = performanceID;
				for (int i = 1; i < Performance.PERIFONUM; i++) {
					readUser[i] = reader.readLine();
				}
				String[] seatsSplit = new String[Integer.parseInt(readUser[6])];// ���⿡ ��� �ϳ��� �־��ٰ���(�� �¼����� ũ�� ����)
				seatsSplit = readUser[9].split("");// ���ٷ� �Ȱ� �ϳ��� ����
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
				// �迭�� ������ֱ�

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

	private static void printCustomer() {
		System.out.println("ID\tPW\t�̸�\t����ó\t\t�ּ�\t\t����\t���\t���������ݾ�\t���ϸ���");
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
