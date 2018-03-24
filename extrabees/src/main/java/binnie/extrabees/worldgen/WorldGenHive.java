package binnie.extrabees.worldgen;


import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenHive extends WorldGenerator {
	private final float rate;

	public WorldGenHive(float rate) {
		this.rate = rate;
	}

	public float getRate() {
		return rate;
	}
}
