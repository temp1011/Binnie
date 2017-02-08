package binnie.botany.genetics;

import binnie.botany.Botany;
import binnie.botany.CreativeTabBotany;
import binnie.botany.core.BotanyGUI;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDictionary extends Item implements IItemModelRegister {

	public ItemDictionary() {
		this.setCreativeTab(CreativeTabBotany.instance);
		this.setUnlocalizedName("database");
		this.setMaxStackSize(1);
		setRegistryName("database");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final NonNullList<ItemStack> par3List) {
		super.getSubItems(par1, par2CreativeTabs, par3List);
		par3List.add(new ItemStack(par1, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0, "botanistDatabase");
		manager.registerItemModel(item, 1, "masterBotanistDatabase");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		if (itemStack.getMetadata() == 0) {
			Botany.proxy.openGui(BotanyGUI.Database, playerIn, playerIn.getPosition());
		} else {
			Botany.proxy.openGui(BotanyGUI.DatabaseNEI, playerIn, playerIn.getPosition());
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack i) {
		return (i.getItemDamage() == 0) ? "Botanist Database" : "Master Botanist Database";
	}
}
