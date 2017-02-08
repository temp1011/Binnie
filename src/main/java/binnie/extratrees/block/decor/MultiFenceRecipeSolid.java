package binnie.extratrees.block.decor;

import binnie.extratrees.block.WoodManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiFenceRecipeSolid implements IRecipe {
	@Nullable
	private ItemStack cached;

	@Override
	public boolean matches(final InventoryCrafting inv, final World world) {
		final String pattern = "";
		FenceType type = null;
		for (int row = 0; row < 3; ++row) {
			final ItemStack a = inv.getStackInSlot(row * 3);
			final ItemStack b = inv.getStackInSlot(row * 3 + 1);
			final ItemStack c = inv.getStackInSlot(row * 3 + 2);
			if (!a.isEmpty() && !b.isEmpty()) {
				if (!c.isEmpty()) {
					type = WoodManager.getFenceType(a);
					final FenceType type2 = WoodManager.getFenceType(b);
					final FenceType type3 = WoodManager.getFenceType(c);
					if (type != null && type2 != null && type3 != null && type.equals(type2)) {
						if (type.equals(type3)) {
							FenceDescription fenceDescription = WoodManager.getFenceDescription(a);
							this.cached = WoodManager.getFence(fenceDescription.getPlankType(), fenceDescription.getSecondaryPlankType(), new FenceType(type.size, true, type.solid), 2);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		return this.getRecipeOutput();
	}

	@Override
	public int getRecipeSize() {
		return 3;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return (this.cached == null) ? new ItemStack(Blocks.OAK_FENCE) : this.cached;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
