package com.Moshu.SimpleAdvertising;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabComplete implements TabCompleter
{

	public ArrayList<String> empty = new ArrayList<>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("ad"))
		{
		
		if(!(sender instanceof Player)) {

            return empty;
        }
		
		if(args.length == 1)
		{
			

			Player p = (Player) sender;
			
                ArrayList<String> c = new ArrayList<>();

                c.add("(Text)");
                

                if(p.hasPermission("simplead.reload") || p.hasPermission("simplead.admin") )
                {
                	c.add("reload");
                }
                
                if(p.hasPermission("simplead.help") || p.hasPermission("simplead.admin"))
                {
                	c.add("help");
                }
                
                if(p.hasPermission("simplead.debug") || p.hasPermission("simplead.admin"))
                {
                	c.add("debug");
                }

                return c;
            
			
		}
		
		return empty;
		
		}
		
		if(cmd.getName().equalsIgnoreCase("points"))
		{
		
			
		if(!(sender instanceof Player)) {

            return empty;
        }
		
		
		if(args.length == 1)
		{
			

			Player p = (Player) sender;
			
                ArrayList<String> c = new ArrayList<>();


                if(p.hasPermission("simplead.points") || p.hasPermission("simplead.admin"))
                {
                	c.add("give");
                	c.add("take");
                	c.add("set");
                }


                return c;
            
			
		}
		
		
		return empty;
		
		}
		
		return null;
	}

}

