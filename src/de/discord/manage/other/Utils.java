package de.discord.manage.other;

import de.discord.commands.normal.RadioCommand;
import de.discord.commands.normal.SettingsCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.managers.Settings;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class Utils {


    public static void SetupStuff() {
        SettingsCommand.setup();
        RadioCommand.setup();
        RadioBot.INSTANCE.logger.info("[UTILS] INFO Setup finished.");
    }

    public static boolean checkUserPermission(TextChannel channel, Member member, Class<?> c) {

        long guildid = channel.getGuild().getIdLong();

        if ((boolean) RadioBot.INSTANCE.settings.getValue(guildid, Settings.Setting.AdminOnly)) {

            if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                Error.set(channel, Error.Type.UserPermLack, c.getSimpleName(), "ADMINISTRATOR");
                return true;
            }

        }

        Role role = (Role) RadioBot.INSTANCE.settings.getValue(guildid, Settings.Setting.MusicRole);

        if (role != null) {
            if (!member.getRoles().contains(role) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                Error.set(channel, Error.Type.NoRole, c.getSimpleName(), role.getAsMention());
                return true;
            }
        }

        return false;
    }
}
