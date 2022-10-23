package chiefarug.mods.kindled.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ShulkerBulletRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.jetbrains.annotations.NotNull;

public class KindledBulletRenderer extends ShulkerBulletRenderer {

	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("kindled:textures/entity/kindled/bullet.png");
	private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);

	public KindledBulletRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull ShulkerBullet pEntity) {
		return TEXTURE_LOCATION;
	}

	// Unfortunately I have to do this because mojank hardcode the texture location instead of using the get method for it.
	@Override
	public void render(ShulkerBullet pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		pMatrixStack.pushPose();
		float f = Mth.rotlerp(pEntity.yRotO, pEntity.getYRot(), pPartialTicks);
		float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
		float f2 = (float) pEntity.tickCount + pPartialTicks;
		pMatrixStack.translate(0.0D, 0.15F, 0.0D);
		pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
		pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
		pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
		pMatrixStack.scale(-0.5F, -0.5F, 0.5F);
		this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
		VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(getTextureLocation(pEntity)));
		this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pMatrixStack.scale(1.5F, 1.5F, 1.5F);
		VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RENDER_TYPE);
		this.model.renderToBuffer(pMatrixStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
		pMatrixStack.popPose();
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}
}
