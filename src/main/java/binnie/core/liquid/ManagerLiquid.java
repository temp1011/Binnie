package binnie.core.liquid;

import binnie.core.ManagerBase;
import forestry.api.core.EnumContainerType;
import forestry.core.render.TextureManager;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ManagerLiquid extends ManagerBase {
	Map<String, IFluidType> fluids;

	public ManagerLiquid() {
		this.fluids = new LinkedHashMap<>();
		EnumContainerType.CAN.add(new BinnieContainerPermission(FluidContainerType.CAN));
		EnumContainerType.CAPSULE.add(new BinnieContainerPermission(FluidContainerType.CAPSULE));
		EnumContainerType.REFRACTORY.add(new BinnieContainerPermission(FluidContainerType.REFARACTORY));
		EnumContainerType.GLASS.add(new BinnieContainerPermission(FluidContainerType.GLASS));
	}

	public Collection<IFluidType> getFluidTypes() {
		return this.fluids.values();
	}

	public void createLiquids(final IFluidType[] liquids) {
		for (final IFluidType liquid : liquids) {
			final BinnieFluid fluid = this.createLiquid(liquid);
			if (fluid == null) {
				throw new RuntimeException("Liquid registered incorrectly - " + liquid.getIdentifier());
			}
		}
	}

	public BinnieFluid createLiquid(final IFluidType fluid) {
		this.fluids.put(fluid.getIdentifier().toLowerCase(), fluid);
		final BinnieFluid bFluid = new BinnieFluid(fluid);
		FluidRegistry.registerFluid(bFluid);
		FluidRegistry.addBucketForFluid(bFluid);
		return bFluid;
	}

	public FluidStack getFluidStack(final String name, final int amount) {
		return FluidRegistry.getFluidStack(name.toLowerCase(), amount);
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
		for (final IFluidType fluid : this.fluids.values()) {
			for (final FluidContainerType container : FluidContainerType.values()) {
				if (container.isActive() && fluid.canPlaceIn(container)) {
					container.registerContainerData(fluid);
				}
			}
		}
	}

	public IFluidType getFluidType(final String liquid) {
		return this.fluids.get(liquid.toLowerCase());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerSprites(TextureStitchEvent event) {
		for (IFluidType fluid : this.fluids.values()) {
			TextureManager.registerSprite(fluid.getFlowing());
			TextureManager.registerSprite(fluid.getStill());
		}
	}
}
