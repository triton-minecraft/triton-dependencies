package dev.kyriji.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.logging.Logger;

@Plugin(
	id = "triton-dependencies",
	name = "TritonDependencies",
	version = "1.0",
    url = "https://github.com/triton-minecraft/triton-dependencies",
	description = "A Dependency Loader for the TritonMC Network",
	authors = {"wiji, Kyro"}
)
public class DependencyLoader {

	public static ProxyServer INSTANCE;
	public final Logger logger;

	@Inject
	public DependencyLoader(ProxyServer server, Logger logger) {
		INSTANCE = server;
		this.logger = logger;
	}

	@Subscribe
	public void onProxyInitialize(ProxyInitializeEvent event) {
		logger.info("Loading dependencies...");
	}
}