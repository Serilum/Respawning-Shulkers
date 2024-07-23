package com.natamus.respawningshulkers.events;

import com.natamus.collective.functions.HashMapFunctions;
import com.natamus.collective.util.CollectiveReference;
import com.natamus.respawningshulkers.config.ConfigHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ShulkerEvent {
	private static final HashMap<Entity, Integer> shulkersTicksLeft = new HashMap<Entity, Integer>();
	private static final HashMap<Level, CopyOnWriteArrayList<Entity>> respawnShulkers = new HashMap<Level, CopyOnWriteArrayList<Entity>>();

	public static void onWorldTick(ServerLevel serverLevel) {
		if (HashMapFunctions.computeIfAbsent(respawnShulkers, serverLevel, k -> new CopyOnWriteArrayList<Entity>()).size() > 0) {
			for (Entity shulker : respawnShulkers.get(serverLevel)) {
				int ticksleft = shulkersTicksLeft.get(shulker) - 1;
				if (ticksleft == 0) {
					respawnShulkers.get(serverLevel).remove(shulker);
					shulkersTicksLeft.remove(shulker);
					
					serverLevel.addFreshEntity(shulker);
					continue;
				}
				
				shulkersTicksLeft.put(shulker, ticksleft);
			}
		}
	}
	
	public static void onShulkerDeath(Level level, Entity entity, DamageSource source) {
		if (level.isClientSide) {
			return;
		}

		if (!(entity instanceof Shulker)) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(CollectiveReference.MOD_ID + ".fromspawner")) {
			if (ConfigHandler.shulkersFromSpawnersDoNotRespawn) {
				return;
			}
		}

		if (tags.contains("flowermimics.mimic")) {
			return;
		}
		
		Shulker newshulker = EntityType.SHULKER.create(level);
		newshulker.restoreFrom(entity);
		newshulker.setHealth(30F);
		
		shulkersTicksLeft.put(newshulker, ConfigHandler.timeInTicksToRespawn);
		HashMapFunctions.computeIfAbsent(respawnShulkers, level, k -> new CopyOnWriteArrayList<Entity>()).add(newshulker);
	}
	
	public static void onServerShutdown(MinecraftServer server) {
		Set<Level> levels = respawnShulkers.keySet();
		for (Level level : levels) {
			for (Entity shulker : respawnShulkers.get(level)) {
				level.addFreshEntity(shulker);
			}
		}
	}
}
