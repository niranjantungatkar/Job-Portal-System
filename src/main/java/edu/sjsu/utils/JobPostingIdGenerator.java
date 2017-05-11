package edu.sjsu.utils;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.sql.Statement;
import java.sql.Connection;

public class JobPostingIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

		Random rand = new Random();
		char c1 = (char) (rand.nextInt(26) + 'A');
		char c2 = (char) (rand.nextInt(26) + 'A');
		char c3 = (char) (rand.nextInt(26) + 'A');
		char c4 = (char) (rand.nextInt(26) + 'A');
		Connection connection = session.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select count(*) as total from job_posting");

			if (rs.next()) {
				int id = rs.getInt(1) + 1000;
				String generatedId = c1 + "" + c2 + new Integer(id).toString() + c3 + "" + c4;
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
