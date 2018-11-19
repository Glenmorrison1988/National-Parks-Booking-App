package com.techelevator.campground.model.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.campground.model.SiteDAO;

public class JDBCsiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	public JDBCsiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

}
}