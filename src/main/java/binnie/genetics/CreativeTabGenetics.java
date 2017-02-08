package binnie.genetics;

import binnie.extrabees.ExtraBees;
import binnie.genetics.item.GeneticsItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabGenetics extends CreativeTabs {
	public static CreativeTabs instance = new CreativeTabGenetics();

	@Override
	public ItemStack getIconItemStack() {
		return GeneticsItems.EmptySerum.get(1);
	}

	public CreativeTabGenetics() {
		super("Genetics");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return this.getTabLabel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTabLabel() {
		return ExtraBees.proxy.localise("tab.genetics");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		//TODO add icon item for genetics
		return ItemStack.EMPTY;
	}

}
