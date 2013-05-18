
package net.extrabiomes;

import java.io.File;
import java.util.logging.Level;

import net.extrabiomes.generation.ExtraBiomesWorldGenerator;
import net.extrabiomes.generation.biomes.YellowStone;
import net.extrabiomes.lib.Reference;
import net.extrabiomes.lib.Settings;
import net.extrabiomes.networking.ConnectionHandler;
import net.extrabiomes.proxy.CommonProxy;
import net.extrabiomes.terraincontrol.util.WorldHelper;
import net.extrabiomes.utility.LogWriter;
import net.extrabiomes.world.BiomeManager;
import net.extrabiomes.world.ExtrabiomesWorldType;
import net.minecraft.creativetab.CreativeTabs;

import com.google.common.base.Optional;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.TerrainControlEngine;
import com.khorn.terraincontrol.customobjects.BODefaultValues;
import com.khorn.terraincontrol.util.StringHelper;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

/**
 * ExtrabiomesXL - the main mod class
 * 
 * @author ScottKillen
 * 
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = { Reference.MOD_CHANNEL }, packetHandler = net.extrabiomes.networking.PacketHandler.class), connectionHandler = ConnectionHandler.class)
public final class ExtrabiomesXL implements TerrainControlEngine {

    @Instance(Reference.MOD_ID)
    public static ExtrabiomesXL  instance;

    @SidedProxy(clientSide = Reference.MOD_CLIENT_PROXY, serverSide = Reference.MOD_PROXY)
    public static CommonProxy    proxy;
    
    public static BiomeManager biomeManager;

    @Override
    public File getGlobalObjectsDirectory() {
        return new File(terrainControlDirectory,
                BODefaultValues.BO_GlobalDirectoryName.stringValue());
    }

    @Override
    public LocalWorld getWorld(final String name) {
        final Optional<LocalWorld> worldTC = Settings.getTCWorld();
        if (!worldTC.isPresent()){
        	return null;
        }
        LocalWorld world = worldTC.get();
        return world;
       /* final String worldName = MinecraftServer.getServer().worldServers[0].getSaveHandler()
                .getSaveDirectoryName();
        if (world.getName() == worldName){
            return world;
        }else{
            // Outdated world stored
            Settings.getWorldType().worldTC = null;
            return null;
        }*/

    }

    private File terrainControlDirectory;
    
    @PreInit
    public void handlePreInit(final FMLPreInitializationEvent event) {
        LogWriter.fine("Running pre-initialization tasks...");
        
        Settings.setCreativeTab(new CreativeTabs("extrabiomesTab"));
        LogWriter.fine("Created creative tab for mod items.");
        
        terrainControlDirectory = new File(event.getModConfigurationDirectory(), "TerrainControl");
        

        // Load configuration
        // Create blocks
        // Create items

        // Start TerrainControl engine
        TerrainControl.supportedBlockIds = 4095;
        TerrainControl.startEngine(this);

        biomeManager = new BiomeManager();
        
        //TODO Move somewhere else and load based off config file
        biomeManager.RegisterBiome(new YellowStone(20, "Yellowstone"));
        
        Settings.setWorldType(new ExtrabiomesWorldType(WorldHelper.getNextWorldTypeID(), "ebxl"));

        proxy.addStringLocalization("itemGroup.extrabiomesTab", Reference.MOD_NAME);
        proxy.addStringLocalization("generator.ebxl", Reference.MOD_NAME);

        LogWriter.fine("Pre-initizalization complete.");
    }

    @Override
    public void log(final Level level, final String... messages) {
        LogWriter.INSTANCE.log(level, StringHelper.join(messages, ","));
    }

    public File getTerrainControlDirectory() {
        return terrainControlDirectory;
    }

}
