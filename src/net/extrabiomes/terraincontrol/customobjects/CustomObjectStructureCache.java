package net.extrabiomes.terraincontrol.customobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.generator.resourcegens.CustomStructureGen;
import net.extrabiomes.terraincontrol.util.ChunkCoordinate;
import net.extrabiomes.terraincontrol.util.RandomHelper;


/**
 * Each world has a cache of unfinished structures. This class is the cache.
 *
 */
public class CustomObjectStructureCache
{
    private Map<ChunkCoordinate, CustomObjectStructure> structureCache;
    private LocalWorld world;

    public CustomObjectStructureCache(LocalWorld world)
    {
        this.world = world;
        this.structureCache = new HashMap<ChunkCoordinate, CustomObjectStructure>();
    }

    public void reload(LocalWorld world)
    {
        this.world = world;
        structureCache.clear();
    }

    public CustomObjectStructure getStructureStart(int chunkX, int chunkZ)
    {
        ChunkCoordinate coord = ChunkCoordinate.fromChunkCoords(chunkX, chunkZ);
        CustomObjectStructure structureStart = structureCache.get(coord);

        // Clear cache if needed
        if (structureCache.size() > 400)
        {
            structureCache.clear();
        }

        if (structureStart != null)
        {
            return structureStart;
        }
        // No structure found, create one
        Random random = RandomHelper.getRandomForCoords(chunkX ^ 2, (chunkZ + 1) * 2, world.getSeed());
        CustomStructureGen structureGen = world.getSettings().biomeConfigs[world.getBiomeId(chunkX * 16 + 15, chunkZ * 16 + 15)].structureGen;
        if (structureGen != null)
        {
            CustomObjectCoordinate customObject = structureGen.getRandomObjectCoordinate(random, chunkX, chunkZ);
            if (customObject != null)
            {
                structureStart = new CustomObjectStructure(world, customObject);
                structureCache.put(coord, structureStart);
                return structureStart;
            } // TODO Maybe also store that no structure was here?
        }

        return null;
    }
}
