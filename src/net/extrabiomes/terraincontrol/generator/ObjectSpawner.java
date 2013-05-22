package net.extrabiomes.terraincontrol.generator;

import java.util.Random;

import net.extrabiomes.terraincontrol.DefaultMaterial;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.TerrainControl;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.extrabiomes.terraincontrol.configuration.TCDefaultValues;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.extrabiomes.terraincontrol.generator.resourcegens.Resource;


public class ObjectSpawner
{
    private WorldConfig worldSettings;
    private Random rand;
    private LocalWorld world;

    public ObjectSpawner(WorldConfig wrk, LocalWorld localWorld)
    {
        this.worldSettings = wrk;
        this.rand = new Random();
        this.world = localWorld;
    }

    public void populate(int chunkX, int chunkZ)
    {
        // Get the corner block coords
        int x = chunkX * 16;
        int z = chunkZ * 16;

        // Get the BiomeConfig of the other corner
        int biomeId = world.getBiomeId(x + 15, z + 15);
        BiomeConfig localBiomeConfig = this.worldSettings.biomeConfigs[biomeId];
        
        // Get the random generator
        long resourcesSeed = worldSettings.resourcesSeed != 0L? worldSettings.resourcesSeed : world.getSeed();
        this.rand.setSeed(resourcesSeed);
        long l1 = this.rand.nextLong() / 2L * 2L + 1L;
        long l2 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(chunkX * l1 + chunkZ * l2 ^ resourcesSeed);

        // Generate structures
        boolean hasGeneratedAVillage = world.PlaceTerrainObjects(rand, chunkX, chunkZ);

        // Fire event
        TerrainControl.firePopulationStartEvent(world, rand, hasGeneratedAVillage, chunkX, chunkZ);

        // Resource sequence
        for (int i = 0; i < localBiomeConfig.ResourceCount; i++)
        {
            Resource res = localBiomeConfig.ResourceSequence[i];
            world.setChunksCreations(false);
            res.process(world, rand, hasGeneratedAVillage, chunkX, chunkZ);
        }

        // Animals
        world.placePopulationMobs(localBiomeConfig, rand, chunkX, chunkZ);

        // Snow and ice
        placeSnowAndIce(chunkX, chunkZ);

        // Replace blocks
        world.replaceBlocks();

        // Replace biomes
        world.replaceBiomes();

        // Replace settings after Reload command
        if (this.worldSettings.isDeprecated)
            this.worldSettings = this.worldSettings.newSettings;

        // Fire event
        TerrainControl.firePopulationEndEvent(world, rand, hasGeneratedAVillage, chunkX, chunkZ);
    }

    protected void placeSnowAndIce(int chunkX, int chunkZ)
    {
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                int blockToFreezeX = x + i;
                int blockToFreezeZ = z + j;
                BiomeConfig biomeConfig = worldSettings.biomeConfigs[world.getBiomeId(blockToFreezeX, blockToFreezeZ)];
                if (biomeConfig.BiomeTemperature < TCDefaultValues.snowAndIceMaxTemp.floatValue())
                {
                    int blockToFreezeY = world.getHighestBlockYAt(blockToFreezeX, blockToFreezeZ);
                    if (blockToFreezeY > 0)
                    {
                        // Ice has to be placed one block in the world
                        if (DefaultMaterial.getMaterial(world.getTypeId(blockToFreezeX, blockToFreezeY - 1, blockToFreezeZ)).isLiquid())
                        {
                            world.setBlock(blockToFreezeX, blockToFreezeY - 1, blockToFreezeZ, biomeConfig.iceBlock, 0);
                        } else
                        {
                            // Snow has to be placed on an empty space on a
                            // block that accepts snow in the world
                            if (world.getMaterial(blockToFreezeX, blockToFreezeY, blockToFreezeZ) == DefaultMaterial.AIR)
                            {
                                if (world.getMaterial(blockToFreezeX, blockToFreezeY - 1, blockToFreezeZ).isSolid())
                                {
                                    world.setBlock(blockToFreezeX, blockToFreezeY, blockToFreezeZ, DefaultMaterial.SNOW.id, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}