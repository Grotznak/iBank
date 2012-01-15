package com.iBank.Commands;

import java.math.BigInteger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iBank.iBank;
import com.iBank.system.Bank;
import com.iBank.system.BankAccount;
import com.iBank.system.Configuration;
import com.iBank.system.Handler;
import com.iBank.system.MessageManager;

/**
 *  /bank deposit <NAME> <AMOUNT>
 * @author steffengy
 *
 */
public class CommandDeposit extends Handler {
	public void handle(CommandSender sender, String[] arguments) { 	
		if(arguments.length == 2) {
			if(!(sender instanceof Player)) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNoPlayer.toString());
				return;
			}
			if(!iBank.canExecuteCommand(((Player)sender).getLocation())) {
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotRegion.toString());
				return;
			}
			if(Bank.hasAccount(arguments[0])) {
				BigInteger todp = null;
				BankAccount account = Bank.getAccount(arguments[0]);
				if(account.isOwner(((Player)sender).getName()) || account.isUser(((Player)sender).getName()))
				try{
				todp = new BigInteger(arguments[1]);
				}catch(Exception e) {
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString()+" [AMOUNT]");
					return;
				}
				// check if current player has that amount
				if(iBank.economy.has(((Player)sender).getName(), todp.doubleValue())) {
					iBank.economy.withdrawPlayer(((Player)sender).getName(), todp.doubleValue());
					account.addBalance(todp);
					MessageManager.send(sender, "&g&"+Configuration.StringEntry.SuccessDeposit.toString().replace("$name$", arguments[0]).replace("$amount$", iBank.format(todp)));
				}else{
					MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotEnough.toString());
					return;
				}
			}else{
				MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorNotExist.toString().replace("$name$", arguments[0]));
			}
			
		}else{
			MessageManager.send(sender, "&r&"+Configuration.StringEntry.ErrorWrongArguments.toString());
		}
	}
}
