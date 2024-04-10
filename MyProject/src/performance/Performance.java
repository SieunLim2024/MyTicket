package performance;

import java.util.ArrayList;

public class Performance implements Comparable<Performance> {
	public static final int PERIFONUM=12;
	private String performanceID;// key������ �� ����
	private String name; // ������
	private String genre; // �帣(������, ����, �ܼ�Ʈ)
	private String dayOfPerformance; // ������
	private String venue; // ���
	private int limitAge; // �������ѿ���
	private int totalSeats; // ���¼���
	private int soldSeats; // �Ǹ��¼���
	private int ticketPrice; // Ƽ�ϰ���,compare
	private String[][] seats;//�¼� ��Ȳ
	private int yseats;//����
	private int xseats;//�� �ٿ� ���

	public Performance(String performanceID, String name, String genre, String dayOfPerformance, String venue,
			int limitAge, int totalSeats, int soldSeats, int ticketPrice,int yseats) {
		super();
		this.performanceID = performanceID;
		this.name = name;
		this.genre = genre;
		this.dayOfPerformance = dayOfPerformance;
		this.venue = venue;
		this.limitAge = limitAge;
		this.totalSeats = totalSeats;
		this.soldSeats = soldSeats;
		this.ticketPrice = ticketPrice;
		this.seats=seats;
		this.xseats=totalSeats/yseats;
		calSeats();
	}
	public Performance(String performanceID, String name, String genre, String dayOfPerformance, String venue,
			int limitAge, int totalSeats, int soldSeats, int ticketPrice, String[][] readSeat, int yseats, int xseats) {
		super();
		this.performanceID = performanceID;
		this.name = name;
		this.genre = genre;
		this.dayOfPerformance = dayOfPerformance;
		this.venue = venue;
		this.limitAge = limitAge;
		this.totalSeats = totalSeats;
		this.soldSeats = soldSeats;
		this.ticketPrice = ticketPrice;
		this.seats = readSeat;
		this.yseats=yseats;
		this.xseats=xseats;
	}
	public void calSeats() {
		seats= new String[yseats][xseats];
	}

	public String getPerformanceID() {
		return performanceID;
	}

	public void setPerformanceID(String performanceID) {
		this.performanceID = performanceID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDayOfPerformance() {
		return dayOfPerformance;
	}

	public void setDayOfPerformance(String dayOfPerformance) {
		this.dayOfPerformance = dayOfPerformance;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public int getLimitAge() {
		return limitAge;
	}

	public void setLimitAge(int limitAge) {
		this.limitAge = limitAge;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getSoldSeats() {
		return soldSeats;
	}

	public void setSoldSeats(int soldSeats) {
		this.soldSeats = soldSeats;
	}

	public int getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	public String[][] getSeats() {
		return seats;
	}
	public void setSeats(String[][] seats) {
		this.seats = seats;
	}
	public int getYseats() {
		return yseats;
	}
	public void setYseats(int yseats) {
		this.yseats = yseats;
	}
	public int getXseats() {
		return xseats;
	}
	public void setXseats(int xseats) {
		this.xseats = xseats;
	}
	public static void printPerformance(ArrayList<Performance> list) {
		System.out.println("���� ���: ");
		System.out.println("==============================================");
		System.out.println("����ID\t\t  �帣\t����\t������\t    ��û����\t�ܿ��¼�\t���� ���\t\t������");
		if (list.size() == 0) {
			System.out.println("��ϵ� ������ �����ϴ�..");
		} else {
			for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i).getPerformanceID() + "  |");
				System.out.print(list.get(i).getGenre() + "\t|");
				System.out.print(list.get(i).getTicketPrice() + "\t|");
				System.out.print(list.get(i).getDayOfPerformance() + " |");
				if (list.get(i).getLimitAge() < 10) {
					System.out.print(" " + list.get(i).getLimitAge() + "\t\t|");
				} else {
					System.out.print(list.get(i).getLimitAge() + "\t\t|");
				}
				System.out.print((list.get(i).getTotalSeats() - list.get(i).getSoldSeats()) + "/"
						+ list.get(i).getTotalSeats() + "\t|");
				System.out.print(list.get(i).getVenue() + "\t|");
				System.out.print(list.get(i).getName());
				System.out.println("\t");
			}
		}//end of else if
	}
	public static void printSeats(ArrayList<Performance> list,String performanceId) {
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).performanceID.equals(performanceId)) {
				//2���� �迭 ���
				System.out.print("  ");
				for(int j=0;j<list.get(i).xseats;j++) {
					System.out.print((char)(j+65)+" ");
				}
				System.out.println();//�� �ٲ�
				for(int k=0;k<list.get(i).yseats;k++) {
					System.out.print(k+1+" ");
					for(int j=0;j<list.get(i).xseats;j++) {
						System.out.print(list.get(i).seats[k][j]+" ");
					}
					System.out.println();//�� �ٲ�
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Performance [performanceID=" + performanceID + ", name=" + name + ", genre=" + genre
				+ ", dayOfPerformance=" + dayOfPerformance + ", venue=" + venue + ", limitAge=" + limitAge
				+ ", totalSeats=" + totalSeats + ", soldSeats=" + soldSeats + ", ticketPrice=" + ticketPrice + "]";
	}

	@Override
	public int compareTo(Performance o) {
		return this.ticketPrice-o.ticketPrice;
	}

	// �����ð�, ���� ����, ĳ����, ������ ����
	// (���� ��¥�� ���Ͽ� �������� ������ ���� ���ϵ���)
	// static ���� count

}
