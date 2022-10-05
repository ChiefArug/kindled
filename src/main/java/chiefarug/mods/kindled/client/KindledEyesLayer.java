package chiefarug.mods.kindled.client;

import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import org.jetbrains.annotations.NotNull;

public class KindledEyesLayer extends EyesLayer<KindledEntity, KindledModel> {

	private static final RenderType KINDLED_EYES = RenderType.eyes(KindledRenderer.EYES_TEXTURE);

	public KindledEyesLayer(RenderLayerParent<KindledEntity, KindledModel> p_116981_) {
		super(p_116981_);
	}

	@Override
	public @NotNull RenderType renderType() {
		return KINDLED_EYES;
	}
}
