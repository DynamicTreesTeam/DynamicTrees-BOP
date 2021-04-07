package dynamictreesbop;

import dynamictreesbop.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=DynamicTreesBOP.MODID, name = ModConstants.NAME, version = ModConstants.VERSION, dependencies = ModConstants.DEPENDENCIES, updateJSON = "https://github.com/the-realest-stu/DynamicTrees-BOP/blob/master/version_info.json?raw=true")
public class DynamicTreesBOP {
	
	public static final String MODID = ModConstants.MODID;
	
	@Mod.Instance
	public static DynamicTreesBOP instance;
	
	@SidedProxy(clientSide = "dynamictreesbop.proxy.ClientProxy", serverSide = "dynamictreesbop.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}
	
}
