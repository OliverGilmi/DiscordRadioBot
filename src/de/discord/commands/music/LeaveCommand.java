package de.discord.commands.music;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.other.Utils;
import de.discord.music.AudioPlayerSendHandler;
import de.discord.music.MusicController;
import de.discord.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@CommandAnnotation
public class LeaveCommand implements Command, SlashCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

		//Error checking
		if (Utils.checkUserPermission(channel, m, this.getClass())) return;

		VoiceChannel vc;
		if ((vc = Objects.requireNonNull(m.getVoiceState()).getChannel()) == null) {
			Error.set(channel.getIdLong(), Error.Type.NoVC, getClass().getSimpleName());
			return;
		}

		if (!vc.getMembers().contains(m.getGuild().getMember(m.getJDA().getSelfUser()))) {
			Error.set(channel.getIdLong(), Error.Type.DifferentVC, getClass().getSimpleName());
			return;
		}


		MusicController controller = RadioBot.INSTANCE.playerManager.getController(vc.getGuild());
		AudioManager manager = vc.getGuild().getAudioManager();

		manager.closeAudioConnection();
		MusicUtil.reduceHandler(m.getGuild());

		controller.player = null;
		controller.currentStation = null;

		manager.setSendingHandler(null);

		if (message != null)
			message.addReaction("U+23CF").queue();

	}

	@Override
	public @NotNull String getName() {
		return "leave";
	}

	@Override
	public @NotNull Permission[] getUserPermission() {
		return new Permission[0];
	}

	@Override
	public @NotNull Permission[] getBotPermission() {
		return new Permission[0];
	}

	@Override
	public @NotNull String[] getAliases() {
		return new String[]{"l"};
	}

	@Override
	public @NotNull EmbedBuilder getHelp(String prefix) {
		return getDefaultHelp(prefix).appendDescription(desc + "This command lets "+ RadioBot.INSTANCE.getName() +" leave your channel");
	}

	@Override
	public void executeSlashCommand(SlashCommandEvent e) {

		if (e.getChannelType() != ChannelType.TEXT) {
			e.reply("This command can only be used on Servers.").queue();
			return;
		}

		VoiceChannel vc;
		if ((vc = Objects.requireNonNull(e.getMember().getVoiceState()).getChannel()) == null) {
			Error.set(e.getTextChannel(), Error.Type.NoVC, getClass().getSimpleName(), e);
			return;
		}

		if (!vc.getMembers().contains(e.getGuild().getSelfMember())) {
			Error.set(e.getTextChannel(), Error.Type.DifferentVC, getClass().getSimpleName(), e);
			return;
		}

		performCommand(Objects.requireNonNull(e.getMember()), e.getTextChannel(), null, null);

		e.reply("Successfully left the channel!").queue(interactionHook -> interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));

	}

	@Override
	public @NotNull String getDescription() {
		return "This command lets "+ RadioBot.INSTANCE.getName() +" leave your channel";
	}

	@Override
	public OptionData[] getOptionData() {
		return new OptionData[0];
	}
}