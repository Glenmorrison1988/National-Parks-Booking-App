package com.techelevator.campground.model;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds(Park park);

}
