package com.natamus.respawningshulkers.neoforge.events;

import com.natamus.respawningshulkers.events.ShulkerEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeShulkerEvent {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Pre e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}
		
		ShulkerEvent.onWorldTick((ServerLevel)level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onShulkerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		ShulkerEvent.onShulkerDeath(entity.level(), entity, e.getSource());
	}
	
	@SubscribeEvent
	public static void onServerShutdown(ServerStoppingEvent e) {
		ShulkerEvent.onServerShutdown(e.getServer());
	}
}
