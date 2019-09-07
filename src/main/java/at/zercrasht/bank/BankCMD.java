package at.zercrasht.bank;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(strings.length == 1){
                if(strings[0].equalsIgnoreCase("guthaben")){
                    int coins = BankAPI.getCoins(player.getUniqueId().toString());
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§aDu hast gerade §e" + coins + BankSystem.getInstance().getCurrency() + "§a auf der Bank!");
                }else{
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§cMögliche Commands:");
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank guthaben §7- §eZeigt dein Bank-Guthaben an!");
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank einzahlen <Betrag> §7- §eZahle Guthaben auf dein Konto ein!");
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank abheben <Betrag> §7- §eHebe Guthaben von deinem Konto ab!");
                }
            }else if(strings.length == 2){
                if(strings[0].equalsIgnoreCase("abheben")){
                    int amount;
                    try {
                        amount = Integer.parseInt(strings[1]);
                        int coins = BankAPI.getCoins(player.getUniqueId().toString());
                        if(amount <= coins){
                            EconomyResponse economyResponse = BankSystem.getInstance().getEconomy().depositPlayer(player, amount);
                            if(economyResponse.transactionSuccess()) {
                                BankAPI.removeCoins(player.getUniqueId().toString(), amount);
                                player.sendMessage(BankSystem.getInstance().getPrefix() + "§aDu hast §e" + amount + BankSystem.getInstance().getCurrency() + "§a von deinem Bank-Konto abgehoben!");
                            }else{
                                player.sendMessage(BankSystem.getInstance().getPrefix() + "§cEs gab einen Fehler bei der Transaktion! Melde dich bei einem Administrator!");
                            }
                        }else{
                            player.sendMessage(BankSystem.getInstance().getPrefix() + "§cDu hast zu wenig Guthaben auf der Bank!");
                        }
                    }catch (NumberFormatException e){
                        player.sendMessage(BankSystem.getInstance().getPrefix() + "§cDu must eine Gültige Zahl angeben!");
                    }
                }else if(strings[0].equalsIgnoreCase("einzahlen")){
                    int amount;
                    try {
                        amount = Integer.parseInt(strings[1]);
                        int coins = (int) BankSystem.getInstance().getEconomy().getBalance(player);

                        if(coins >= amount){
                            EconomyResponse economyResponse = BankSystem.getInstance().getEconomy().withdrawPlayer(player, amount);
                            if(economyResponse.transactionSuccess()){
                                BankAPI.addCoins(player.getUniqueId().toString(), amount);
                                player.sendMessage(BankSystem.getInstance().getPrefix() + "§aDu hast §e" + amount + BankSystem.getInstance().getCurrency() + "§a auf dein Bank-Konto eingezahlt!");
                            }else{
                                player.sendMessage(BankSystem.getInstance().getPrefix() + "§cEs gab einen Fehler bei der Transaktion! Melde dich bei einem Administrator!");
                            }
                        }else{
                            player.sendMessage(BankSystem.getInstance().getPrefix() + "§cDu hast zu wenig Guthaben auf deinem Konto!");
                        }
                    }catch (NumberFormatException e){
                        player.sendMessage(BankSystem.getInstance().getPrefix() + "§cDu must eine Gültige Zahl angeben!");
                    }
                }else{
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§cMögliche Commands:");
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank guthaben §7- §eZeigt dein Bank-Guthaben an!");
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank einzahlen <Betrag> §7- §eZahle Guthaben auf dein Konto ein!");
                    player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank abheben <Betrag> §7- §eHebe Guthaben von deinem Konto ab!");
                }
            }else{
                player.sendMessage(BankSystem.getInstance().getPrefix() + "§cMögliche Commands:");
                player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank guthaben §7- §eZeigt dein Bank-Guthaben an!");
                player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank einzahlen <Betrag> §7- §eZahle Guthaben auf dein Konto ein!");
                player.sendMessage(BankSystem.getInstance().getPrefix() + "§e/bank abheben <Betrag> §7- §eHebe Guthaben von deinem Konto ab!");
            }
        }else{
            commandSender.sendMessage(BankSystem.getInstance().getPrefix() + "§cDu musst ein Spieler sein!");
        }

        return false;
    }
}
