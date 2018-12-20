package dynamictreesbop;

public class ModConstants extends com.ferreusveritas.dynamictrees.ModConstants {
	
	public static final String MODID = "dynamictreesbop";
	public static final String NAME = "Dynamic Trees BOP";
	public static final String VERSION = "1.12.2-9999.9999.9999z";//Maxed out version to satisfy dependencies during dev, Assigned from gradle during build, do not change
	
	//Other Mods
	public static final String BOP = "biomesoplenty";
	public static final String RUSTIC = "rustic";
	
	//Other Mod Versions
	public static final String BOP_VER = AT + "7.0.1.2422" + ORGREATER;

	public static final String DEPENDENCIES
		= REQAFTER + DYNAMICTREES_LATEST
		+ NEXT
		+ REQAFTER + BOP + BOP_VER
		+ NEXT
		+ OPTBEFORE + RUSTIC
		+ NEXT
		+ OPTBEFORE + DYNAMICTREESTC
		;	

}
