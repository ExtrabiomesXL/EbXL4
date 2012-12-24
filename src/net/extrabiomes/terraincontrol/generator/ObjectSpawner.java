package net.extrabiomes.terraincontrol.generator;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;

import java.util.Random;

import net.extrabiomes.terraincontrol.DefaultMaterial;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.extrabiomes.terraincontrol.configuration.TCDefaultValues;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.extrabiomes.terraincontrol.generator.resourcegens.OreGen;
import net.extrabiomes.terraincontrol.generator.resourcegens.Resource;
import net.extrabiomes.terraincontrol.generator.resourcegens.SmallLakeGen;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.terraingen.TerrainGen;

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
        int x = chunkX * 16;
        int z = chunkZ * 16;

        int biomeId = world.getCalculatedBiomeId(x + 16, z + 16);
        BiomeConfig localBiomeConfig = this.worldSettings.biomeConfigs[biomeId];

        this.rand.setSeed(world.getSeed());
        long l1 = this.rand.nextLong() / 2L * 2L + 1L;
        long l2 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(chunkX * l1 + chunkZ * l2 ^ world.getSeed());

        boolean hasGeneratedAVillage = world.PlaceTerrainObjects(rand, chunkX, chunkZ);

        boolean hasBegunOreGen = false;
        
        // Resource sequence
        for (int i = 0; i < localBiomeConfig.ResourceCount; i++)
        {
            Resource res = localBiomeConfig.ResourceSequence[i];
            if (res instanceof SmallLakeGen && hasGeneratedAVillage)
                continue;
            world.setChunksCreations(false);
            //EBXL
            if (res instanceof OreGen) {
                if (!hasBegunOreGen) {
                    hasBegunOreGen = true;
                    MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(world.getMCWorld(), rand, chunkX, chunkZ));
                }
                if (!sendOreEvent((OreGen)res, world.getMCWorld(), rand, chunkX, chunkZ))
                    continue;
            } else {
                if (hasBegunOreGen) {
                    hasBegunOreGen = false;
                    MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(world.getMCWorld(), rand, chunkX, chunkZ));
                }
            }
            //===
            res.process(world, rand, chunkX, chunkZ);
        }

        // Snow and ice
        placeSnowAndIce(chunkX, chunkZ);

        world.replaceBlocks();

        world.replaceBiomesLate();

        if (this.worldSettings.isDeprecated)
            this.worldSettings = this.worldSettings.newSettings;
    }
    
    private boolean sendOreEvent(OreGen res, World mcWorld, Random rand, int chunkX, int chunkZ) {
        final GenerateMinable.EventType type;
        int blockID = res.getBlockId();
        
        if (blockID == Block.dirt.blockID) {
            type = DIRT;
        } else if (blockID == Block.gravel.blockID) {
            type = GRAVEL;
        } else if (blockID == Block.oreCoal.blockID) {
            type = COAL;
        } else if (blockID == Block.oreIron.blockID) {
            type = IRON;
        } else if (blockID == Block.oreGold.blockID) {
            type = GOLD;
        } else if (blockID == Block.oreRedstone.blockID) {
            type = REDSTONE;
        } else if (blockID == Block.oreDiamond.blockID) {
            type = DIAMOND;
        } else if (blockID == Block.oreLapis.blockID) {
            type = LAPIS;
        } else type = CUSTOM;
        
        return TerrainGen.generateOre(mcWorld, rand, null, chunkX, chunkZ, type);
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
                BiomeConfig biomeConfig = worldSettings.biomeConfigs[world.getCalculatedBiomeId(blockToFreezeX, blockToFreezeZ)];
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
                            // solid block in the world
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