package ebxl4;

import java.io.File;

import net.minecraft.world.WorldType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import ebxl4.exceptions.WorldIDOverriddenException;
import ebxl4.lib.EbXL4Configuration;
import ebxl4.lib.GeneralSettings;
import ebxl4.lib.ModInfo;
import ebxl4.proxy.CommonProxy;
import ebxl4.world.EbXL4WorldType;
import ebxl4.lib.LogHelper;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class EbXL4 {
  @Instance(ModInfo.MOD_ID)
  public static EbXL4         instance;
  
  // Local Variables
  public static EbXL4WorldType worldType;
  
  @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
  public static CommonProxy         proxy;
  
  @Mod.EventHandler
  public static void preInit(FMLPreInitializationEvent event) throws Exception {
    LogHelper.info("Doing Pre Init Stuff.");
    
    // Initialize the general configuration settings
    EbXL4Configuration.init(new File(event.getModConfigurationDirectory(), "/ebxlpp.cfg"));
    
    if(GeneralSettings.worldID != -1) {
      worldType = new EbXL4WorldType(GeneralSettings.worldID, "EbXL++");
    }
  }
  
  @Mod.EventHandler
  public static void init(FMLInitializationEvent event) throws InstantiationException, IllegalAccessException {
    LogHelper.info("Initializing.");
  }
  
  @Mod.EventHandler
  public static void postInit(FMLPostInitializationEvent event) {
    LogHelper.info("Doing post initilization stuff.");
    // Check to make sure that no other mods overwrote the world type
    if(WorldType.worldTypes[GeneralSettings.worldID].getWorldTypeName() != "EbXL++") {
      LogHelper.severe("%s's World ID %d was overridden by another mod.", ModInfo.MOD_NAME, GeneralSettings.worldID);
    }
  }
}
