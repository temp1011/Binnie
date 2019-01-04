package binnie.genetics.machine.acclimatiser;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import binnie.core.machines.IMachine;
import binnie.core.machines.MachineUtil;
import binnie.core.machines.errors.ErrorState;
import binnie.core.machines.power.ComponentProcessIndefinate;
import binnie.genetics.config.ConfigurationMain;
import binnie.genetics.machine.GeneticsErrorCode;

public class AcclimatiserLogic extends ComponentProcessIndefinate {
	public AcclimatiserLogic(final IMachine machine) {
		super(machine, ConfigurationMain.acclimatiserEnergy);
	}

	@Override
	public ErrorState canWork() {
		final MachineUtil machineUtil = getUtil();
		if (machineUtil.getStack(Acclimatiser.SLOT_TARGET).isEmpty()) {
			return new ErrorState(GeneticsErrorCode.NO_INDIVIDUAL, Acclimatiser.SLOT_TARGET);
		}
		if (machineUtil.getNonEmptyStacks(Acclimatiser.SLOT_ACCLIMATISER).isEmpty()) {
			return new ErrorState(GeneticsErrorCode.ACCLIMATISER_NO_ITEM, Acclimatiser.SLOT_ACCLIMATISER);
		}
		return super.canWork();
	}

	@Override
	public ErrorState canProgress() {
		final MachineUtil machineUtil = getUtil();
		if (!Acclimatiser.canAcclimatise(machineUtil.getStack(Acclimatiser.SLOT_TARGET), machineUtil.getNonEmptyStacks(Acclimatiser.SLOT_ACCLIMATISER))) {
			return new ErrorState(GeneticsErrorCode.ACCLIMATISER_CAN_NOT_WORK, Acclimatiser.SLOT_TARGET);
		}
		return super.canProgress();
	}

	@Override
	protected boolean inProgress() {
		return this.canWork() == null;
	}

	@Override
	protected void onTickTask() {
		super.onTickTask();
		if (this.getUtil().getRandom().nextInt(100) == 0) {
			this.attemptAcclimatisation();
		}
	}

	protected void attemptAcclimatisation() {
		MachineUtil machineUtil = getUtil();
		ItemStack target = machineUtil.getStack(Acclimatiser.SLOT_TARGET);
		if (target.isEmpty()) {
			return;
		}

		for (int i : Acclimatiser.SLOT_ACCLIMATISER) {
			ItemStack s = machineUtil.getStack(i);
			if (!s.isEmpty() && Acclimatiser.canAcclimatise(target, s)) {
				ItemStack toUse = s.copy();


				IFluidHandlerItem handler = FluidUtil.getFluidHandler(s);
				if (handler != null) {
					FluidStack sim = handler.drain(Fluid.BUCKET_VOLUME, false);
					if (sim == null || sim.amount != Fluid.BUCKET_VOLUME) {
						continue;
					} else {
						handler.drain(Fluid.BUCKET_VOLUME, true);
						machineUtil.setStack(i, handler.getContainer());
					}
				} else {
					machineUtil.decreaseStack(i, 1);
				}


				ItemStack acclimed = Acclimatiser.acclimatise(toUse, s);
				machineUtil.setStack(Acclimatiser.SLOT_TARGET, acclimed);
				break;
			}
		}
	}
}
