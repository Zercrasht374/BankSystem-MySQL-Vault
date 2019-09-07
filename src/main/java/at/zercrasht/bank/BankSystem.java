package at.zercrasht.bank;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BankSystem extends JavaPlugin {

    @Getter
    public Economy economy;
    @Getter
    public String prefix;
    @Getter
    public String noperm;
    @Getter
    public static BankSystem instance;
    @Getter
    public String currency;

    @Override
    public void onEnable(){
        MySQL.setStandardMySQL();
        MySQL.readMySQL();
        MySQL.connect();
        MySQL.createTable();
        init(Bukkit.getPluginManager());
        instance = this;
        initCfg(getConfig());
        if(!(setupEconomy())){
            Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cVault wurde nicht gefunden! Plugin wird entladen!");
            getServer().getPluginManager().disablePlugin(this);
        }
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "§aPlugin Geladen!");

    }
    public void onDisalble(){
        MySQL.close();
    }


    public void initCfg(FileConfiguration fileConfiguration){
        fileConfiguration.options().copyDefaults(true);
        fileConfiguration.addDefault("prefix", "&8[&6Bank&8] ");
        fileConfiguration.addDefault("noperm", "&cAcces denied!");
        fileConfiguration.addDefault("currency", "€");

        prefix = ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("prefix"));
        noperm = ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("noperm"));
        currency = fileConfiguration.getString("currency");
        saveConfig();
    }

    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }

            return (economy != null);
        }else{
            return false;
        }
    }

    public void init(PluginManager pluginManager){
        getCommand("bank").setExecutor(new BankCMD());
    }

}
