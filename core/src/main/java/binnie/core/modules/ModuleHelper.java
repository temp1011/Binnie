package binnie.core.modules;

import forestry.api.core.ForestryAPI;

public class ModuleHelper {
	private ModuleHelper() {

	}

	public static boolean isModuleEnabled(String containerID, String moduleID) {
		return ForestryAPI.moduleManager.isModuleEnabled(containerID, moduleID);
	}
}
