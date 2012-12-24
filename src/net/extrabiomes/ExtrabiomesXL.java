
package net.extrabiomes;

import java.io.File;
import java.util.logging.Level;

import net.extrabiomes.lib.Reference;
import net.extrabiomes.networking.ConnectionHandler;
import net.extrabiomes.proxy.CommonProxy;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.TerrainControl;
import net.extrabiomes.terraincontrol.TerrainControlEngine;
import net.extrabiomes.terraincontrol.customobjects.BODefaultValues;
import net.extrabiomes.terraincontrol.util.Txt;
import net.extrabiomes.utility.LogWriter;
import net.extrabiomes.world.ExtrabiomesWorldType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.server.MinecraftServer;
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

    private CreativeTabs   tabsEBXL = new CreativeTabs("extrabiomesTab");
    private ExtrabiomesWorldType worldType;
    private File extrabiomesConfigurationDirectory; 
    private File terrainControlDirectory; 
    
    public File getTerrainControlDirectory() {
        return terrainControlDirectory;
    }

    @Override
    public File getGlobalObjectsDirectory() {
        return new File(terrainControlDirectory,
                BODefaultValues.BO_GlobalDirectoryName.stringValue());
    }

    @Override
    public LocalWorld getWorld(final String name) {
        final LocalWorld world = worldType.worldTC;
        if (world == null) return null;
        final String worldName = MinecraftServer.getServer().worldServers[0].getSaveHandler()
                .getSaveDirectoryName();
        if (world.getName() == worldName)
            return world;
        else {
            // Outdated world stored
            worldType.worldTC = null;
            return null;
        }

    }

    @PreInit
    public void handlePreInit(final FMLPreInitializationEvent event) {
        LogWriter.fine("Running pre-initialization tasks...");
        
        extrabiomesConfigurationDirectory = new File(event.getModConfigurationDirectory(), "extrabiomes");
        terrainControlDirectory = new File(extrabiomesConfigurationDirectory, "TerrainControl");

        // Load configuration
        // Create blocks
        // Create items

        // Start TerrainControl engine
        TerrainControl.supportedBlockIds = 4095;
        TerrainControl.startEngine(this);

        worldType = new ExtrabiomesWorldType();

        proxy.addStringLocalization("itemGroup.extrabiomesTab", Reference.MOD_NAME);
        proxy.addStringLocalization("generator.ebxl", Reference.MOD_NAME);

        LogWriter.fine("Pre-initizalization complete.");
    }

    @Override
    public void log(final Level level, final String... messages) {
        LogWriter.INSTANCE.log(level, Txt.implode(messages, ","));
    }

}
