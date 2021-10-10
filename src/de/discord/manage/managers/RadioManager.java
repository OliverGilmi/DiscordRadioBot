package de.discord.manage.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.other.Utils;
import de.discord.manage.sql.LiteSQL;
import de.discord.music.MusicController;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;

public class RadioManager {

	public Map<String, RadioStation> radios;
	public Map<RadioStation.Country, List<RadioStation>> countryStationList;

	public static Map<String, RadioStation.Country> countryMap;

	public RadioManager() {
		radios = new ConcurrentHashMap<>();
		countryStationList = new ConcurrentHashMap<>();
		countryMap = new ConcurrentHashMap<>();

		for (RadioStation.Country c : RadioStation.Country.values()) {
			countryStationList.put(c, new ArrayList<>());
			countryMap.put(c.name().toLowerCase(), c);
		}

		ResultSet set = LiteSQL.onQuery("SELECT * FROM radiostation");

		try {

			if (set != null) {
				while (set.next()) {

					String name = set.getString("name");
					String url = set.getString("url");
					String title = set.getString("title");
					String countryName = set.getString("country");

					RadioStation.Country country;
					if ((country = countryMap.get(countryName)) != null) {

						RadioStation station = new RadioStation(url, title, country, name);

						radios.put(name, station);

					} else {
						throw new IllegalArgumentException("Country Name wurde nicht gefunden " + countryName);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<String> list = Arrays.asList(radios.keySet().toArray(new String[]{}));

		Collections.sort(list);

		for (String s : list) {

			RadioStation station = radios.get(s);

			countryStationList.get(station.country).add(station);

		}

	}

	public void addRadio(String name, String url, String title, RadioStation.Country country) {
		RadioStation station = new RadioStation(url, title, country, name);

		radios.put(name, station);
		countryStationList.get(country).add(station);

		LiteSQL.preparedUpdate("INSERT INTO radiostation(name, url, title, country) VALUES(?, ?, ?, ?)", name, url, title, country.name().toLowerCase());
	}

	public void addRadioV2(String cmd, String name, RadioStation.Country country, String url) {
		RadioStation station = new RadioStation(url, ":radio: Now playing: " + name, country, cmd);
		radios.put(cmd, station);
		countryStationList.get(country).add(station);

		LiteSQL.preparedUpdate("INSERT INTO radiostation(name, url, title, country) VALUES(?, ?, ?, ?)", cmd, url, ":radio: Now playing: " + name, country.name().toLowerCase());
	}

	public void removeRadio(String name) {

		RadioStation station = radios.remove(name);
		countryStationList.get(station.country).remove(station);

		LiteSQL.preparedUpdate("DELETE FROM radiostation WHERE name = ?", name);
	}


	public void TriggerRadio(String command, TextChannel channel, Member member, Guild guild, @Nullable SlashCommandEvent event) {

		if (member == null) return;

		RadioStation station;

		if ((station = radios.get(command)) != null) {

			if (Utils.checkUserPermission(channel, member,this.getClass())) return;

			if (Objects.requireNonNull(member.getVoiceState()).inVoiceChannel() && guild.getSelfMember().hasPermission(Objects.requireNonNull(member.getVoiceState().getChannel()), Permission.VOICE_SPEAK)) {

				AudioManager manager = guild.getAudioManager();

				if (botAvailable(manager, member)) {

					try {

						manager.openAudioConnection(member.getVoiceState().getChannel());

						RadioBot.INSTANCE.logger.info("[RADIO] INFO " + station.name + " executed by " + member.getUser().getAsTag() + " in " + guild.getName());

						MusicController controller = RadioBot.INSTANCE.playerManager.getController(guild);

						controller.currentStation = station;

						RadioBot.INSTANCE.playerManager.playRadio(guild, station, channel, event, member);

					} catch (InsufficientPermissionException e) {
						Error.set(channel, Error.Type.ChannelFull, getClass().getSimpleName(), event);
					}
				} else {
					Error.set(channel, Error.Type.DifferentVC, getClass().getSimpleName(), event);
				}
			} else {
				Error.set(channel, Error.Type.NoVC, getClass().getSimpleName(), event);
			}
		} else if (event != null) {
			event.reply("This station doesn't exist!\nSee `" + RadioBot.INSTANCE.prefixMan.getPrefix(guild.getIdLong()) + "radio` for every station").queue(interactionHook -> interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
		}
	}


	public static boolean botAvailable(AudioManager manager, Member member) {


		if (manager.getConnectedChannel() == null && !manager.isConnected()) {
			return true;
		}

		boolean b = true;

		for (Member m : manager.getConnectedChannel().getMembers()) {

			if (!m.getUser().isBot()) {
				b = false;
				break;
			}
		}

		if (manager.getConnectedChannel().getMembers().contains(member)) {
			b = true;
		}

		return b;
	}
}