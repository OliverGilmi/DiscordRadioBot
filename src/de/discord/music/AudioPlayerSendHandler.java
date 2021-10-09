package de.discord.music;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;
	private AudioFrame lastFrame;
	public int max = 1;

	public AudioPlayerSendHandler(AudioPlayer player) {

	this.audioPlayer = player;
	}	

	private int i = 1;

	@Override
	public boolean canProvide() {

		if (i < max) {
			i++;
		} else {
			i = 1;
			lastFrame = audioPlayer.provide();
		}

		return lastFrame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {	
		return ByteBuffer.wrap(lastFrame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}
	
	
}
