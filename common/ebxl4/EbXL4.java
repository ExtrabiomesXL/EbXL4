package ebxl4;

import java.io.File;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import ebxl4.api.APIVersion;
import ebxl4.exceptions.InvalidAPIException;
import ebxl4.exceptions.NoFreeWorldIDsException;
import ebxl4.exceptions.WorldIDOverriddenException;
import ebxl4.exceptions.WorldIDTakenException;
import ebxl4.lib.EBXLCommandHandler2;
import ebxl4.lib.EbXL4Configuration;
import ebxl4.lib.GeneralSettings;
import ebxl4.lib.ModInfo;
import ebxl4.proxy.CommonProxy;
import ebxl4.world.EbXL4WorldType;
import ebxl4.lib.LogHelper;
import ebxl4.network.EbXL4Connection;
import ebxl4.network.EbXL4Packet;
import ebxl4.lib.EBXLCommandHandler;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels={"EbXL++channel"} , packetHandler=EbXL4Packet.class)
public class EbXL4 {
  @Instance(ModInfo.MOD_ID)
  public static EbXL4         instance;
  
  // Local Variables
  public static EbXL4WorldType worldType;
  
  @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
  public static CommonProxy         proxy;
  
  @SuppressWarnings("unused")
  @Mod.EventHandler
  public static void preInit(FMLPreInitializationEvent event) throws Exception, NoFreeWorldIDsException, WorldIDTakenException {
    LogHelper.info("Doing Pre Init Stuff.");
    if(APIVersion.version != ModInfo.MOD_VERSION) {
      if(APIVersion.modId.length() > 0) {
        LogHelper.severe("EbXL++ API has been modified by %s which is using the API for EbXL++ %s. Please ask the author's of %s to use the dependencies=\"after:EbXL++\" in their @mod annotation, and update their EbXL++ API.", APIVersion.modId, APIVersion.version, APIVersion.modId);
        throw new InvalidAPIException();
      } else {
        LogHelper.severe("EbXL++ API has been modified by an unknown mod using the API for EbXL++ %s.", APIVersion.version);
      }
    }
    
    // Initialize the general configuration settings
    EbXL4Configuration.cfgFile = new File(event.getModConfigurationDirectory(), "/ebxlpp.cfg"); 
    EbXL4Configuration.init(EbXL4Configuration.cfgFile);
    
    if(GeneralSettings.worldID != -1) {
      worldType = new EbXL4WorldType(GeneralSettings.worldID, "EbXL++");
    }
  }
  
  @Mod.EventHandler
  public static void init(FMLInitializationEvent event) throws InstantiationException, IllegalAccessException {
    LogHelper.info("Initializing.");
    NetworkRegistry.instance().registerConnectionHandler(new EbXL4Connection());
  }
  
  @Mod.EventHandler
  public static void postInit(FMLPostInitializationEvent event) throws WorldIDOverriddenException {
    LogHelper.info("Doing post initilization stuff.");
    // Check to make sure that no other mods overwrote the world type
    if(WorldType.worldTypes[GeneralSettings.worldID] != worldType) {
      LogHelper.severe("%s's World ID %d was overridden by another mod.", ModInfo.MOD_NAME, GeneralSettings.worldID);
      EbXL4Configuration.fixWorldType(EbXL4Configuration.cfgFile);
      throw new WorldIDOverriddenException();
    }
  }
  
  @Mod.EventHandler
  public void serverStart(FMLServerStartingEvent event) {
    
    MinecraftServer server = MinecraftServer.getServer(); //Gets current server
    ICommandManager command = server.getCommandManager(); //Gets the command manager to use for server
    ServerCommandManager serverCommand = ((ServerCommandManager) command); //Turns it into another form to use
    
    serverCommand.registerCommand(new EBXLCommandHandler());
    serverCommand.registerCommand(new EBXLCommandHandler2());
  }
}
