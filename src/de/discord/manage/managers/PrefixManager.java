package de.discord.manage.managers;

import de.discord.manage.sql.LiteSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrefixManager {

	
	private Map<Long, String> prefixes;
	public final String standard;
	
	
	public PrefixManager(String standardPrefix) {

		this.standard = standardPrefix;

		prefixes = new ConcurrentHashMap<>();
		
		ResultSet set = LiteSQL.onQuery("SELECT * FROM prefix");
		
		try {
			while (set.next()) {
				
				prefixes.put(set.getLong("guildid"), set.getString("pre"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getPrefix(long guildid) {
		
		return prefixes.getOrDefault(guildid, this.standard);
			
	}
	
	public void newPrefix(long guildid, String prefix) {
		
		prefixes.put(guildid, prefix);
		
		LiteSQL.onUpdate("INSERT OR REPLACE INTO prefix(guildid, pre) VALUES(" + guildid + ", '" + prefix + "')");
		
	}	
}