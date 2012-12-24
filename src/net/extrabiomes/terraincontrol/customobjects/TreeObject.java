package net.extrabiomes.terraincontrol.customobjects;

import java.util.Map;
import java.util.Random;

import net.extrabiomes.terraincontrol.LocalBiome;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.generator.resourcegens.TreeType;

public class TreeObject implements CustomObject
{
    TreeType type;
    
    public TreeObject(TreeType type)
    {
        this.type = type;
    }
    
    @Override
    public String getName()
    {
        return type.name();
    }

    @Override
    public boolean canSpawnAsTree()
    {
        return true;
    }
    
    @Override
    public boolean canSpawnAsObject()
    {
        return false;
    }

    @Override
    public boolean spawn(LocalWorld world, Random random, int x, int y, int z)
    {
        return world.PlaceTree(type, random, x, y, z);
    }

    @Override
    public boolean spawnAsTree(LocalWorld world, Random random, int x, int y, int z)
    {
        return world.PlaceTree(type, random, x, y, z);
    }
    
    @Override
    public boolean spawn(LocalWorld world, Random random, int x, int z)
    {
        return world.PlaceTree(type, random, x, world.getHighestBlockYAt(x, z), z);
    }

    @Override
    public boolean spawnAsTree(LocalWorld world, Random random, int x, int z)
    {
        return world.PlaceTree(type, random, x, world.getHighestBlockYAt(x, z), z);
    }

    @Override
    public boolean process(LocalWorld world, Random random, int chunkX, int chunkZ)
    {
        // A tree has no rarity, so spawn it once in the chunk
        int x = chunkX * 16 + random.nextInt(16);
        int z = chunkZ * 16 + random.nextInt(16);
        int y = world.getHighestBlockYAt(x, z);
        return world.PlaceTree(type, random, x, y, z);
    }

    @Override
    public boolean processAsTree(LocalWorld world, Random random, int chunkX, int chunkZ)
    {
        return process(world, random, chunkX, chunkZ);
    }

    @Override
    public CustomObject applySettings(Map<String, String> settings)
    {
        // Trees don't support this
        return this;
    }

    @Override
    public boolean hasPreferenceToSpawnIn(LocalBiome biome)
    {
        return true;
    }

}
