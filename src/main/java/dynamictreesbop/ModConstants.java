package dynamictreesbop;

public class ModConstants extends com.ferreusveritas.dynamictrees.ModConstants {
	
	public static final String MODID = "dynamictreesbop";
	public static final String NAME = "Dynamic Trees BOP";
	public static final String VERSIONAUTO = "@VERSION@";
	public static final String VERSION = VERSIONAUTO;//Change to VERSIONDEV in development, VERSIONAUTO for release
	
	//Other Mods
	public static final String BOP = "biomesoplenty";
	public static final String RUSTIC = "rustic";
	
	//Other Mod Versions
	public static final String BOP_VER = AT + "7.0.1.2344" + ORGREATER;

	public static final String DEPENDENCIES
		= REQAFTER + DYNAMICTREES_LATEST
		+ NEXT
		+ REQAFTER + BOP + BOP_VER
		+ NEXT
		+ BEFORE + RUSTIC
		+ NEXT
		+ BEFORE + DYNAMICTREESTC
		;	

}
