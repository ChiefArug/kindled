package chiefarug.mods.kindled.client;

import chiefarug.mods.kindled.KindledRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

	@SubscribeEvent
	public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(KindledRegistry.KINDLED_ENTITY.get(), KindledRenderer::new);
		event.registerEntityRenderer(KindledRegistry.KINDLED_BULLET_ENTITY.get(), KindledBulletRenderer::new);
	}

	@SubscribeEvent
	public static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(KindledModel.LAYER_LOCATION, KindledModel::createBodyLayer);
	}
}
