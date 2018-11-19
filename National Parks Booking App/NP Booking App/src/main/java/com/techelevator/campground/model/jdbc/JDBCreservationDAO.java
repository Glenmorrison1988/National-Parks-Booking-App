package com.techelevator.campground.model.jdbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;

public class JDBCreservationDAO implements ReservationDAO {

	private Scanner input = new Scanner(System.in);
	private List<Campground> thisCampgroundList;

	
	

	private JdbcTemplate jdbcTemplate;

	public JDBCreservationDAO(DataSource dataSource) {
	  
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	@Override
	public void getAvailableReservations(List<Campground> thisCampgroundList) {
		
		this.thisCampgroundList = thisCampgroundList;
		List<Reservation> availableReservations = new ArrayList<>();
		int campgroundId = 0;
		Campground selectedCampground = null;
		System.out.println("Which Campground (enter 0 to cancel)?");
		campgroundId = input.nextInt();
		input.nextLine();
		if (campgroundId == 0) {
			return;
		}
		for (int i = 0; i < thisCampgroundList.size(); i++) {
			Campground currentCampground = thisCampgroundList.get(i);
			if (campgroundId == currentCampground.getCampgroundId()) {
				selectedCampground = currentCampground;
			}
		}
		try{int test  = selectedCampground.getCampgroundId();
		}catch (NullPointerException n) {
			System.out.println("Please select one of the campgrounds numbers on your screen!");
			getAvailableReservations(thisCampgroundList);
		}
		BigDecimal cost = selectedCampground.getDailyFee();
		System.out.println("What is the arrival date? yyyy/mm/dd");
		LocalDate arrivalDate = checkDateInput();
		System.out.println("What is the departure date? yyyy/mm/dd");
		LocalDate departureDate = checkDateInput();
		departureDate = checkDepartureDate(arrivalDate, departureDate);
		Integer lengthOfStay = departureDate.getDayOfYear() - arrivalDate.getDayOfYear();
		BigDecimal lengthOfStayBD = new BigDecimal(lengthOfStay);
		BigDecimal totalCost = cost.multiply(lengthOfStayBD);
		
		List<Site> availableSites = getAvailableSites(arrivalDate, departureDate, selectedCampground);
		if(!checkSites(availableSites)) {
			return;
		}
		displayAvailableSites(availableSites, totalCost);
	
	reserveSelectedSite(availableSites, arrivalDate, departureDate);
	return;
	}
	
	

	private void reserveSelectedSite(List<Site> availableSites, LocalDate arrivalDate, LocalDate departureDate) {
		Site selectedSite = new Site();
		System.out.println("Which site should be reserved (enter 0 to cancel)?");
		Integer selectedSiteNumber = input.nextInt(); //add try catch
		if(selectedSiteNumber == 0) {
			return;
		}
		for(int i = 0; i < availableSites.size(); i++) {
			if(selectedSiteNumber == availableSites.get(i).getSiteNumber()) {
				selectedSite = availableSites.get(i);
			}
		}
		if(selectedSite.getSiteNumber()== null) {
			System.out.println("\nPlease choose valid site number\n");
			reserveSelectedSite(availableSites, arrivalDate, departureDate);
		}
		String nameEntered = "";
		while (true) {
		System.out.println("What name should the reservation be made under?\n");
		nameEntered = input.next();
		if(nameEntered == null) {
			System.out.println("Please enter a valid name\n");
			continue;
		}
		if(nameEntered.length() > 80) {
			System.out.println("Name cannot exceed 80 characters\n");
			continue;
		}
		break;
		}
		
		int siteId = selectedSite.getSiteId();
		String sqlReserveSelectedSite = "INSERT INTO reservation (site_id, name, from_date, to_date) VALUES (?,?,?,?)";
		jdbcTemplate.update(sqlReserveSelectedSite, siteId, nameEntered, arrivalDate, departureDate);
		String sqlGetReservation = "SELECT * FROM reservation WHERE site_id = ? AND from_date = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservation, siteId, arrivalDate);
		Reservation selectedReservation = new Reservation();
		while(results.next()) {
			selectedReservation.setReservationId(results.getInt(1));
		}
		
		
		
		System.out.println("The reservation has been made and the confirmation id is " + selectedReservation.getReservationId());
		
	}

	private boolean checkSites(List<Site> availableSites) {
		if(availableSites.size() == 0) {
			System.out.println("Sorry, there are no sites available at that campground during those dates."
							 + "\nWould you like to select a different campground or timeframe? (Y)es or (N)o: ");
			String answer = input.nextLine().toUpperCase();
			if(answer.equals("Y")||answer.equals("YES")) {
				getAvailableReservations(thisCampgroundList);
			}else {
				return false;
			}
		}
		return true;
	}

	private LocalDate checkDepartureDate(LocalDate arrivalDate, LocalDate departureDate) {
		while (true) {
			if (departureDate.compareTo(arrivalDate) <= 0) {
				System.out.println("Your departure date must be at least one day later than your arrival date."
						+ "\nPlease re-enter your departure date: yyyy/mm/dd");
				departureDate = checkDateInput();
				continue;
			}
			return departureDate;
		}
	}

	private LocalDate checkDateInput() {
		String dateString = "";
		dateString = input.nextLine().replaceAll("/", "-");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateString, formatter);

		} catch (Exception e) {
			System.out.println("Please enter a valid date!\nPlease use this format: yyyy/mm/dd");
			checkDateInput();
			
		}
		if(date.compareTo(LocalDate.now()) < 0) {
			System.out.println("Cannot book a past date.\nPlease enter a valid date: yyyy/mm/dd");
			checkDateInput();
		}
		return date;
	}
	
	private List<Site> getAvailableSites(LocalDate arrivalDate, LocalDate departureDate, Campground campground) {
		List<Site> availableSites = new ArrayList<>();
		int arrivalMonth = arrivalDate.getMonthValue();
	
		int departureMonth = departureDate.getMonthValue();
		
		int campgroundId = campground.getCampgroundId();
		String sqlGetAvailableSites =     "select * "
										+ "from site "
										+ "where campground_id = ? "
										+ "and not site_id in (select s.site_id "
										 					+ "from site s "
										 					+ "join reservation r "
									 				     	+ "on s.site_id = r.site_id "									 				
										 					+ "where (? >= from_date and ? <= to_date) "
										 					+ "or (? <= from_date and ? >= to_date)) "		
										+ "limit 5";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, 
																arrivalDate, departureDate, 
																arrivalDate, departureDate
																);
		while(results.next()) {
			Site current = new Site();
			current.setSiteId(results.getInt(1));
			current.setCampgroundId(results.getInt(2));
			current.setSiteNumber(results.getInt(3));
			current.setMaxOccupancy(results.getInt(4));
			current.setAccessible(results.getBoolean(5));
			current.setMaxRvLength(results.getInt(6));
			current.setUtilities(results.getBoolean(7));
			availableSites.add(current);
		}
		
		
		int openFrom = campground.getMonthNumberFrom();
		
		int openTo = campground.getMonthNumberTo();
		
		if((arrivalMonth > openFrom && departureMonth > openTo) || (arrivalMonth < openFrom && departureMonth > openTo) || (arrivalMonth < openFrom && departureMonth < openTo)) {
			List<Site> empty = new ArrayList<>();  
			return empty;  
		}
		
		
		
		
		return availableSites;
		
	}
	
	private void displayAvailableSites(List<Site> availableSites, BigDecimal totalCost) {
		
		
		String format = "%-10s%-15s%-15s%-15s%-15s%-15s";
		System.out.println("\n\nResults Matching Your Search Criteria\n");
		System.out.println(String.format(format, "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost"));
		for (int i = 0; i < availableSites.size(); i++) {
			Site current = availableSites.get(i);
			System.out.println(String.format(format, current.getSiteNumber(), current.getMaxOccupancy(), current.isAccessible(),
												current.getMaxRvLength(), current.isUtilities(), "$" + totalCost.toString()));
		}
		System.out.println();
		
	}
  
	@Override
	public void viewReservations(List<Campground> thisCampgroundList) {
		int campgroundId = 0;
		Campground selectedCampground = null;
		campgroundId = input.nextInt();  
		input.nextLine();
		if (campgroundId == 0) {
			return;
		}
		for (int i = 0; i < thisCampgroundList.size(); i++) {
			Campground currentCampground = thisCampgroundList.get(i);
			if (campgroundId == currentCampground.getCampgroundId()) {
				selectedCampground = currentCampground;
			}
		}
		try{int test  = selectedCampground.getCampgroundId();
		}catch (NullPointerException n) {
			System.out.println("Please select one of the campgrounds numbers on your screen! (enter 0 to cancel)");
			viewReservations(thisCampgroundList);
		}
		int selectedId = selectedCampground.getCampgroundId();
		System.out.println("\n");
		List<Reservation> availableSites = new ArrayList<>();
		String sqlViewReservations =    "select \n" + 
										"*\n" + 
										"from \n" + 
										"reservation s\n " 
									  + "where from_date <= now() + INTERVAL '30 days' "
									  + "and site_id in "
									  + "(select site_id "
									  + "from site "
									  + "where "
									  + "campground_id = ?)";
								
		int two = 2;
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlViewReservations, selectedId);  
		while(results.next()) {
			Reservation current = new Reservation();
			current.setReservationId(results.getInt(1));
			current.setSiteId(results.getInt(2));
			current.setName(results.getString(3));
			current.setArrivalDate(results.getDate(5).toLocalDate());
			current.setDepartureDate(results.getDate(6).toLocalDate());
			availableSites.add(current);	
		}
		String format = "%-10s%-40s%-20s%-20s";
		System.out.println(String.format(format, "SiteID", "Group Name", "Arrival Date", "Departure Date"));
		for(Reservation current : availableSites) {
			int siteId = current.getSiteId();
			String name = current.getName();
			LocalDate arrival = current.getArrivalDate();
			LocalDate departure = current.getDepartureDate();
			
			System.out.println(String.format(format, siteId, name, arrival, departure));
			
		}
		System.out.println("\n");
	}
}  
