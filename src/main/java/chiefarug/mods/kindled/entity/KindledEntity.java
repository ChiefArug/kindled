package chiefarug.mods.kindled.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KindledEntity extends Monster implements Enemy {

	@Nullable
	private DyeColor type; // null when default color

	public static final EntityType<KindledEntity> ENTITY_TYPE = EntityType.Builder.of(KindledEntity::new, MobCategory.MONSTER)
			.canSpawnFarFromPlayer()
			.sized(1.0F, 1.0F)
			.clientTrackingRange(10)
			.build("kindled"); //TODO: check what the param passed to build does. Seems to just be datafixer stuff, but it is @NotNull

	public KindledEntity(EntityType<? extends KindledEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	public static AttributeSupplier createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 16.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.build();
	}

	@Nullable
	private DyeColor getKindledType() {
		return this.type;
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Type", 99)) {
			this.type = DyeColor.byId(tag.getByte("Type"));
		}
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (getKindledType() != null) {
			tag.putByte("Type", (byte) this.type.getId());
		}
	}
}
