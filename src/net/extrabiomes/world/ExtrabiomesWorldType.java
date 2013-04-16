
package net.extrabiomes.world;

import java.io.File;

import net.extrabiomes.ExtrabiomesXL;
import net.extrabiomes.terraincontrol.SingleWorld;
import net.extrabiomes.terraincontrol.TCWorldChunkManager;
import net.extrabiomes.terraincontrol.TCWorldChunkManagerOld;
import net.extrabiomes.terraincontrol.util.WorldHelper;
import net.extrabiomes.utility.LogWriter;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

import com.khorn.terraincontrol.configuration.WorldConfig;

/**
 * ExtrabiomesWorldType - Used for Extrabiomes worlds
 * 
 * @author ScottKillen
 * 
 */
public class ExtrabiomesWorldType extends WorldType
{
    public SingleWorld worldTC;

    public ExtrabiomesWorldType(final int index, final String name) {
        super(index, name);
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
            // stand-alone server. Continue normally.
        }

        // Restore old biomes
        SingleWorld.restoreBiomes();

        // Load everything
        final File worldDirectory = new File(ExtrabiomesXL.instance.getTerrainControlDirectory(),
                "Worlds/" + world.getSaveHandler().getSaveDirectoryName());

        if (!worldDirectory.exists()) {
            LogWriter.fine(
                    "Terrain Control: settings for save \"%s\" do not exist, creating defaults",
                    world.getSaveHandler().getSaveDirectoryName());

            if (!worldDirectory.mkdirs())
                System.out.println("Terrain Control: cant create folder "
                        + worldDirectory.getAbsolutePath());
        }

        worldTC = new SingleWorld(world.getSaveHandler().getSaveDirectoryName());
        final WorldConfig config = new WorldConfig(worldDirectory, worldTC, false);
        worldTC.Init(world, config);

        WorldChunkManager chunkManager = null;

        switch (worldTC.getSettings().ModeTerrain)
        {
            case Normal:
                chunkManager = new TCWorldChunkManager(worldTC);
                worldTC.setBiomeManager((TCWorldChunkManager) chunkManager);
                break;
            case OldGenerator:
                chunkManager = new TCWorldChunkManagerOld(worldTC);
                worldTC.setOldBiomeManager((TCWorldChunkManagerOld) chunkManager);
                break;
            case Default:
                chunkManager = super.getChunkManager(world);
                break;
        }

        return chunkManager;
    }

    @Override
    public int getMinimumSpawnHeight(final World world) {
        return WorldHelper.toLocalWorld(world).getSettings().waterLevelMax;
    }
    
    public SingleWorld getWorldTC() {
		return worldTC;
	}
}
