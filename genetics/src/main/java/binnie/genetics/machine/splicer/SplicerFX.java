package binnie.genetics.machine.splicer;

import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import binnie.core.BinnieCore;
import binnie.core.machines.IMachine;
import binnie.core.machines.MachineComponent;
import binnie.core.machines.component.IRender;
import binnie.core.machines.network.INetwork;

public class SplicerFX extends MachineComponent implements IRender.DisplayTick, IRender.Render, INetwork.TilePacketSync {
	private final EntityItem dummyEntityItem;

	public SplicerFX(final IMachine machine) {
		super(machine);
		this.dummyEntityItem = new EntityItem(machine.getWorld());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onDisplayTick(World world, BlockPos pos, Random rand) {
		final int tick = (int) (world.getTotalWorldTime() % 3L);
		if (tick == 0 && this.getUtil().getProcess().isInProgress()) {
			ParticleManager effectRenderer = BinnieCore.getBinnieProxy().getMinecraftInstance().effectRenderer;
			effectRenderer.addEffect(new SplicerParticle(world, pos));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInWorld(double x, final double y, final double z) {
		if (!this.getUtil().getProcess().isInProgress()) {
			return;
		}
		final ItemStack stack = this.getUtil().getStack(9);
		this.dummyEntityItem.world = this.getMachine().getWorld();
		this.dummyEntityItem.setItem(stack);
		final EntityItem dummyEntityItem = this.dummyEntityItem;
		dummyEntityItem.setAgeToCreativeDespawnTime(); //++dummyEntityItem.age;
		this.dummyEntityItem.hoverStart = 0.0f;
		if (stack.isEmpty()) {
			return;
		}
		final EntityPlayer player = BinnieCore.getBinnieProxy().getPlayer();
		final double dx = x + 0.5 - player.lastTickPosX;
		final double dz = z + 0.5 - player.lastTickPosZ;
		final double t = Math.atan2(dz, dx) * 180.0 / 3.1415;
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.translate(0.0f, -0.25f, 0.0f);
		BinnieCore.getBinnieProxy().getMinecraftInstance().getRenderItem().renderItem(dummyEntityItem.getItem(), ItemCameraTransforms.TransformType.FIXED);//doRender(this.dummyEntityItem, 0.0, 0.0, 0.0, 0.0f, 0.0f);
		GlStateManager.popMatrix();
	}

	@Override
	public void syncToNBT(final NBTTagCompound nbt) {
		final NBTTagCompound item = new NBTTagCompound();
		final ItemStack stack = this.getUtil().getStack(9);
		if (!stack.isEmpty()) {
			stack.writeToNBT(item);
			nbt.setTag("item", item);
		}
	}

	@Override
	public void syncFromNBT(final NBTTagCompound nbt) {
		if (nbt.hasKey("item")) {
			this.getUtil().setStack(9, new ItemStack(nbt.getCompoundTag("item")));
		} else {
			this.getUtil().setStack(9, ItemStack.EMPTY);
		}
	}

	@Override
	public void onInventoryUpdate() {
		if (!this.getUtil().isServer()) {
			return;
		}
		this.getUtil().refreshBlock();
	}

	private static class SplicerParticle extends Particle {
		private double axisX;
		private double axisZ;
		private double angle;

		public SplicerParticle(World world, BlockPos pos) {
			super(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
			axisX = this.posX;
			axisZ = this.posZ;
			angle = (int) (this.world.getTotalWorldTime() % 4L) * 0.5 * 3.1415;
			this.axisX = 0.0;
			this.axisZ = 0.0;
			this.angle = 0.0;
			this.motionX = 0.0;
			this.motionZ = 0.0;
			this.motionY = (this.rand.nextDouble() - 0.5) * 0.02;
			this.particleMaxAge = 240;
			this.particleGravity = 0.0f;
			this.canCollide = true;
			this.setRBGColorF(0.3f + this.rand.nextFloat() * 0.5f, 0.3f + this.rand.nextFloat() * 0.5f, 0.0f);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void onUpdate() {
			super.onUpdate();
			final double speed = 0.04;
			this.angle -= speed;
			final double dist = 0.25 + 0.2 * Math.sin(this.particleAge / 50.0f);
			this.setPosition(this.axisX + dist * Math.sin(this.angle), this.posY, this.axisZ + dist * Math.cos(this.angle));
			this.setAlphaF((float) Math.cos(1.57 * this.particleAge / this.particleMaxAge));
		}

		@Override
		public int getFXLayer() {
			return 0;
		}
	}
}
