package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		String result = null;
		//Write your code here
		/*
		Connection connection = getConnection();
		PreparedStatement stmt  = connection.prepareStatement("Select response from SQLDatabaseEngine where keyword =?"); 
		stmt.setString(1, text);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = rs.getString(1);
		}
		return result;
		*/
		try {
			Connection connection = getConnection();
			PreparedStatement stmt  = connection.prepareStatement("Select response from SQLDatabaseEngine where keyword =?"); 
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			log.info("IOException while reading file: {}", e.toString());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (IOException ex) {
				log.info("IOException while closing file: {}", ex.toString());
			}
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
