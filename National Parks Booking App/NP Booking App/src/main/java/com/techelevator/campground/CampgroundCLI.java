package com.techelevator.campground;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.menu.CampgroundMenu;
import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCcampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCparkDAO;
import com.techelevator.campground.model.jdbc.JDBCreservationDAO;
import com.techelevator.campground.model.jdbc.JDBCsiteDAO;

public class CampgroundCLI {
	private static final String MAIN_MENU_OPTION_ACADIA = "Acadia";
	private static final String MAIN_MENU_OPTION_ARCHES = "Arches";
	private static final String MAIN_MENU_OPTION_CUYAHOGA_NATIONAL_VALLEY_PARK = "Cuyahoga National Valley Park";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_ACADIA, MAIN_MENU_OPTION_ARCHES,
			MAIN_MENU_OPTION_CUYAHOGA_NATIONAL_VALLEY_PARK, MAIN_MENU_OPTION_EXIT };
	private static final String PARK_MENU_OPTION_VIEW = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH = "Search for Reservations";
	private static final String PARK_MENU_OPTION_RETURN = "Return to Previous Menu";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_VIEW, PARK_MENU_OPTION_SEARCH, 
			 PARK_MENU_OPTION_RETURN };
	private static final String CAMPGROUND_MENU_OPTION_SEARCH = "Search for Reservations";
	private static final String CAMPGROUND_MENU_OPTION_VIEW_RESERVATIONS = "View a Campgrounds Upcoming Reservations";
	private static final String CAMPGROUND_MENU_OPTION_RETURN = "Return to Previous Menu";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_SEARCH, 
			CAMPGROUND_MENU_OPTION_VIEW_RESERVATIONS, CAMPGROUND_MENU_OPTION_RETURN };

	private CampgroundMenu menu;
	private CampgroundDAO campgroundDAO;
	private ParkDAO parkDAO;
	private SiteDAO siteDAO;  
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		this.menu = new CampgroundMenu(System.in, System.out);
		campgroundDAO = new JDBCcampgroundDAO(dataSource);
		parkDAO = new JDBCparkDAO(dataSource);
		siteDAO = new JDBCsiteDAO(dataSource);
		reservationDAO = new JDBCreservationDAO(dataSource);
	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText); 
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	public void run() {
		while (true) {
			printHeading("View Parks Interface");
			System.out.print("Select a Park for Further Details");
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_ACADIA)) {
				handlePark("Acadia");
			} else if (choice.equals(MAIN_MENU_OPTION_ARCHES)) {
				handlePark("Arches");
			} else if (choice.equals(MAIN_MENU_OPTION_CUYAHOGA_NATIONAL_VALLEY_PARK)) {
				handlePark("Cuyahoga Valley");
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Goodbye!");
				System.exit(0);
			}
		}
	}

	private void handlePark(String park) {
		printHeading("Park Information Screen");
		System.out.print(park + " National Park\n");
		Park thisPark = parkDAO.getParkInfo(park);
		while (true) {
			System.out.println("Select a Command");
			parkMenu(park, thisPark);
		}
	}

	private void parkMenu(String park, Park thisPark) {
		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if (choice.equals(PARK_MENU_OPTION_VIEW)) {
			printHeading("Park Campgrounds");
			System.out.println(park + " National Park Campgrounds\n");
			campgroundDAO.getAllCampgrounds(thisPark);
			String choice2 = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
			if (choice2.equals(CAMPGROUND_MENU_OPTION_SEARCH)) {
				searchForReservations(thisPark);  
				run();
				}else if (choice2.equals(CAMPGROUND_MENU_OPTION_VIEW_RESERVATIONS)){
						viewReservations(thisPark);
				} else if (choice2.equals(CAMPGROUND_MENU_OPTION_RETURN)) {
						parkMenu(choice2, thisPark); 
				}
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH)) {
				searchForReservations(thisPark);
				run();
		} else if (choice.equals(PARK_MENU_OPTION_RETURN)) {
		}
				run();
		
		
	}

	private void searchForReservations(Park thisPark) {
		printHeading("Search for Campground Reservation");
		List<Campground> thisCampgroundList = campgroundDAO.getAllCampgrounds(thisPark);
		reservationDAO.getAvailableReservations(thisCampgroundList);
	}
	private void viewReservations(Park thisPark) {
		printHeading("View Campground Reservations");
		List<Campground> thisCampgroundList = campgroundDAO.getAllCampgrounds(thisPark);
		System.out.println("Which Campground's Reservations Would You Like To See (enter 0 to cancel)?");
		reservationDAO.viewReservations(thisCampgroundList);
	}
	

	  
}
