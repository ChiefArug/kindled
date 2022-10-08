package chiefarug.mods.kindled.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static chiefarug.mods.kindled.Kindled.LGGR;

public class KindledEntity extends Monster implements Enemy {

	private static final Map<Item, DyeColor> candles = new HashMap<>();

	static {
		candles.put(Items.CANDLE, null);
		candles.put(Items.WHITE_CANDLE, DyeColor.WHITE);
		candles.put(Items.ORANGE_CANDLE, DyeColor.ORANGE);
		candles.put(Items.MAGENTA_CANDLE, DyeColor.MAGENTA);
		candles.put(Items.LIGHT_BLUE_CANDLE, DyeColor.LIGHT_BLUE);
		candles.put(Items.YELLOW_CANDLE, DyeColor.YELLOW);
		candles.put(Items.LIME_CANDLE, DyeColor.LIME);
		candles.put(Items.PINK_CANDLE, DyeColor.PINK);
		candles.put(Items.GRAY_CANDLE, DyeColor.GRAY);
		candles.put(Items.LIGHT_GRAY_CANDLE, DyeColor.LIGHT_GRAY);
		candles.put(Items.CYAN_CANDLE, DyeColor.CYAN);
		candles.put(Items.PURPLE_CANDLE, DyeColor.PURPLE);
		candles.put(Items.BLUE_CANDLE, DyeColor.BLUE);
		candles.put(Items.BROWN_CANDLE, DyeColor.BROWN);
		candles.put(Items.GREEN_CANDLE, DyeColor.GREEN);
		candles.put(Items.RED_CANDLE, DyeColor.RED);
		candles.put(Items.BLACK_CANDLE, DyeColor.BLACK);
	}

	protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(KindledEntity.class, EntityDataSerializers.BYTE);
	// An ugly method to sync the entities current yBodRot, because vanilla doesn't seem to do that.
	protected static final EntityDataAccessor<Float> DATA_ROTATION = SynchedEntityData.defineId(KindledEntity.class, EntityDataSerializers.FLOAT);
	@Nullable
	private DyeColor color;
	private int poofTimer;

	public static final EntityType<KindledEntity> ENTITY_TYPE = EntityType.Builder.of(KindledEntity::new, MobCategory.MONSTER)
			.canSpawnFarFromPlayer()
			.sized(1.0F, 1.0F)
			.clientTrackingRange(10)
			.build("kindled"); //TODO: check what the param passed to build does. Seems to just be datafixer stuff, but it is @NotNull

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new KindledAttackGoal());

		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public KindledEntity(EntityType<? extends KindledEntity> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 16.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.build();
	}

	@Override
	public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (DATA_COLOR_ID.equals(key)) {
			byte c = this.entityData.get(DATA_COLOR_ID);
			if (c == 16) {
				this.color = null;
			} else {
				this.color = DyeColor.byId(c);
			}
		}
		if (DATA_ROTATION.equals(key) && level.isClientSide()) {
			this.yBodyRot = this.entityData.get(DATA_ROTATION);
		}
	}

	@Nullable
	public DyeColor getColor() {
		return color;
	}

	private void setColor(@Nullable DyeColor color) {
		this.entityData.set(DATA_COLOR_ID, color == null ? 16 : (byte) color.getId());
	}


	private void setColor(@Range(from = 0, to = 16) byte color) {
		setColor(DyeColor.byId(color));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_COLOR_ID, (byte) 16);
		this.entityData.define(DATA_ROTATION, this.yBodyRot);
	}

	@Override
	public boolean hurt(@NotNull DamageSource source, float pAmount) {
		if (source.isProjectile()) {
			Entity entity1 = source.getDirectEntity();
			if (entity1 != null && entity1.getType() == EntityType.SHULKER_BULLET) {
				poofTimer += 5;
			}
		}
		return super.hurt(source, pAmount);
	}

	@Override
	@NotNull //DEBUG
	protected InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
		if (this.level.isClientSide()) return InteractionResult.PASS;
			ItemStack heldItem = player.getItemInHand(hand);
		Item item = heldItem.getItem();
		if (candles.containsKey(item)) {
			setColor(candles.get(item));
			//setYBodyRot(yBodyRot + 70);
			spin();
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

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
			setColor(tag.getByte("Type"));
		}
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		DyeColor color = getColor();
		tag.putByte("Color", color == null ? 16 : (byte) getColor().getId());
	}

	public boolean isPoofReady() {
		return poofTimer > 60;
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
				setCustomName(Component.literal(String.valueOf(yBodyRot)));
				setCustomNameVisible(true); //DEBUG
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
				return kindled.getLevel().getDifficulty() != Difficulty.PEACEFUL;
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
					kindled.level.addFreshEntity(new ShulkerBullet(kindled.level, kindled, target, Direction.Axis.X));
				} else {
					kindled.spin();
				}
			} else {
				kindled.setTarget(null);
			}
		}
	}

	private void turn(float f) {
		setYBodyRot(f);
		this.entityData.set(DATA_ROTATION, f);
	}

	private void spin() {
		float newRot = random.nextBoolean() ? yBodyRot - 80 : yBodyRot + 80;
		turn(newRot);
	}

	class PoofGoal extends BaseKindledGoal {
		private int poofTimer;

		@Override
		public boolean canUse() {
			return false;
		}
	}


}
