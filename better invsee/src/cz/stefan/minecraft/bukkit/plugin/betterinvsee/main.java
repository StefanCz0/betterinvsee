package cz.stefan.minecraft.bukkit.plugin.betterinvsee;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener, CommandExecutor{
	public LinkedList<Inventory> openedBPI = new LinkedList<Inventory>();
	public LinkedList<Player> playerBPI = new LinkedList<Player>();
	public LinkedList<Player> BPIviewers = new LinkedList<Player>();
	@Override
	public void onEnable() {
		getLogger().warning("\n**************************************\n      Enabling Better invsee 1.0\n      Author: pavelstef\n      github: github.com/StefanCz0\n**************************************");
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(label.equalsIgnoreCase("openinv")||label.equalsIgnoreCase("openinventory")||label.equalsIgnoreCase("betterinvsee")||label.equalsIgnoreCase("invsee")) {
				if(args.length>0) {
					String names="";
					for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
						names=pl.getName();
						if(args[0].equals(names)) {
							Player open = getServer().getPlayerExact(args[0]);
							Inventory invopen=getBPI(open);
							openedBPI.add(invopen);
							playerBPI.add(open);
							BPIviewers.add(p);
							p.openInventory(invopen);
						}
					}
				}
			}
		}
		return true;
	}

	@EventHandler
	public void onBPIclick(InventoryClickEvent e) {
		Player view = (Player) e.getWhoClicked();
		ItemStack current =e.getCurrentItem();
		Inventory inv = e.getInventory();
		ItemStack cursor = e.getCursor();
		for(int in = 0; in<openedBPI.size(); in++) {

			Inventory ifit = openedBPI.get(in);

			Player owner = playerBPI.get(in);



			if(inv.equals(ifit)) {
				if(current!=null) {
					if(current.getItemMeta().getDisplayName().equals(ChatColor.AQUA+"open players inventory")) {
						view.closeInventory();
						view.openInventory(owner.getInventory());
						e.setCancelled(true);

					}
					if(current.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE+"open players ender chest")) {
						view.closeInventory();
						view.openInventory(owner.getEnderChest());
						e.setCancelled(true);
					}
					if(current.getItemMeta().getDisplayName().equals(" ")) {
						e.setCancelled(true);
					}
					if(current.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"SAVE")) {
						BPImanager(ifit, owner);
						owner.updateInventory();
						e.setCancelled(true);
					}
					if(current.getItemMeta().getDisplayName().equals(ChatColor.RED+"RELOAD")) {
						view.closeInventory();
						Inventory invopen=getBPI(owner);
						playerBPI.remove(owner);
						openedBPI.remove(ifit);
						BPIviewers.remove(view);
						BPIviewers.add(view);
						openedBPI.add(invopen);
						playerBPI.add(owner);
						view.openInventory(invopen);	
					}

				}


			}
		}

	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		for(int i = 0; i<openedBPI.size(); i++) {
			if(openedBPI.get(i)!=null) {
				Inventory ifit = openedBPI.get(i);
				if(playerBPI.get(i)!=null) {
					Player owner = playerBPI.get(i);
					if(BPIviewers.get(i).equals(p)) {

						if(inv.equals(ifit)) {

							openedBPI.remove(inv);
							playerBPI.remove(owner);

							

						}
					}
				}
			}
		}

	}
	public Inventory getBPI(Player p) {
		ItemStack air = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta m = air.getItemMeta();
		m.setDisplayName(" ");
		air.setItemMeta(m);
		Inventory inv = getServer().createInventory(null, 54, "BPI: "+ChatColor.YELLOW+p.getName()+ChatColor.RESET+" -overview");
		for(int i = 0; i<inv.getSize();i++) {
			inv.setItem(i, air);
		}
		if(p.getInventory().getHelmet()!=null) {
			inv.setItem(53, p.getInventory().getHelmet());
		}else {
			inv.setItem(53,  new ItemStack(Material.AIR));
		}
		if(p.getInventory().getChestplate()!=null) {
			inv.setItem(52, p.getInventory().getChestplate());
		} else {
			inv.setItem(52,  new ItemStack(Material.AIR));
		}
		if(p.getInventory().getLeggings()!=null) {
			inv.setItem(51, p.getInventory().getLeggings());
		} else {
			inv.setItem(51, new ItemStack(Material.AIR));
		}
		if(p.getInventory().getBoots()!=null) {
			inv.setItem(50, p.getInventory().getBoots());
		} else {
			inv.setItem(50,  new ItemStack(Material.AIR));
		}
		for(int i = 0;i<36;i++) {
			if(p.getInventory().getItem(i)!=null) {
				inv.setItem(i, p.getInventory().getItem(i));
			} else {
				inv.setItem(i, new ItemStack(Material.AIR));
			}
		}
		if(p.getInventory().getItemInOffHand()!=null) {
			inv.setItem(45, p.getInventory().getItemInOffHand());
		} else {
			inv.setItem(45,new ItemStack(Material.AIR));
		}
		ItemStack openpinv = new ItemStack(Material.CHEST);

		ItemMeta meta = openpinv.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA+"open players inventory");
		ItemStack enderchest = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta1=enderchest.getItemMeta();
		meta1.setDisplayName(ChatColor.DARK_PURPLE+"open players ender chest");
		enderchest.setItemMeta(meta1);
		openpinv.setItemMeta(meta);
		inv.setItem(47, openpinv);
		inv.setItem(48, enderchest);
		ItemStack save = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta meta3 = save.getItemMeta();
		meta3.setDisplayName(ChatColor.GREEN+"SAVE");
		ItemStack discard = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta4 = discard.getItemMeta();
		meta4.setDisplayName(ChatColor.RED+"RELOAD");
		save.setItemMeta(meta3);
		discard.setItemMeta(meta4);
		inv.setItem(44, save);
		inv.setItem(43, discard);
		return inv;

	}



	public void BPImanager(Inventory BPI, Player owner) {

		Inventory inv = owner.getInventory();
		for(int i =0;i<35;i++) {
			if(BPI.getItem(i)!=null) {
				inv.setItem(i,BPI.getItem(i) );
			} else {
				inv.setItem(i, new ItemStack(Material.AIR));
			}
		}
		inv.setItem(inv.getSize()-1,BPI.getItem(45));


		inv.setItem(inv.getSize()-5,BPI.getItem(50));


		inv.setItem(inv.getSize()-4,BPI.getItem(51));


		inv.setItem(inv.getSize()-3,BPI.getItem(52));

		inv.setItem(inv.getSize()-2, BPI.getItem(53));

	}
}
