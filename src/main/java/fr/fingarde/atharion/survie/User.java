package fr.fingarde.atharion.survie;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class User
{
    public static ArrayList<User> users = new ArrayList<>();

    private UUID uuid;

    private String nickname;
    private String prefix;
    private String suffix;
    private String displayName;
    private Player player;

    public User(UUID uuid)
    {
        this.uuid = uuid;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Players_S WHERE uuid = '" + this.uuid.toString() + "'");

            if (!result.next()) { return; }

            this.nickname = result.getString("nickname");
            this.prefix = result.getString("prefix");
            this.suffix = result.getString("suffix");

            this.player = Bukkit.getPlayer(this.uuid);

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players_S SET prefix = '" + this.prefix.replaceAll("'", "\\\\'") + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players_S SET suffix = '" + this.suffix.replaceAll("'", "\\\\'") + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players_S SET nickname = '" + this.nickname.replaceAll("'", "\\\\'") + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void loadNameInTab()
    {
        player.setPlayerListName(this.displayName);
    }

    public void loadName()
    {
        this.player.setDisplayName(this.player.getName());
        if (this.nickname != "") { this.player.setDisplayName(this.nickname); }

        String localPrefix = "";
        String localSuffix = "";

        if (this.prefix != "") { localPrefix = ((this.prefix.length() == 2 && this.prefix.startsWith("§")) ? this.prefix : (this.prefix + " ")) + "§r"; }
        if (this.suffix != "") { localSuffix =  "§r" + ((this.suffix.length() == 2 && this.prefix.startsWith("§")) ? this.suffix : (" " + this.suffix)); }

        this.displayName = localPrefix + player.getDisplayName() + localSuffix;
    }

    public static User getFromUUID(UUID uuid)
    {
        for (User userInArray : users)
        {
            if (userInArray.getUUID() == uuid) { return userInArray; }
        }

        return null;
    }
}
