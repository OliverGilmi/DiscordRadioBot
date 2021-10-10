package de.discord.music;

import java.util.concurrent.ConcurrentHashMap;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.discord.core.RadioBot;
import de.discord.listener.LeaveListener;
import de.discord.manage.managers.RadioStation;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PlayerManager {

	public ConcurrentHashMap<Long, MusicController> controller;

	public ConcurrentHashMap<RadioStation, AudioPlayer> players;
	public ConcurrentHashMap<AudioPlayer, AudioPlayerSendHandler> sendHandlers;

	public PlayerManager() {
		this.controller = new ConcurrentHashMap<>();
		this.players = new ConcurrentHashMap<>();
		this.sendHandlers = new ConcurrentHashMap<>();
	}

	public MusicController getController(Guild guild) {
		MusicController mc;

		if (this.controller.containsKey(guild.getIdLong())) {
			mc = this.controller.get(guild.getIdLong());
		} else {
			mc = new MusicController(guild);
			this.controller.put(guild.getIdLong(), mc);
		}
		return mc;
	}

	public void playRadio(Guild guild, RadioStation station, TextChannel channel, SlashCommandEvent event, Member member) {


		AudioPlayer player = players.get(station);
		MusicController controller = getController(guild);

		controller.currentStation = station;
		controller.player = player;

		if (player == null) {
			player = RadioBot.INSTANCE.audioPlayerManager.createPlayer();
			player.setVolume(30);
			players.put(station, player);

			controller.player = player;

			RadioBot.INSTANCE.audioPlayerManager.loadItem(station.url, new AudioLoadResult(controller, channel, event, member));

		} else {

			if (event != null)
				event.replyEmbeds(station.getEmbed(member).build()).queue();
			else if (channel != null)
				channel.sendMessageEmbeds(station.getEmbed(member).build()).queue();
		}

		if (guild.getAudioManager().getSendingHandler() != null) {
			((AudioPlayerSendHandler) guild.getAudioManager().getSendingHandler()).max--;
			guild.getAudioManager().setSendingHandler(null);
		}

		boolean b = false;

		AudioPlayerSendHandler handler = sendHandlers.get(player);
		if (handler == null) {
			handler = new AudioPlayerSendHandler(player);
			sendHandlers.put(player, handler);
		} else {
			b = true;
		}

		guild.getAudioManager().setSendingHandler(handler);
		if (b)
			handler.max++;

	}

}