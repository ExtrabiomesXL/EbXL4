
package net.extrabiomes.world;

import java.io.File;

import net.extrabiomes.ExtrabiomesXL;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.extrabiomes.terraincontrol.wrapper.BiomeManager;
import net.extrabiomes.terraincontrol.wrapper.BiomeManagerOld;
import net.extrabiomes.terraincontrol.wrapper.SingleWorld;
import net.extrabiomes.utility.LogWriter;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * ExtrabiomesWorldType - Used for Extrabiomes worlds
 * 
 * @author ScottKillen
 * 
 */
public class ExtrabiomesWorldType extends WorldType {

    public SingleWorld worldTC;

    public ExtrabiomesWorldType() {
        super(WorldTypeHelper.getNextWorldTypeID(), "ebxl");
    }

    @Override
    public IChunkProvider getChunkGenerator(final World world, final String generatorOptions) {
        if (worldTC.getSettings().ModeTerrain != WorldConfig.TerrainMode.Default)
            return worldTC.getChunkGenerator();
        else
            return super.getChunkGenerator(world, generatorOptions);
    }

    @Override
    public WorldChunkManager getChunkManager(final World world) {
        try {
            if (world instanceof WorldClient) return super.getChunkManager(world);
        } catch (final NoClassDefFoundError e) {
            // There isn't a WorldClient class, so we are on a
            // stand-alone
            // server. Continue normally.
        }

        // Restore old biomes
        SingleWorld.restoreBiomes();

        // Load everything
        final File worldDirectory = new File(ExtrabiomesXL.instance.getTerrainControlDirectory(),
                "Worlds/" + world.getSaveHandler().getSaveDirectoryName());

        if (!worldDirectory.exists()) {
            LogWriter
                    .fine("Terrain Control: settings for save \"%s\" do not exist, creating defaults",
                            world.getSaveHandler().getSaveDirectoryName());

            if (!worldDirectory.mkdirs())
                System.out.println("Terrain Control: cant create folder "
                        + worldDirectory.getAbsolutePath());
        }

        worldTC = new SingleWorld(world.getSaveHandler().getSaveDirectoryName());
        final WorldConfig config = new WorldConfig(worldDirectory, worldTC, false);
        worldTC.Init(world, config);

        WorldChunkManager ChunkManager = null;

        switch (worldTC.getSettings().ModeBiome)
        {
            case FromImage:
            case Normal:
                ChunkManager = new BiomeManager(worldTC);
                worldTC.setBiomeManager((BiomeManager) ChunkManager);
                break;
            case OldGenerator:
                ChunkManager = new BiomeManagerOld(worldTC);
                worldTC.setOldBiomeManager((BiomeManagerOld) ChunkManager);
                break;
            case Default:
                ChunkManager = super.getChunkManager(world);
                break;
        }

        return ChunkManager;
    }

}
