package com.techelevator.campground.model;

import java.util.List;

public interface ReservationDAO {

	public void getAvailableReservations(List<Campground> thisCampgroundList);

	public void viewReservations(List<Campground> campgrounds);



}
