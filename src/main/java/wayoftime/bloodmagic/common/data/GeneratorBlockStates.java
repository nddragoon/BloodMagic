package wayoftime.bloodmagic.common.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class GeneratorBlockStates extends BlockStateProvider
{
	public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, BloodMagic.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
//		buildCubeAll(BloodMagicBlocks.TARTARICFORGE.get());
//		buildCubeAll(BloodMagicBlocks.SPEED_RUNE.get());

		for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries())
		{
			buildCubeAll(block.get());
		}

		buildCubeAll(BloodMagicBlocks.BLOOD_LIGHT.get());
	}

	private void buildCubeAll(Block block)
	{
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(cubeAll(block)).build());
	}

}
