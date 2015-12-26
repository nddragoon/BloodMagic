package WayofTime.bloodmagic.client.render.alchemyArray;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;

public class BindingAlchemyCircleRenderer extends AlchemyCircleRenderer {

	public float offsetFromFace = -0.9f;
	public final ResourceLocation arrayResource;
	public final ResourceLocation[] arraysResources;
	
	public final int numberOfSweeps = 5;
	public final int startTime = 50;
	public final int sweepTime = 40;
	
	public final int endTime = 300;

	public BindingAlchemyCircleRenderer() {
		this.arrayResource = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png");
		arraysResources = new ResourceLocation[5];
		arraysResources[0] = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png");
		arraysResources[1] = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png");
		arraysResources[2] = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png");
		arraysResources[3] = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png");
		arraysResources[4] = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png");
	}

	public float getAngleOfCircle(int circle, float craftTime) {
		if (circle >= 0 && circle <= 4) {
			float originalAngle = (float) (circle * 2 * Math.PI / 5d);
			
			double sweep = (craftTime - startTime)/sweepTime;
			if(sweep >= 0 && sweep < numberOfSweeps) {
				float offset = ((int)sweep)*sweepTime + startTime;
				originalAngle += 2*Math.PI*2/5*((craftTime - offset)/sweepTime + (int)sweep);
			}else if(sweep >= numberOfSweeps)
			{
				originalAngle += 2*Math.PI*2/5*numberOfSweeps + (craftTime - 5*sweepTime - startTime)*2*Math.PI*2/5/sweepTime;
			}
			
			return originalAngle;
		}

		return 0;
	}
	
	public float getAngle(float craftTime, int sweep) {
		return (float) (2*Math.PI*2/5*(craftTime)/sweepTime);
	}

	/**
	 * Returns the center-to-center distance of this circle.
	 */
	public float getDistanceOfCircle(int circle, float craftTime) {
		double sweep = (craftTime - startTime)/sweepTime;
		if(sweep >= 0 && sweep < numberOfSweeps) {
			float offset = ((int)sweep)*sweepTime + startTime;
			float angle = getAngle(craftTime - offset, (int) sweep);
			float theta2 = (float) (Math.PI - 4*Math.PI/5)/2f;
			float thetaPrime = (float) (Math.PI - theta2 - angle);
			if(thetaPrime > 0 && thetaPrime < Math.PI) {
				return (float) (2 * Math.sin(theta2) / Math.sin(thetaPrime));
			}
		} else if(sweep >= numberOfSweeps && craftTime < endTime) {
			return 2 - 2 * (craftTime - startTime - numberOfSweeps * sweepTime) / (endTime - startTime - numberOfSweeps * sweepTime);
		} else if(craftTime >= endTime) {
			return 0;
		}
		
		return 2;
	}

	public float getRotation(int circle, float craftTime) {
		float offset = 2;
		if(circle == -1) {
			return (float) (craftTime * 360 * 2/5/sweepTime);
		}
		if (craftTime >= offset) {
			float modifier = (float) Math.pow(craftTime - offset, 1.5);
			return modifier * 0.5f;
		}
		return 0;
	}

	public float getSecondaryRotation(int circle, float craftTime) {
		float offset = 50;
		if (craftTime >= offset) {
			float modifier = (float) Math.pow(craftTime - offset, 1.7);
			return modifier * 0.5f;
		}
		return 0;
	}

	public float getVerticalOffset(int circle, float craftTime) {
		if (circle >= 0 && circle <= 4) {
			if (craftTime >= 5) {
				if (craftTime <= 40) {
					return (float) ((-0.4) * Math.pow((craftTime - 5) / 35f, 3));
				} else {
					return -0.4f;
				}
			}

			return 0;
		}

		if (craftTime >= 5) {
			if (craftTime <= 40) {
				return (float) ((-0.4) * Math.pow((craftTime - 5) / 35f, 3));
			} else {
				return -0.4f;
			}
		}
		return 0;
	}

	public void renderAt(TileEntity tile, double x, double y, double z, float craftTime) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer wr = tessellator.getWorldRenderer();

		GlStateManager.pushMatrix();

		float rot = getRotation(-1, craftTime);
		float secondaryRot = getSecondaryRotation(craftTime);

		float size = 3.0F;

		// Bind the texture to the circle
		Minecraft.getMinecraft().renderEngine.bindTexture(arrayResource);

		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 1);

		GlStateManager.translate(x, y, z);

		EnumFacing sideHit = EnumFacing.UP; // Specify which face this "circle"
											// is located on
		GlStateManager.translate(sideHit.getFrontOffsetX() * offsetFromFace, sideHit.getFrontOffsetY() * offsetFromFace, sideHit.getFrontOffsetZ() * offsetFromFace);

		switch (sideHit) {
		case DOWN:
			GlStateManager.translate(0, 0, 1);
			GlStateManager.rotate(-90.0f, 1, 0, 0);
			break;
		case EAST:
			GlStateManager.rotate(-90.0f, 0, 1, 0);
			GlStateManager.translate(0, 0, -1);
			break;
		case NORTH:
			break;
		case SOUTH:
			GlStateManager.rotate(180.0f, 0, 1, 0);
			GlStateManager.translate(-1, 0, -1);
			break;
		case UP:
			GlStateManager.translate(0, 1, 0);
			GlStateManager.rotate(90.0f, 1, 0, 0);
			break;
		case WEST:
			GlStateManager.translate(0, 0, 1);
			GlStateManager.rotate(90.0f, 0, 1, 0);
			break;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5f, 0.5f, getVerticalOffset(craftTime));

		double var31 = 0.0D;
		double var33 = 1.0D;
		double var35 = 0;
		double var37 = 1;

		// GlStateManager.color(0.5f, 1f, 1f, 1f);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(rot, 0, 0, 1);
		// GlStateManager.rotate(secondaryRot, 1, 0, 0);
		// GlStateManager.rotate(secondaryRot * 0.45812f, 0, 0, 1);
		wr.begin(7, DefaultVertexFormats.POSITION_TEX);
		// wr.setBrightness(200);
		wr.pos(size / 2f, size / 2f, 0.0D).tex(var33, var37).endVertex();
		wr.pos(size / 2f, -size / 2f, 0.0D).tex(var33, var35).endVertex();
		wr.pos(-size / 2f, -size / 2f, 0.0D).tex(var31, var35).endVertex();
		wr.pos(-size / 2f, size / 2f, 0.0D).tex(var31, var37).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();

		for (int i = 0; i < 5; i++) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(arraysResources[i]);
			float newSize = 1;
			float distance = this.getDistanceOfCircle(i, craftTime);
			float angle = this.getAngleOfCircle(i, craftTime);
			float rotation = this.getRotation(i, craftTime);
			
			GlStateManager.translate(distance * Math.sin(angle), -distance * Math.cos(angle), this.getVerticalOffset(i, craftTime));
			GlStateManager.rotate(rotation, 0, 0, 1);
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.pos(newSize / 2f, newSize / 2f, 0.0D).tex(var33, var37).endVertex();
			wr.pos(newSize / 2f, -newSize / 2f, 0.0D).tex(var33, var35).endVertex();
			wr.pos(-newSize / 2f, -newSize / 2f, 0.0D).tex(var31, var35).endVertex();
			wr.pos(-newSize / 2f, newSize / 2f, 0.0D).tex(var31, var37).endVertex();
			tessellator.draw();

			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();

		// GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.popMatrix();
	}
}