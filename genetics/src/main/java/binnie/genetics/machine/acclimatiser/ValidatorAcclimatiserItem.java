package binnie.genetics.machine.acclimatiser;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import binnie.core.machines.inventory.SlotValidator;
import binnie.core.util.I18N;
import binnie.genetics.api.acclimatiser.IToleranceType;

public class ValidatorAcclimatiserItem extends SlotValidator {
	public ValidatorAcclimatiserItem() {
		super(null);
	}

	@Override
	public boolean isValid(ItemStack stack) {
		for (IToleranceType type : Acclimatiser.getToleranceTypes()) {
			if (type.hasEffect(stack)) {
				return true;
			}
		}
		if (stack.getItem() == Items.BUCKET) { //ugly hack, TODO - improve to general fluid items
			return true;
		}
		return false;
	}

	@Override
	public String getTooltip() {
		return I18N.localise("genetics.machine.lab_machine.acclimatiser.tooltips.slots.acclimatising");
	}
}
