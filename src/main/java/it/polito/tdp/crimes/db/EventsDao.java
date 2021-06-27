package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;

public class EventsDao
{
	public List<Event> listAllEvents()
	{
		String sql = "SELECT * FROM events";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Event> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
							res.getInt("offense_code_extension"), res.getString("offense_type_id"),
							res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
							res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
							res.getInt("is_crime"), res.getInt("is_traffic")));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(List<String> vertici, String category, int year)
	{
		String sql = "SELECT e.offense_type_id tipo "
					+ "FROM events e "
					+ "WHERE e.offense_category_id = ? "
					+ "		AND YEAR (e.reported_date) = ? "
					+ "GROUP BY tipo"; 
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category);
			st.setInt(2, year);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					String tipo = res.getString("tipo"); 
					if(!vertici.contains(tipo))
						vertici.add(tipo);
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<String> getCategories()
	{
		String sql = "SELECT e.offense_category_id category "
				+ "FROM events e "
				+ "GROUP BY category"; 
		
		List<String> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					String s = res.getString("category");
					if(!list.contains(s))
						list.add(s); 
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> getYears()
	{
		String sql = "SELECT YEAR (e.reported_date) data "
					+ "FROM events e "
					+ "GROUP BY data"; 
		
		List<Integer> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					Integer year = res.getInt("data");
					if(!list.contains(year))
						list.add(year); 
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	} 

	public List<Adiacenza> getAdiacenze(String category, Integer year)
	{
		String sql = "SELECT e1.offense_type_id tipo1, "
					+ "		 e2.offense_type_id tipo2, "
					+ "		 COUNT(DISTINCT e1.district_id) peso "
					+ "FROM events e1, events e2 "
					+ "WHERE e1.offense_type_id < e2.offense_type_id "
					+ "		AND e1.offense_category_id = ? "
					+ "		AND e1.offense_category_id = e2.offense_category_id "
					+ "		AND YEAR (e1.reported_date) = ? "
					+ "		AND YEAR (e2.reported_date) = YEAR (e1.reported_date) "
					+ "		AND e1.district_id = e2.district_id "
					+ "GROUP BY tipo1, tipo2" ;
		
		List<Adiacenza> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category);
			st.setInt(2, year);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					Adiacenza a = new Adiacenza(res.getString("tipo1"), res.getString("tipo2"), res.getInt("peso"));
					if(!list.contains(a))
						list.add(a); 
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		
	}
}
