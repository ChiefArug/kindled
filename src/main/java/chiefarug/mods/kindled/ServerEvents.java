package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {

	// Add an extra goal to Shulkers, so they attack Kindled by default
	@SubscribeEvent
	public static void addAdditionalGoals(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Shulker shulker) {
			shulker.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(shulker, KindledEntity.class, true, false));
		}
	}
}
