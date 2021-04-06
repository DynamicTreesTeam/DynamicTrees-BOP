package dynamictreesbop;

import net.minecraftforge.common.config.Config;

@Config(modid = DynamicTreesBOP.MODID)
public class ModConfigs {

	@Config.Comment("If enabled persimmon trees will be generated during worldgen and autumnal, maple and dead trees will not drop persimmons")
	@Config.Name("Persimmon Trees")
	@Config.RequiresMcRestart()
	public static boolean enablePersimmonTrees = true;

	@Config.Comment("If enabled peach trees will be generated during worldgen and flowering, jacaranda and mahogany trees will not drop peaches")
	@Config.Name("Peach Trees")
	@Config.RequiresMcRestart()
	public static boolean enablePeachTrees = true;

	@Config.Comment("If enabled pear trees will be generated during worldgen and mangrove and willow trees will not drop pears")
	@Config.Name("Pear Trees")
	@Config.RequiresMcRestart()
	public static boolean enablePearTrees = true;

	@Config.Comment("If enabled poplar variants will drop their own seeds that can be planted outside of the grove biome")
	@Config.Name("Poplar Seeds")
	@Config.RequiresMcRestart()
	public static boolean enablePoplarSeeds = true;

}
