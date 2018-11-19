package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;

public class JDBCcampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;
	public JDBCcampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	} 
	@Override
	public List<Campground> getAllCampgrounds(Park park) {
		List<Campground> allCampgrounds = new ArrayList<>();
		String sqlGetAllCampgrounds = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds, park.getParkId());
		while(results.next()) {
			Campground thisCampground = mapRowToCampground(results);
			allCampgrounds.add(thisCampground);
		}  
		
		String format = "%-5s%-30s%-15s%-15s%-20s";
		System.out.println(String.format(format, "", "Name", "Open","Close", "Daily Fee"));
		for (int i = 0; i < allCampgrounds.size(); i++) {
			System.out.println(String.format(format,"#"+ allCampgrounds.get(i).getCampgroundId(), allCampgrounds.get(i).getName(), allCampgrounds.get(i).getOpenFromMonth(), allCampgrounds.get(i).getOpenToMonth(), "$" + allCampgrounds.get(i).getDailyFeeString() ));
		}
		System.out.println();
		
		return allCampgrounds;
		
	}
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground thisCampground = new Campground();
		thisCampground.setCampgroundId(results.getInt("campground_id"));
		thisCampground.setParkId(results.getInt("park_id"));
		thisCampground.setName(results.getString("name"));
		thisCampground.setOpenFrom(results.getString("open_from_mm"));
		thisCampground.setOpenTo(results.getString("open_to_mm"));
		thisCampground.setDailyFee(results.getBigDecimal("daily_fee"));
		
		return thisCampground;
	}
	
	
}
