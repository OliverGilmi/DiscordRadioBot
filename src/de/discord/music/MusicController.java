package de.discord.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.discord.core.RadioBot;
import de.discord.manage.managers.RadioStation;
import net.dv8tion.jda.api.entities.Guild;


public class MusicController {
	
	private final Guild guild;
	public AudioPlayer player;
	public RadioStation currentStation;

	public MusicController(Guild guild) {
		this.guild = guild;
	}

	public void setMaintenancePLayer() {
		this.player.addListener(new AudioEventAdapter() {
			@Override
			public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

				if (currentStation == null) return;

				RadioBot.INSTANCE.playerManager.playRadio(guild, currentStation, null, null, null);

			}
		});
	}

	public Guild getGuild() {
		return guild;
	}

	public AudioPlayer getPlayer() {
		return player;
	}
}