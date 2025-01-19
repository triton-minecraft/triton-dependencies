package dev.kyriji.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class DependencyLoader extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Loading dependencies...");
	}
}