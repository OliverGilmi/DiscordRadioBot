package de.discord.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Error {

    public enum Type {
        Syntax, NoHelp, Prefix, UserPermLack, BotPermLack, LoadFailed, NoMatches, NoTrackPlaying, InvVolume, DifferentVC, NoVC, BotNoVC, ChannelFull, Unknown, NotANumber, NoSetting, FeedbackLengh, FeedbackBan, MentionSyntax, CurrentlyUsed, OwnerOnly, NoRole, StationLoadFailed
    }

    public static final Map<Short, Type> errormap = new ConcurrentHashMap<>();

    /**
     * Example:
     * Error.set(channel.getIdLong(), Error.Type.TypeName, Permission.EXAMPLE, getClass().getSimpleName())
     *
     *
     *
     * @param t ErrorType-List:
     * Syntax (101), MentionSyntax (102), NoHelp (103), Prefix (104), UserPermLack (105), BotPermLack (106), NotANumber (107), FeedbackLengh (109), NoSetting (109), FeedbackBan (110), LoadFailed (201), NoMatches (203), NoTrackPlaying (204), InvVolume (205), DifferentVC (206), NoVC (207), BotNoVC (208), Unknown (300)
     * @see Type
     *
     */
    public static void set(long ChannelID, Type t, String getClass_getSimpleName, String... args) {

        TextChannel channel = RadioBot.INSTANCE.shardMan.getTextChannelById(ChannelID);

        if (channel == null) return;

        set(channel, t, getClass_getSimpleName, null, args);

    }

    /**
     * Example:
     * Error.set(channel, Error.Type.TypeName, Permission.EXAMPLE, getClass().getSimpleName())
     *
     * @param t ErrorType-List:
     * Syntax (101), NoHelp (102), Prefix (103), UserPermLack (104), BotPermLack (105), NotANumber (106), FeedbackLengh (107), LoadFailed (201), NoMatches (203), NoTrackPlaying (204), InvVolume (205), DifferentVC (206), NoVC (207), BotNoVC (208), Unknown (300)
     *
     * Same as set(ChannelID, type, SimpleName, args) but with TextChannel
     */
    public static void set(TextChannel channel, Type t, String getClass_getSimpleName, String... args) {
        set(channel, t, getClass_getSimpleName, null, args);
    }


    /**
     * Example:
     * Error.set(channel, Error.Type.TypeName, Permission.EXAMPLE, event, getClass().getSimpleName())
     *
     * Allows for replay from a SlashCommand
     *
     * @param t ErrorType-List:
     * Syntax (101), NoHelp (102), Prefix (103), UserPermLack (104), BotPermLack (105), NotANumber (106), FeedbackLengh (107), LoadFailed (201), NoMatches (203), NoTrackPlaying (204), InvVolume (205), DifferentVC (206), NoVC (207), BotNoVC (208), Unknown (300)
     *
     * Same as set(ChannelID, type, SimpleName, args) but with TextChannel
     */
    public static void set(TextChannel channel, Error.Type t, String getClass_getSimpleName, SlashCommandEvent event, String... args) {
        Error e = new Error();
        String CmdName = getClass_getSimpleName.toLowerCase().replace("command", "");
        switch(t) {
            case Syntax:
                e.sendEmbed(channel, getClass_getSimpleName, 101, ":interrobang: **Syntax incorrect!** \nSee `" + RadioBot.INSTANCE.prefixMan.getPrefix(channel.getGuild().getIdLong()) + "help " + CmdName + "` for more information on the correct Syntax.", event);
                break;
            case MentionSyntax:
                e.sendEmbed(channel, getClass_getSimpleName, 102, ":interrobang: **Syntax incorrect!** \nPing " + channel.getJDA().getSelfUser().getAsMention() + " for more help.", event);
                break;
            case NoHelp:
                e.sendEmbed(channel, getClass_getSimpleName, 103, ":question: **There's no advanced help for this command!**", event);
                break;
            case Prefix:
                e.sendEmbed(channel, getClass_getSimpleName, 104, ":exclamation: **This Prefix cannot be used!** \nA valid Prefix for this Bot can only be up to 5 characters long.", event);
                break;
            case UserPermLack:
                e.sendEmbed(channel, getClass_getSimpleName, 105, ":no_pedestrians: **User lacking Permission!** \nYou don't have the Permission(s) `" + args[0] + "`, which is/are required to execute this Command.", event);
                break;
            case BotPermLack:
                e.sendEmbed(channel, getClass_getSimpleName, 106, ":no_mobile_phones: **Bot lacking Permission!** \n" + RadioBot.INSTANCE.getName() + " doesn't have the Permission(s) `" + args[0] + "`, which is/are required to perform this Action linked to the Command.", event);
                break;
            case NotANumber:
                e.sendEmbed(channel, getClass_getSimpleName, 107, ":1234: **Input is not a number!** \nGiven input was not a valid number", event);
                break;
            case FeedbackLengh:
                e.sendEmbed(channel, getClass_getSimpleName, 108, ":warning: **Input is not long enough!** \nYour Feedback text must be longer than 10 characters.", event);
                break;
            case NoSetting:
                e.sendEmbed(channel, getClass_getSimpleName, 109, ":wrench: **No Setting found!** \nThere was no setting found, which you can edit, with the Name: \"" + args[0] + "\"", event);
                break;
            case FeedbackBan:
                e.sendEmbed(channel, getClass_getSimpleName, 110, ":x: **Banned!** \n" + channel.getGuild().getName() + " is banned from submitting feedback", event);
                break;
            case OwnerOnly:
                e.sendEmbed(channel, getClass_getSimpleName, 111, ":x: **Prohibited!**\nOnly the Owner of this Bot can execute this command!", event);
                break;
            case NoRole:
                e.sendEmbed(channel, getClass_getSimpleName, 110, ":warning: **No user role!**\nYou don't have the required role (" + args[0] + ") to execute this command.", event);
                break;
            case LoadFailed:
                e.sendEmbed(channel, getClass_getSimpleName, 201, ":x: **Load failed!** \nThe Song you tried to play couldn't be loaded and therefore not played either. This Error can be caused through a Bot- but also Source-Side Error. Simply try loading that Song again.", event);
                break;
            case NoMatches:
                e.sendEmbed(channel, getClass_getSimpleName, 202, ":warning: **No Track found!** \nNo Song with that name could be found. If you know that this Song exists, simply try playing that Song again.", event);
                break;
            case NoTrackPlaying:
                e.sendEmbed(channel, getClass_getSimpleName, 203, ":stop_button: **No Song is playing!**\n Therefore, no Track-Info can be shown and the playback can't be stopped.", event);
                break;
            case InvVolume:
                e.sendEmbed(channel, getClass_getSimpleName, 204, ":mute: **Invalid Volume!** \nPlease pick a Volume in between 1 to 100.", event);
                break;
            case DifferentVC:
                e.sendEmbed(channel, getClass_getSimpleName, 205, ":busts_in_silhouette: **Different VoiceChannels!** \n" + RadioBot.INSTANCE.getName() + " is in another VoiceChannel than you. Please join that VoiceChannel or get " + RadioBot.INSTANCE.getName() + " into your VoiceChannel.", event);
                break;
            case NoVC:
                e.sendEmbed(channel, getClass_getSimpleName, 206, ":mute: **User in no VoiceChannel!** \nYou aren't connected to a VoiceChannel, or, " + RadioBot.INSTANCE.getName() + " doesn't have the Permission to join your VoiceChannel. If you want " + RadioBot.INSTANCE.getName() + " to play something for you, please get into a VoiceChannel that " + RadioBot.INSTANCE.getName() + " can access.", event);
                break;
            case BotNoVC:
                e.sendEmbed(channel, getClass_getSimpleName, 207, ":mute: **" + RadioBot.INSTANCE.getName() + " in no VoiceChannel!** \n" + RadioBot.INSTANCE.getName() + " isn't connected to any VoiceChannel, therefore the requested Action cannot be performed.", event);
                break;
            case CurrentlyUsed:
                e.sendEmbed(channel, getClass_getSimpleName, 208, ":warning: **Bot currently used!** \n" + RadioBot.INSTANCE.getName() + " is currently beeing used by at least one other person. Please inform him, if you want to listen to " + RadioBot.INSTANCE.getName() + " yourself.", event);
                break;
            case ChannelFull:
                e.sendEmbed(channel, getClass_getSimpleName, 209, ":warning: **Voice Channel Full!** \nYour voice channel is currently full, so I can't join it.", event);
                break;
            case StationLoadFailed:
                e.sendEmbed(channel, getClass_getSimpleName, 210, ":warning: **Station can't be played.**\nThe playing radio has stopped unexpectedly and can't be loaded anymore. Please try loading it one more time yourself or otherwise DM " + RadioBot.INSTANCE.getName() + " for help of a teammember (see " + RadioBot.INSTANCE.prefixMan.getPrefix(channel.getGuild().getIdLong()) + "getsupport for more info).", event);
                break;
            default:
                e.sendEmbed(channel, getClass_getSimpleName, 300, ":no_entry: **Unknown Error!** \nPlease inform us about this Error immediately if it affects the functionality of " + RadioBot.INSTANCE.getName() + "." + (args != null ? "Message: " + args[1] : ""), event);
        }

    }



    //EmbedBuilder-Standard-Layout
    final String desc = ":information_source: Error %id%\n%desc%";
    final String foot = "Error-ID: %id% | Module: %mod%\nGuild-ID: %g% | Channel-ID: %c%";
    final EmbedBuilder builder = new EmbedBuilder().setColor(0xff0000).setTitle("» An Error occurred!").setDescription(desc).setFooter(foot);



    //EmbedBuilder wird gechanged & gesendet
    public void sendEmbed(TextChannel channel, String mod, int errid, String errdesc, SlashCommandEvent e) {




        long gid = 0;

        if (channel != null) gid = channel.getGuild().getIdLong();

        builder.setDescription(desc.replace("%id%", "**" + errid + "**").replace("%desc%", errdesc));
        builder.setFooter(foot.replace("%id%", Integer.toString(errid)).replace("%mod%", mod.toUpperCase()).replace("%g%", Long.toString(gid)).replace("%c%", String.valueOf(gid)));

        if (e != null) {
            e.replyEmbeds(builder.build()).queue();
        } else if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            RadioBot.INSTANCE.logger.warning("[ERROR] ERROR Error.java Channel null!");
        }
    }

    private static boolean b = false;

    public static void setup() {

        if (b) return;

        errormap.put((short) 101, Type.Syntax);
        errormap.put((short) 102, Type.MentionSyntax);
        errormap.put((short) 103, Type.NoHelp);
        errormap.put((short) 104, Type.Prefix);
        errormap.put((short) 105, Type.UserPermLack);
        errormap.put((short) 106, Type.BotPermLack);
        errormap.put((short) 107, Type.NotANumber);
        errormap.put((short) 108, Type.FeedbackBan);
        errormap.put((short) 109, Type.NoSetting);
        errormap.put((short) 110, Type.FeedbackBan);

        errormap.put((short) 201, Type.LoadFailed);
        errormap.put((short) 202, Type.NoMatches);
        errormap.put((short) 203, Type.NoTrackPlaying);
        errormap.put((short) 204, Type.InvVolume);
        errormap.put((short) 205, Type.DifferentVC);
        errormap.put((short) 206, Type.NoVC);
        errormap.put((short) 207, Type.BotNoVC);
        errormap.put((short) 208, Type.CurrentlyUsed);
        errormap.put((short) 209, Type.ChannelFull);

        errormap.put((short) 300, Type.Unknown);

        if (errormap.values().containsAll(Arrays.asList(Type.values().clone()))) {
            System.out.println("[ERROR] WARN NOT EVERY ERROR IS REGISTERED");
        }
        b = true;
    }
}