package chiefarug.mods.kindled;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class MagicDustItem extends Item {
	public MagicDustItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		// Null check here cause its @Nullable
		boolean consumeItem = context.getPlayer() == null || context.getPlayer().getAbilities().instabuild;
		return InWorldTransforms.pumpkinToMagicPumpkin(
				context.getLevel(),
				context.getClickedPos(),
				context.getItemInHand(),
				context.getClickedFace(),
				consumeItem
		).orElseGet(() -> super.useOn(context));
	}
}
