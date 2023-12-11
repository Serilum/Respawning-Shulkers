package com.natamus.respawningshulkers.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.respawningshulkers.events.ShulkerEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeShulkerEvent {
	@SubscribeEvent
	public void onWorldTick(LevelTickEvent e) {
		Level level = e.level;
		if (level.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		ShulkerEvent.onWorldTick((ServerLevel)level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onShulkerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		ShulkerEvent.onShulkerDeath(entity.getCommandSenderWorld(), entity, e.getSource());
	}
	
	@SubscribeEvent
	public void onServerShutdown(ServerStoppingEvent e) {
		ShulkerEvent.onServerShutdown(e.getServer());
	}
}
