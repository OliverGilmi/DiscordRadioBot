package de.discord.manage.sql;

import de.discord.manage.managers.Settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;

public class LiteSQL {
	
	public static Connection conn;
	public static Statement stmt;
	
	public static void connect() {
		conn = null;
		
		try {
			File file = new File("radio.db");
			if(!file.exists()) {
				System.out.println(file.createNewFile() ? "[SQL] INFO Datatabase created." : "[SQL] ERROR Can't create database.");
			}
			
			String url = "jdbc:sqlite:" + file.getPath();
			conn = DriverManager.getConnection(url);
			
			System.out.println("[SQL] INFO Connection established.");
			
			stmt = conn.createStatement();
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}

		SQLManager.onCreate();
	}
		
	public static void disconnect() {
		try {
			if(conn != null) {
				conn.close();
				System.out.println("[SQL] INFO Connection terminated.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void onUpdate(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void preparedUpdate(String sql, Object... inserts) {

		try {

			PreparedStatement stm = conn.prepareStatement(sql);
			int i = 1;
			for (Object obj : inserts) {

				if (obj.getClass().equals(String.class)) {
					stm.setString(i, (String) obj);
				} else if (obj.getClass().equals(Long.class)) {
					stm.setLong(i, (Long) obj);
				} else if (obj.getClass().equals(Integer.class)) {
					stm.setLong(i, (Integer) obj);
				} else if (obj.getClass().equals(Boolean.class)) {
					stm.setBoolean(i, (Boolean) obj);
				} else if (obj.getClass().equals(Settings.GuildSettings.class)) {
					stm.setBytes(i, makebyte(obj));
				} else {
					throw new IllegalArgumentException("Wrong Object. " + obj.getClass());
				}
				i++;

			}
			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet preparedQuery(String sql, Object... inserts) {

		try {

			PreparedStatement stm = conn.prepareStatement(sql);
			int i = 1;
			for (Object obj : inserts) {

				if (obj.getClass().equals(String.class)) {
					stm.setString(i, (String) obj);
				} else if (obj.getClass().equals(Long.class)) {
					stm.setLong(i, (Long) obj);
				} else if (obj.getClass().equals(Integer.class)) {
					stm.setLong(i, (Integer) obj);
				} else if (obj.getClass().equals(Boolean.class)) {
					stm.setBoolean(i, (Boolean) obj);
				} else {
					throw new IllegalArgumentException("Not supported Class");
				}
				i++;
			}

			return stm.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] makebyte(Object modeldata) {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(modeldata);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResultSet onQuery(String sql) {
		
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}

