package chiefarug.mods.kindled.client;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class KindledEyesLayer extends EyesLayer<KindledEntity, KindledModel> {

	private static final Map<DyeColor, RenderType> KINDLED_EYES = new HashMap<>();
	static {
		KINDLED_EYES.put(null, renderTypeForColor(null));
		for (DyeColor color : DyeColor.values()) {
			KINDLED_EYES.put(color, renderTypeForColor(color));
		}
	}

	private static RenderType renderTypeForColor(@Nullable DyeColor color) {
		ResourceLocation rl = new ResourceLocation("kindled:textures/entity/kindled/eyes.png");
		if (color != null) {
			rl = new ResourceLocation("kindled:textures/entity/kindled/eyes_" + color.getSerializedName() + ".png");
		}
		return RenderType.eyes(rl);

	}

	@Override
	public void render(@NotNull PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, @NotNull KindledEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		VertexConsumer vertexconsumer = pBuffer.getBuffer(renderType(pLivingEntity.getColor()));
		this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	public KindledEyesLayer(RenderLayerParent<KindledEntity, KindledModel> p_116981_) {
		super(p_116981_);
	}

	@Override
	public @NotNull RenderType renderType() {
		return KINDLED_EYES.get(null);
	}

	@NotNull
	public RenderType renderType(@Nullable DyeColor color) {
		return KINDLED_EYES.get(color);
	}
}
