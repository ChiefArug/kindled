package chiefarug.mods.kindled;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class MagicDustItem extends Item {
	public MagicDustItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		Player player = context.getPlayer();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();

		if (player instanceof ServerPlayer) // Make sure to trigger advancement stuff
			CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, stack);

		// Null check here cause its @Nullable
		boolean keepItem = player == null || player.getAbilities().instabuild;
		return InWorldTransforms.pumpkinToMagicPumpkin(
				context.getLevel(),
				pos,
				stack,
				context.getClickedFace(),
				!keepItem
		).orElseGet(() -> super.useOn(context));
	}
}
