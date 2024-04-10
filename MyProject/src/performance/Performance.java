package performance;

import java.util.ArrayList;

public class Performance implements Comparable<Performance> {
	public static final int PERIFONUM=12;
	private String performanceID;// key값으로 할 예정
	private String name; // 공연명
	private String genre; // 장르(뮤지컬, 연극, 콘서트)
	private String dayOfPerformance; // 공연일
	private String venue; // 장소
	private int limitAge; // 관람제한연령
	private int totalSeats; // 총좌석수
	private int soldSeats; // 판매좌석수
	private int ticketPrice; // 티켓가격,compare
	private String[][] seats;//좌석 현황
	private int yseats;//몇줄
	private int xseats;//한 줄에 몇명

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
		System.out.println("공연 목록: ");
		System.out.println("==============================================");
		System.out.println("공연ID\t\t  장르\t가격\t공연일\t    시청연령\t잔여좌석\t공연 장소\t\t공연명");
		if (list.size() == 0) {
			System.out.println("등록된 공연이 없습니다..");
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
				//2차원 배열 출력
				System.out.print("  ");
				for(int j=0;j<list.get(i).xseats;j++) {
					System.out.print((char)(j+65)+" ");
				}
				System.out.println();//줄 바꿈
				for(int k=0;k<list.get(i).yseats;k++) {
					System.out.print(k+1+" ");
					for(int j=0;j<list.get(i).xseats;j++) {
						System.out.print(list.get(i).seats[k][j]+" ");
					}
					System.out.println();//줄 바꿈
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

	// 공연시간, 관람 연령, 캐스팅, 간단한 설명
	// (현재 날짜와 비교하여 공연일이 지나면 예매 못하도록)
	// static 으로 count

}
