package de.discord.music;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.discord.core.RadioBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AudioLoadResult implements AudioLoadResultHandler {

	private final MusicController controller;
	private final TextChannel channel;
	private final SlashCommandEvent event;
	private final Member member;

	public AudioLoadResult(@Nonnull MusicController controller, @Nullable TextChannel channel, @Nullable SlashCommandEvent event, @Nullable Member member) {
		this.controller = controller; 
		this.channel = channel;
		this.event = event;
		this.member = member;

	}

	@Override
	public void trackLoaded(AudioTrack track) {

		controller.getPlayer().playTrack(track);

		if (member != null) {
			if (event != null)
				event.replyEmbeds(controller.currentStation.getEmbed(member).build()).queue();
			else if (channel != null)
				channel.sendMessageEmbeds(controller.currentStation.getEmbed(member).build()).queue();
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
			RadioBot.INSTANCE.logger.warning("[LOAD] INFO Playlist Loaded for some reason? Playlist name: " + playlist.getName());
	}


	public void noMatches() {
		MusicUtil.sendEmbed(this.controller.getGuild().getIdLong(), (new EmbedBuilder()).setDescription(":information_source: Error **210**\n:warning: **Station can't be played.**\nThe playing radio has stopped unexpectedly and can't be loaded anymore. Please try loading it one more time yourself").setFooter("Error-ID: 210 | Module: AUDIOLOADRESULT\nGuild-ID: " + this.controller.getGuild().getId()).setColor(Color.red));
		RadioBot.INSTANCE.logger.warning("[LOAD] WARN No matches in " + this.controller.getGuild().getName());
	}

	public void loadFailed(FriendlyException exception) {
		MusicUtil.sendEmbed(this.controller.getGuild().getIdLong(), (new EmbedBuilder()).setDescription(":information_source: Error **210**\n:warning: **Station can't be played.**\nThe playing radio has stopped unexpectedly and can't be loaded anymore. Please try loading it one more time yourself.").setFooter("Error-ID: 210 | Module: AUDIOLOADRESULT\nGuild-ID: " + this.controller.getGuild().getId()).setColor(Color.red));
		RadioBot.INSTANCE.logger.warning("[LOAD] WARN Load failed! Info: " + exception.getMessage() + " " + exception.getCause());
	}

}
