package edu.sjsu.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class JobApplicationIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

		Random rand = new Random();
		char c1 = (char) (rand.nextInt(26) + 'A');
		char c2 = (char) (rand.nextInt(26) + 'A');
		char c3 = (char) (rand.nextInt(26) + 'A');
		Connection connection = session.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select count(*) as total from job_posting");

			if (rs.next()) {
				int id = rs.getInt(1) + 1000;
				String generatedId = "APP" + new Integer(id).toString() + c1 + "" + c2 + "" + c3;
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
