package com.natamus.respawningshulkers.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.respawningshulkers.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry(min = 1, max = 72000) public static int timeInTicksToRespawn = 1200;
	@Entry public static boolean shulkersFromSpawnersDoNotRespawn = true;

	public static void initConfig() {
		configMetaData.put("timeInTicksToRespawn", Arrays.asList(
			"The amount of time in ticks it takes for a shulker to respawn after it died. 20 ticks = 1 second. By default 60 seconds."
		));
		configMetaData.put("shulkersFromSpawnersDoNotRespawn", Arrays.asList(
			"If enabled, shulkers which spawn from (modded) spawners will not be respawned after a death."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}