package fr.fingarde.atharion.survie;

import com.zaxxer.hikari.HikariDataSource;
import fr.fingarde.atharion.survie.commands.*;
import fr.fingarde.atharion.survie.listeners.*;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main extends JavaPlugin {

    private int TIMEMULTIPLIER = 3;

    private static Main instance;
    private static HikariDataSource hikari;

    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    private double TPS;
    public static String clock;
    private long passedTime;

    public void onEnable() {
        instance = this;

        connectDatabase();
        createTables();

        registerEvents();
        registerCommands();

        addCrafts();

        updateGameTime();
        refreshTPS();

        sheduleTablist();
    }

    private void registerCommands() {
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("nick").setExecutor(new NickCommand());
        getCommand("prefix").setExecutor(new PrefixCommand());
        getCommand("suffix").setExecutor(new SuffixCommand());
        getCommand("speed").setExecutor(new SpeedCommand());

        getCommand("nick").setTabCompleter(new NickCommand());
        getCommand("prefix").setTabCompleter(new PrefixCommand());
        getCommand("suffix").setTabCompleter(new SuffixCommand());

    }

    private  void registerEvents() {
        getServer().getPluginManager().registerEvents(new CropListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new RideListener(), this);
        getServer().getPluginManager().registerEvents(new SleepListener(), this);
        getServer().getPluginManager().registerEvents(new TimberListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectionListerner(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new BucketListener(), this);
        getServer().getPluginManager().registerEvents(new HammerListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerFeedListener(), this);
        getServer().getPluginManager().registerEvents(new Explosionlistener(), this);
    }

    private void connectDatabase()
    {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", getConfig().getString("host"));
        hikari.addDataSourceProperty("port", getConfig().getInt("port"));
        hikari.addDataSourceProperty("databaseName", getConfig().getString("database"));
        hikari.addDataSourceProperty("user", getConfig().getString("user"));
        hikari.addDataSourceProperty("password", getConfig().getString("password"));

        hikari.addDataSourceProperty("allowPublicKeyRetrieval",true);
        hikari.addDataSourceProperty("verifyServerCertificate", false);
        hikari.addDataSourceProperty("useSSL", false);

        hikari.addDataSourceProperty("tcpKeepAlive", true);
        hikari.addDataSourceProperty("autoReconnect", true);
        hikari.addDataSourceProperty("connectTimeout", 300);

        hikari.addDataSourceProperty("characterEncoding","utf8");
        hikari.addDataSourceProperty("useUnicode","true");

        hikari.setMaximumPoolSize(2147483647);
        hikari.setMinimumIdle(0);
        hikari.setIdleTimeout(300);
    }

    private void addCrafts() {
        ItemStack diamondHammer = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta diamondHammerMeta = diamondHammer.getItemMeta();
        diamondHammerMeta.setDisplayName("§rDiamond Hammer");
        diamondHammerMeta.setLocalizedName("diamond_hammer");
        ArrayList<String> diamondHammerLore = new ArrayList<>();
        diamondHammerLore.add("");
        diamondHammerLore.add("§rDurability: §e" + "1651" + "§r/§e" + "1651");
        diamondHammerMeta.setCustomModelData(10001001);
        diamondHammerMeta.setLore(diamondHammerLore);
        diamondHammer.setItemMeta(diamondHammerMeta);
        ShapedRecipe diamondHammerRecipe = new ShapedRecipe( new NamespacedKey(this, "diamond_hammer"), diamondHammer);
        diamondHammerRecipe.shape("***","?#?"," % ");
        diamondHammerRecipe.setIngredient('%', Material.BLAZE_ROD);
        diamondHammerRecipe.setIngredient('?', Material.DIAMOND);
        diamondHammerRecipe.setIngredient('*', Material.DIAMOND_PICKAXE);
        diamondHammerRecipe.setIngredient('#', Material.DIAMOND_BLOCK);
        getServer().addRecipe(diamondHammerRecipe);

        ItemStack diamondExcavator = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta diamondExcavatorMeta = diamondExcavator.getItemMeta();
        diamondExcavatorMeta.setDisplayName("§rDiamond Excavator");
        diamondExcavatorMeta.setLocalizedName("diamond_excavator");
        ArrayList<String> diamondExcavatorLore = new ArrayList<>();
        diamondExcavatorLore.add("");
        diamondExcavatorLore.add("§rDurability: §e" + "1651" + "§r/§e" + "1651");
        diamondExcavatorMeta.setCustomModelData(10001011);
        diamondExcavatorMeta.setLore(diamondExcavatorLore);
        diamondExcavator.setItemMeta(diamondExcavatorMeta);
        ShapedRecipe diamondExcavatorRecipe = new ShapedRecipe( new NamespacedKey(this, "diamond_excavator"), diamondExcavator);
        diamondExcavatorRecipe.shape("***","?#?"," % ");
        diamondExcavatorRecipe.setIngredient('%', Material.BLAZE_ROD);
        diamondExcavatorRecipe.setIngredient('?', Material.DIAMOND);
        diamondExcavatorRecipe.setIngredient('*', Material.DIAMOND_SHOVEL);
        diamondExcavatorRecipe.setIngredient('#', Material.DIAMOND_BLOCK);
        getServer().addRecipe(diamondExcavatorRecipe);

        ItemStack extraBucked = new ItemStack(Material.BUCKET);
        ItemMeta extraBuckedMeta = extraBucked.getItemMeta();
        extraBuckedMeta.setDisplayName("§rExtra Bucked");
        extraBuckedMeta.setLocalizedName("extra_bucket");
        ArrayList<String> extraBuckedLore = new ArrayList<>();
        extraBuckedLore.add("");
        extraBuckedLore.add("§rMode: §e" + "3x3x3");
        extraBuckedMeta.setLore(extraBuckedLore);
        extraBuckedMeta.setCustomModelData(10030001);

        extraBucked.setItemMeta(extraBuckedMeta);

        ShapedRecipe extraBuckedRecipe = new ShapedRecipe( new NamespacedKey(this, "extra_bukket"), extraBucked);
        extraBuckedRecipe.shape("* *", " * ");
        extraBuckedRecipe.setIngredient('*', Material.DIAMOND_BLOCK);

        getServer().addRecipe(extraBuckedRecipe);
    }
    private void createTables()
    {
        try
        {
            Connection connection = hikari.getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Players_S(\n" +
                            "  id MEDIUMINT NOT NULL AUTO_INCREMENT,\n" +
                            "  uuid TEXT NOT NULL,\n" +
                            "  nickname TEXT NOT NULL,\n" +
                            "  prefix TEXT NOT NULL,\n" +
                            "  suffix TEXT NOT NULL,\n" +
                            "  primary KEY (id)\n" +
                            ")");
            statement.close();
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            console.sendMessage(ChatColor.RED + "[Atharion] ERROR: Impossible d'atteindre la base de données !");
            console.sendMessage(ChatColor.GOLD + "[Atharion] INFO: Le plugin se désactive automatiquement.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


    private void sheduleTablist()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    player.setPlayerListHeader(
                            "§r \n" +
                                    "§r§eAtharion\n" +
                                    "§r      §m                   §r      \n");

                    player.setPlayerListFooter(
                            "§r      §m                   §r      \n" +
                                    "§rOnline: §b" + Bukkit.getOnlinePlayers().size() + "§r/§b" + Bukkit.getMaxPlayers() + "\n" +
                                    "§rTPS: §a" + TPS + "\n" +
                                    "§r      §m                   §r      \n" +
                                    "§r§e" + clock + "\n" +
                                    "§r ");
                }
            }
        }.runTaskTimer(this,  0, 10);
    }

    private void refreshTPS()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Long millis = (System.currentTimeMillis() - passedTime);
                double seconds = millis.floatValue() / 1000.00;

                TPS = 100 / seconds;

                BigDecimal bd = new BigDecimal(TPS);
                bd= bd.setScale(2,BigDecimal.ROUND_DOWN);
                TPS = bd.doubleValue();

                passedTime = System.currentTimeMillis();

            }
        }.runTaskTimer(this,  0L, 100L);
    }

    private void updateGameTime()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (World world : Bukkit.getWorlds())
                {
                    world.setFullTime(world.getFullTime() + 1);
                }

                long gameTime = Bukkit.getWorld("world").getTime() + 6000;
                int hours = (int) Math.floor(gameTime / 1000);
                int minutes = (int) ((gameTime % 1000) / 1000.0 * 60);
                if (hours >= 24) hours -= 24;

                clock = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
            }
        }.runTaskTimer(this, 0 , TIMEMULTIPLIER);
    }

    public static Main getInstance() {
        return instance;
    }

    public static HikariDataSource getHikari() {
        return hikari;
    }

    public static ConsoleCommandSender getConsole() {
        return console;
    }
}
