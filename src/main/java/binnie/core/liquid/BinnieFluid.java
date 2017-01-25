package binnie.core.liquid;

import java.awt.Color;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

class BinnieFluid extends Fluid {
	private final String name;
	private final IFluidType fluidType;
	private final int color;

	@Override
	public String getLocalizedName(FluidStack stack) {
		return this.name;
	}

	public BinnieFluid(final IFluidType fluid) {
		super(fluid.getIdentifier(), fluid.getStill(), fluid.getFlowing());
		this.fluidType = fluid;
		this.name = fluid.getDisplayName();
		this.color = new Color(this.fluidType.getColour()).getRGB();
	}

	@Override
	public int getColor() {
		return color;
	}

	public IFluidType getType() {
		return this.fluidType;
	}
}
