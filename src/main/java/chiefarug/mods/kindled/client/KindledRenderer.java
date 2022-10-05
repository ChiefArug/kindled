package chiefarug.mods.kindled.client;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class KindledRenderer extends MobRenderer<KindledEntity, KindledModel> {

	static final ResourceLocation EYES_TEXTURE = new ResourceLocation("kindled:textures/entity/kindled/eyes.png");
	static final ResourceLocation SHELL_TEXTURE = new ResourceLocation("kindled:textures/entity/kindled/shell.png");
	static final ResourceLocation MODEL_LOCATION = new ResourceLocation("kindled:kindled");
	private static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(MODEL_LOCATION, "main");

	public KindledRenderer(EntityRendererProvider.Context context) {
		super(context, new KindledModel(context.bakeLayer(MODEL_LAYER_LOCATION)), 0F);
		this.addLayer(new KindledEyesLayer(this));
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
	}

	@Override
	public void render(@NotNull KindledEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}

//	private boolean isSpooky() {
//		return true;
//	}


	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull KindledEntity p_114482_) {
		return SHELL_TEXTURE;
	}
}
