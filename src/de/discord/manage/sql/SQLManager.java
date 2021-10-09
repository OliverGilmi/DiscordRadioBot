package de.discord.manage.sql;

public class SQLManager {
	
	public static void onCreate() {

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS prefix(guildid INTEGER PRIMARY KEY UNIQUE, pre VARCHAR)");

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS setting(guildid INTEGER UNIQUE, setting BLOB)");

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS radiostation(name VARCHAR UNIQUE, url VARCHAR, title VARCHAR, country VARCHAR)");

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS support(memberid INTEGER, helpchannelid INTEGER, progress INTEGER, type VARCHAR)");

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS rejoin(channelid INTEGER, radio VARCHAR)");

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS getsupportban(userid INTEGER)");
	}
}
