package de.discord.listener;

import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.music.LoadChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {

	public static String v;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		String message = event.getMessage().getContentDisplay();

		if (event.getMember() == null) return;

		if (event.getMember().getUser().isBot()) return;

		if (event.isFromType(ChannelType.TEXT)) {

			String prefix = RadioBot.INSTANCE.getPrefixMan().getPrefix(event.getGuild().getIdLong());

			TextChannel channel = event.getTextChannel();

			if (!channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE))
				return;

			if (!channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_EMBED_LINKS))
				return;


			if (message.startsWith(prefix)) {
				String[] args = message.substring(prefix.length()).split(" ");

				if (args.length > 0)
					if (!RadioBot.INSTANCE.getCmdMan().performCommand(args[0], event.getMember(), channel, event.getMessage(), prefix))
						if (args.length == 1)
							RadioBot.INSTANCE.radioMan.TriggerRadio(args[0], event.getTextChannel(), event.getMember(), event.getGuild(), null);

			} else {
				String[] args = event.getMessage().getContentRaw().split(" ");

				if (args[0].equalsIgnoreCase("<@!" + event.getJDA().getSelfUser().getIdLong() + ">") || args[0].equalsIgnoreCase("<@" + event.getJDA().getSelfUser().getIdLong() +">")) {


					if (args.length == 3) {
						if (args[1].equalsIgnoreCase("prefix")) {
							if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

								if (args[2].length() <= 5) {

									RadioBot.INSTANCE.prefixMan.newPrefix(event.getGuild().getIdLong(), args[2]);
									EmbedBuilder builder = new EmbedBuilder();
									builder.setDescription("Set the new prefix from `" + prefix + "` to `" + args[2] + "` !");
									builder.setColor(Color.green);
									channel.sendMessageEmbeds(builder.build()).queue();

								} else {

									Error.set(channel, Error.Type.Prefix, getClass().getSimpleName());
								}
							} else {
								Error.set(channel, Error.Type.UserPermLack, getClass().getSimpleName());
							}
						}
					} else if (args.length == 1) {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setAuthor(RadioBot.INSTANCE.getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
						builder.setTitle("Hey " + event.getMember().getEffectiveName() + "!");
						builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
						builder.setDescription("I'm " + event.getJDA().getSelfUser().getAsMention() + "! \n \nI'm in " + event.getJDA().getGuilds().size() + " Guilds \n My prefix here is " + prefix + " \n \n You can change it by typing '" + ((Member)event.getMessage().getMentionedMembers().get(0)).getAsMention() + " prefix <new prefix>' \n \n To see a list of commands type: " + prefix + "help");
						builder.setColor(Color.red);
						channel.sendMessageEmbeds(builder.build()).queue();
					} else {

						Error.set(channel, Error.Type.MentionSyntax, getClass().getSimpleName());
					}
				}
			}
		}
	}

	@Override
	public void onGuildVoiceDeafen(@NotNull GuildVoiceDeafenEvent event) {

		if (event.getMember().equals(event.getGuild().getSelfMember()) && Objects.requireNonNull(event.getMember().getVoiceState()).inVoiceChannel() && event.getMember().hasPermission(Permission.VOICE_DEAF_OTHERS))
			event.getMember().deafen(true).queue();

	}

	@Override
	public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

		if (event.getMember().equals(event.getGuild().getSelfMember()) && event.getMember().hasPermission(Permission.VOICE_DEAF_OTHERS))
			event.getMember().deafen(true).queue();

	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		System.out.println("[main] INFO " + RadioBot.INSTANCE.getName() +  " online.");
		Error.setup();
		LoadChannel.start();
	}

	@Override
	public void onButtonClick(@NotNull ButtonClickEvent event) {
		RadioBot.INSTANCE.getCmdMan().performButton(event);
	}

	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		RadioBot.INSTANCE.getCmdMan().performSlash(event);
	}
}