package de.discord.manage.managers;

import de.discord.core.RadioBot;
import de.discord.manage.sql.LiteSQL;
import net.dv8tion.jda.api.entities.Role;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Settings {

    public Map<Long, GuildSettings> guildSettingsMap;

    public Map<String, Setting> settingMap;

    public final static Settings.Setting[] settingsArray = Setting.values();

    public final static List<Setting> boolSettings = Arrays.asList(Setting.AdminOnly, Setting.StageSpeaker);
    public final static List<Setting> roleSettings = Arrays.asList(Setting.MusicRole);

    public Settings() {

        this.guildSettingsMap = new HashMap<>();
        ResultSet set = LiteSQL.onQuery("SELECT * FROM setting");

        try {
            if (set != null) {
                while (set.next()) {

                    GuildSettings settings = (GuildSettings) read(set.getBytes("setting"));

                    long id = set.getLong("guildid");
                    guildSettingsMap.put(id, settings);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.settingMap = new HashMap<>();

        for (Setting setting : settingsArray){

            settingMap.put(setting.name().toLowerCase(), setting);

        }

    }

    public void insertValue(long guildid, Setting setting, Object obj) {

        GuildSettings guildSettings = guildSettingsMap.get(guildid);

        if (guildSettings == null) {
            guildSettings = new GuildSettings(guildid);
            guildSettingsMap.put(guildid, guildSettings);
        }

        guildSettings.UpdateValue(setting, obj);
    }

    public Object getValue(long guildid, Setting setting) {

        GuildSettings guildSettings = guildSettingsMap.get(guildid);
        if (guildSettings != null) {

            return guildSettings.getValue(setting);
        }

        if (boolSettings.contains(setting)) {
            return false;
        }
        return null;
    }

    public enum Setting {
        AdminOnly, MusicRole, StageSpeaker
    }



    public static class GuildSettings implements Serializable {

        public long guildid;
        public List<Setting> settingList;
        public Map<Setting, Object> objectSettings;

        public GuildSettings(long guildid, Setting... settings) {

            this.guildid = guildid;

            settingList = new ArrayList<>();

            settingList.addAll(Arrays.asList(settings));

            objectSettings = new ConcurrentHashMap<>();

        }

        public void UpdateValue(Setting setting, Object obj) {


            switch (setting) {


                //Boolean Settings
                case StageSpeaker:
                case AdminOnly:

                    if (obj instanceof Boolean) {

                        boolean bool = (boolean) obj;

                        if (bool) {
                            settingList.add(setting);
                        } else {
                            settingList.remove(setting);
                        }
                    } else {
                        throw new IllegalArgumentException("This Setting isn't registered here");
                    }
                     break;

                case MusicRole:

                    if (obj == null) {

                        objectSettings.remove(setting);
                        break;
                    }

                    if (obj instanceof Role) {

                        objectSettings.put(setting, obj);

                    } else {
                        throw new IllegalArgumentException(obj.getClass() + " isn't a supported class");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("This Setting isn't registered here");
            }



            LiteSQL.preparedUpdate("INSERT OR REPLACE INTO setting(guildid, setting) VALUES(?,?)", guildid, this);
        }

        public Object getValue(Setting setting) {

            switch (setting) {
                case AdminOnly:
                case StageSpeaker:

                    return settingList.contains(setting);

                case MusicRole:

                    return objectSettings.get(setting);

                default:
                    throw new IllegalArgumentException("This Setting isn't registered here");

            }

        }


        private void writeObject(java.io.ObjectOutputStream out) throws IOException {

            Map<Setting, Object> map = new ConcurrentHashMap<>();

            for (Setting setting : objectSettings.keySet()) {

                switch (setting) {

                    case MusicRole:

                        Role role = (Role) objectSettings.get(setting);

                        map.put(setting, role.getIdLong());
                        break;

                    default:
                        throw new IllegalArgumentException("No Case for this Setting was created!");
                }
            }

            out.writeObject(map);


            out.writeLong(guildid);
            out.writeObject(settingList);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

            if (objectSettings == null)
                objectSettings = new ConcurrentHashMap<>();

            if (settingList == null)
                settingList = new ArrayList<>();


            Map<Setting, Object> map = (Map<Setting, Object>) in.readObject();



            for (Setting setting : map.keySet()) {

                switch (setting) {

                    case MusicRole:

                        Long l = (Long) map.get(setting);

                        Role role = RadioBot.INSTANCE.shardMan.getRoleById(l);

                        if (role != null) {
                            objectSettings.put(setting, role);
                        }

                        break;
                    default:
                        throw new IllegalArgumentException("No Case for this Setting was created!");
                }
            }

            guildid = in.readLong();
            settingList = (List<Setting>) in.readObject();
        }
    }
    private Object read(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            return ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}