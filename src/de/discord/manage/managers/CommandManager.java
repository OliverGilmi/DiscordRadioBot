package de.discord.manage.managers;

import de.discord.commands.types.*;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class CommandManager {

	public ConcurrentHashMap<String, Command> commands;
	public ConcurrentHashMap<String, SlashCommand> slashCommands;
	public ConcurrentHashMap<String, ButtonCommand> buttonCommands;

	public CommandManager() {

		this.buttonCommands = new ConcurrentHashMap<>();
		this.slashCommands = new ConcurrentHashMap<>();
		this.commands = new ConcurrentHashMap<>();

		Reflections reflection = new Reflections("de.discord.commands");

		Set<Class<?>> classes = reflection.getTypesAnnotatedWith(CommandAnnotation.class);

		for (Class<?> c : classes) {

			try {
				Object object = c.getDeclaredConstructor().newInstance();

				if (object instanceof Command && !(object instanceof DoNotAddAsNormalCommand)) {
					Command command = (Command) object;
					commands.put(command.getName(), command);

					for (String name : command.getAliases()) {
						if (name.split("\\s+").length == 1)
							commands.put(name, command);
					}

				}

				if (object instanceof SlashCommand) {

					SlashCommand command = (SlashCommand) object;

					slashCommands.put(command.getName(), command);

					RadioBot.INSTANCE.shardMan.getShards().forEach(jda -> {
						CommandCreateAction action = jda.upsertCommand(command.getName(), command.getDescription());

						if (command.getOptionData() != null && command.getOptionData().length != 0) {
							action = action.addOptions(command.getOptionData());
						}

						action.queue();

					});

				}

				if (object instanceof ButtonCommand) {

					ButtonCommand command = (ButtonCommand) object;

					buttonCommands.put(command.getName(), command);

				}

			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}


		}

	}

	public boolean performCommand(String command, Member m, TextChannel channel, Message message, String prefix) {

		Command cmd;
		if ((cmd = this.commands.get(command.toLowerCase())) != null) {


			if (cmd.getUserPermission() != null) {
				if (!m.hasPermission(cmd.getUserPermission()) && !m.hasPermission(channel, cmd.getUserPermission())) {

					StringBuilder perms = new StringBuilder();

					for (Permission p : cmd.getUserPermission()) {
						perms.append(p.name()).append(", ");
					}

					Error.set(channel, Error.Type.UserPermLack, getClass().getSimpleName(), perms.substring(0, perms.toString().length() - 2));
					return true;
				}
			}

			Member self = m.getGuild().getSelfMember();
			if (cmd.getBotPermission() != null) {
				if (!self.hasPermission(channel, cmd.getBotPermission())) {

					StringBuilder perms = new StringBuilder();

					for (Permission p : cmd.getBotPermission()) {
						perms.append(p.name()).append(", ");
					}

					Error.set(channel, Error.Type.BotPermLack, getClass().getSimpleName(), perms.substring(0, perms.toString().length() - 2));
					return true;
				}
			}
			RadioBot.INSTANCE.logger.info("[main] INFO " + cmd.getClass().getSimpleName() + " executed");
			cmd.performCommand(m, channel, message, prefix);
			return true;
		}

		return false;
	}

	public void performSlash(SlashCommandEvent e) {

		if (e.getGuild() == null) return;

		String name = e.getName();

		SlashCommand command = slashCommands.get(name);

		if (command != null) {
			RadioBot.INSTANCE.logger.info("[main] INFO " + command.getClass().getSimpleName() + " executed ");
			command.executeSlashCommand(e);
		}

	}

	public void performButton(ButtonClickEvent e) {

		if (e.getButton() == null) return;

		String name = e.getButton().getId();

		if (name == null) return;

		name = name.split("_")[0];

		ButtonCommand command = buttonCommands.get(name);

		if (command != null) command.executeButton(e);


	}
}