package chiefarug.mods.kindled.client;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KindledRenderer extends MobRenderer<KindledEntity, KindledModel> {

	static final Map<DyeColor, ResourceLocation> SHELL_TEXTURES = new HashMap<>();
	static {
		SHELL_TEXTURES.put(null,  new ResourceLocation("kindled:textures/entity/kindled/shell.png"));
		for (DyeColor color : DyeColor.values()) {
			SHELL_TEXTURES.put(color, new ResourceLocation("kindled:textures/entity/kindled/shell_" + color.getSerializedName() + ".png"));
		}
	}
	static final ResourceLocation MODEL_LOCATION = new ResourceLocation("kindled:kindled");
	private static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(MODEL_LOCATION, "main");

	public KindledRenderer(EntityRendererProvider.Context context) {
		super(context, new KindledModel(context.bakeLayer(MODEL_LAYER_LOCATION)), 0.5F);
		this.addLayer(new KindledEyesLayer(this));
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
	}

	@Override
	public void render(@NotNull KindledEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}


	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull KindledEntity entity) {
		return SHELL_TEXTURES.get(entity.getColor());
	}
}
