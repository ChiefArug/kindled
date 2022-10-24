package chiefarug.mods.kindled.entity;

import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class KindledBulletEntity extends ShulkerBullet {

	public static final EntityType<KindledBulletEntity> ENTITY_TYPE = EntityType.Builder.<KindledBulletEntity>of(KindledBulletEntity::new, MobCategory.MISC)
			.sized(0.4F, 0.4F)
			.clientTrackingRange(8)
			.build("kindled_bullet");

	public KindledBulletEntity(Level pLevel, LivingEntity pShooter, Entity pFinalTarget, Direction.Axis pAxis) {
		this(ENTITY_TYPE, pLevel);
		this.setOwner(pShooter);
		BlockPos blockpos = pShooter.blockPosition();
		double d0 = (double) blockpos.getX() + 0.5D;
		double d1 = (double) blockpos.getY() + 0.5D;
		double d2 = (double) blockpos.getZ() + 0.5D;
		this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
		this.finalTarget = pFinalTarget;
		this.currentMoveDirection = Direction.UP;
		this.selectNextMoveDirection(pAxis);
	}

	public KindledBulletEntity(EntityType<KindledBulletEntity> entityEntityType, Level level) {
		super(entityEntityType, level);
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult pResult) {
		Entity entity = pResult.getEntity();
		Entity entity1 = this.getOwner();
		LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;
		boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 2.5F);
		if (flag && livingentity != null) {
			this.doEnchantDamageEffects(livingentity, entity);
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200), MoreObjects.firstNonNull(entity1, this));
			}
		}
	}
}
