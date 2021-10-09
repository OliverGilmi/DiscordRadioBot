package de.discord.manage.managers;

import java.awt.Color;
import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class RadioStation {

	public String name;
	public String url;
	public String title;
	public Country country;
	
	public RadioStation(String url, String title, Country country, String name) {
		
		this.url = url;
		this.title = title;
		this.country = country;
		this.name = name;
	}
	
	
	
	public EmbedBuilder getEmbed(Member member) {
		
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle(title);

		builder.setTimestamp(Instant.now());
		builder.setColor(Color.red);
		builder.appendDescription("\n:red_circle: **[Stream](" + url + ")**");
		builder.addBlankField(false);
		builder.setFooter("Requested by " + member.getEffectiveName(), member.getUser().getAvatarUrl());
		
		return builder;


	}
	
	public enum Country {
		Germany, Britain, USA, Spain, France, Italy, Netherlands, Other
	}
}