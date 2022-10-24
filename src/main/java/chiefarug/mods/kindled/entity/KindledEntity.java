package chiefarug.mods.kindled.entity;

import chiefarug.mods.kindled.Kindled;
import chiefarug.mods.kindled.Registry;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.EnumSet;

import static chiefarug.mods.kindled.Kindled.LGGR;

public class KindledEntity extends Monster implements Enemy {

	public static final EntityType<KindledEntity> ENTITY_TYPE = EntityType.Builder.of((EntityType.EntityFactory<KindledEntity>) KindledEntity::new, MobCategory.MONSTER)
			.canSpawnFarFromPlayer()
			.sized(1.0F, 1.0F)
			.clientTrackingRange(10)
			.build("kindled");
	protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(KindledEntity.class, EntityDataSerializers.BYTE);
	// An ugly method to sync the entities current yBodRot, because vanilla doesn't seem to do that.
	protected static final EntityDataAccessor<Float> DATA_ROTATION = SynchedEntityData.defineId(KindledEntity.class, EntityDataSerializers.FLOAT);
	private int poofedness;

	@Nullable
	private DyeColor color;

	public KindledEntity(EntityType<? extends KindledEntity> type, Level level) {
		super(type, level);
	}

	public KindledEntity(Level level, @Nullable DyeColor color, Direction facing) {
		this(ENTITY_TYPE, level);
		this.setColor(color);
		this.turn(facing.toYRot());
	}

	public static AttributeSupplier createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 16.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.build();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new KindledAttackGoal());

		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, this.getClass()).setAlertOthers());
	}

	@Override
	public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (DATA_COLOR_ID.equals(key)) {
			setColor(this.entityData.get(DATA_COLOR_ID));
		}
		if (DATA_ROTATION.equals(key) && level.isClientSide()) {
			this.yBodyRot = this.entityData.get(DATA_ROTATION);
		}
	}

	public int getCurrentPoofedness() {
		return poofedness;
	}

	public void setCurrentPoofedness(int i) {
		poofedness = i;
	}

	public void increaseCurrentPoofedness(int i) {
		setCurrentPoofedness(getCurrentPoofedness() + i);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isOnGround()) {
			increaseCurrentPoofedness(2);
		} else if (getCurrentPoofedness() > 0){
			increaseCurrentPoofedness(-1);
		}
		if (this.getCurrentPoofedness() > 160) {
			poof();
		}
	}

	public void poof() {
		dropPoofLoot();
		if (level instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 50, 0.5D, 0.5D, 0.5D, 0.1D);
		}
		this.playSound(Registry.KINDLED_POOF_SOUND.get());

		this.discard();
	}

	private void dropPoofLoot() {
		if (this.level.getServer() == null) return; // This should never happen but it shuts IntelliJ up
		ResourceLocation resourcelocation = new ResourceLocation(Kindled.MODID, "entity/kindled/poof");
		LootTable loottable = this.level.getServer().getLootTables().get(resourcelocation);
		LootContext.Builder lootContext$builder = this.createLootContext(false, new DamageSource("kindled.poofed"));
		LootContext ctx = lootContext$builder.create(LootContextParamSets.ENTITY);
		loottable.getRandomItems(ctx).forEach(this::spawnAtLocation);
	}

	@Nullable
	public DyeColor getColor() {
		return color;
	}

	private void setColor(@Nullable DyeColor color) {
		setColor((byte) (color == null ? 16 : color.getId()));
	}


	private void setColor(@Range(from = 0, to = 16) byte color) {
		this.entityData.set(DATA_COLOR_ID, color);
		if (color == 16) {
			this.color = null;
		} else {
			this.color = DyeColor.byId(color);
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_COLOR_ID, (byte) 16);
		this.entityData.define(DATA_ROTATION, this.yBodyRot);
	}

	@Override
	public boolean hurt(@NotNull DamageSource source, float pAmount) {
		Entity entity = source.getEntity();
		if (entity instanceof Player) {
			double xDif = this.getX() - entity.getX();
			boolean xPositive = xDif >= 0;
			double zDif = this.getZ() - entity.getZ();
			boolean zPositive = zDif >= 0;
			// check which direction the player is in, and turn towards that
			if (Math.abs(xDif) > Math.abs(zDif)) {
				if (xPositive) {
					turn(90);
				} else {
					turn(270);
				}
			} else {
				if (zPositive) {
					turn(180);
				} else {
					turn(0);
				}
			}
		}

		if (source.isProjectile()) {
			Entity entity1 = source.getDirectEntity();
			if (entity1 != null) {
				// Increase poof timer if they get hit by a floating bullet, and don't hurt if it's their own sort of bullet.
				if (entity1.getType() == EntityType.SHULKER_BULLET) {
					increaseCurrentPoofedness(random.nextInt(10));
				} else if (entity1.getType() == Registry.KINDLED_BULLET_ENTITY.get()) {
					increaseCurrentPoofedness(random.nextInt(5));
					return entity1.getType() == Registry.KINDLED_BULLET_ENTITY.get(); // Otherwise, they just kill themselves
				}
			}
		}
		return super.hurt(source, pAmount);
	}

//	@Override
//	@NotNull //DEBUG
//	protected InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
//		if (this.level.isClientSide()) return InteractionResult.PASS;
//		ItemStack heldItem = player.getItemInHand(hand);
//		Item item = heldItem.getItem();
//		if (Kindled.candles.containsKey(item)) {
//			setColor(Kindled.candles.get(item));
//			//setYBodyRot(yBodyRot + 70);
//			spin();
//			return InteractionResult.CONSUME;
//		}
//		return InteractionResult.PASS;
//	}

	@Override
	public @NotNull ItemStack getItemInHand(@NotNull InteractionHand pHand) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setPos(double x, double y, double z) {
		if (this.isPassenger() || this.fallDistance > 0.0F) {
			super.setPos(x, y, z);
		} else {
			super.setPos(Mth.floor(x) + 0.5D, y, Mth.floor(z) + 0.5D);
		}
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Color")) {
			setColor(tag.getByte("Color"));
		} else {
			setColor(null);
		}
		if (tag.contains("Facing")) {
			turn(tag.getFloat("Facing"));
		}
		if (tag.contains("Poofedness")) {
			setCurrentPoofedness(tag.getInt("Poofedness"));
		}
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		DyeColor color = getColor();
		tag.putByte("Color", color == null ? 16 : (byte) getColor().getId());
		tag.putFloat("Facing", entityData.get(DATA_ROTATION));
		tag.putInt("Poofedness", getCurrentPoofedness());
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
		return Registry.KINDLED_HURT_SOUND.get();
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public void push(@NotNull Entity pEntity) {
	}

	@Override
	protected float getStandingEyeHeight(@NotNull Pose pPose, @NotNull EntityDimensions pDimensions) {
		return 0.6F;
	}

	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	@Override
	protected @NotNull BodyRotationControl createBodyControl() {
		return new KindledBodyControl(this);
	}

	private void turn(float f) {
		float newRot = random.nextBoolean() ? f - 10 : f + 10; // This offset by 10 will get almost immediately corrected by the BodyRotationController, creating a cool bounce back effect
		setYBodyRot(newRot);
		this.entityData.set(DATA_ROTATION, newRot);
	}

	private void spin() {
		float newRot = random.nextBoolean() ? yBodyRot - 90 : yBodyRot + 90;
		turn(newRot);
	}

	class KindledBodyControl extends BodyRotationControl {
		public KindledBodyControl(Mob pMob) {
			super(pMob);
		}

		@Override
		public void clientTick() {
			// Only let it face one of the cardinal directions
			if (KindledEntity.this.yBodyRot % 90 != 0) {
				int newRot = Math.round(KindledEntity.this.yBodyRot / 90) * 90;
				KindledEntity.this.setYBodyRot(newRot);
				setYRot(yBodyRot);
			}
		}
	}

	abstract class BaseKindledGoal extends Goal {
		protected final KindledEntity kindled = KindledEntity.this;
	}

	class KindledAttackGoal extends BaseKindledGoal {
		private int attackTime;

		public KindledAttackGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			LivingEntity target = kindled.getTarget();
			if (target != null) {
				if (!target.isAlive()) {
					kindled.setTarget(null);
				} else {
					return kindled.getLevel().getDifficulty() != Difficulty.PEACEFUL;
				}
			}

			return false;
		}

		@Override
		public void start() {
			attackTime = 20;
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		private boolean wtf(Direction dir) {
			LGGR.error("How on earth did this happen. Got direction " + dir + " from Direction#fromYRot, should only be north, south, east or west. (This is to make the compiler happy, if anyone actually sees this error message, sorry. I suggest your pray to the bit flip gods that this never happens again)");
			return false;
		}

		private boolean isFacingTarget(LivingEntity target) {
			Direction facing = Direction.fromYRot(kindled.yBodyRot);
			return switch (facing) {
				case NORTH -> kindled.getZ() - 1.0D >= target.getZ();
				case SOUTH -> kindled.getZ() + 1.0D <= target.getZ();
				case WEST -> kindled.getX() - 1.0D >= target.getX();
				case EAST -> kindled.getX() + 1.0D <= target.getX();
				default -> wtf(facing);
			};
		}

		@Override
		public void tick() {
			if (kindled.level.getDifficulty() == Difficulty.PEACEFUL || attackTime-- >= 0) return;

			LivingEntity target = kindled.getTarget();
			if (target == null) return;
			if (kindled.distanceTo(target) <= 400D) {
				this.attackTime = 60 + random.nextInt(40);
				if (isFacingTarget(target)) {
					kindled.level.addFreshEntity(new KindledBulletEntity(kindled.level, kindled, target, Direction.Axis.X));
					kindled.playSound(Registry.KINDLED_SHOOT_SOUND.get(), 2.0F, (kindled.random.nextFloat() - kindled.random.nextFloat()) * 0.2F + 1.0F);
				} else {
					kindled.spin();
				}
			} else {
				kindled.setTarget(null);
			}
		}
	}
}
