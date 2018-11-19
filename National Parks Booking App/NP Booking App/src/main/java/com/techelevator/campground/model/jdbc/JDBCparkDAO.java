package com.techelevator.campground.model.jdbc;

import java.util.List;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCparkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;
	public JDBCparkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

}  
	@Override
	public Park getParkInfo(String park) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		DecimalFormat df = new DecimalFormat("###,###,###");
		Park thisPark = null;
		String sqlGetParkInfo = "SELECT * FROM park WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkInfo, park);
		while(results.next()) {
			thisPark = mapRowToPark(results);
		}
		String date = thisPark.getEstablishDate().format(formatter);
		String format = "%-20s%-20s%n%-20s%-20s%n%-20s%-20s%n%-20s%-20s%n%n%-20s%n%n";
		String info = String.format(format, "Location:", thisPark.getLocation(),"Established:", date, "Area:", df.format(thisPark.getArea())+" sq km", "Annual Visitors:", df.format(thisPark.getVisitors()), thisPark.getDescription());
		System.out.print(info);
		return thisPark; 
	}
	private Park mapRowToPark(SqlRowSet results) {
		Park thisPark = new Park();
		thisPark.setArea(results.getInt("area"));
		thisPark.setEstablishDate(results.getDate("establish_date").toLocalDate());
		thisPark.setDescription(results.getString("description"));
		thisPark.setLocation(results.getString("location"));
		thisPark.setName(results.getString("name"));
		thisPark.setParkId(results.getInt("park_id"));
		thisPark.setVisitors(results.getInt("visitors"));
		return thisPark;
	}
	
}
