
package net.extrabiomes.terraincontrol.structuregens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.Biome;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import com.khorn.terraincontrol.configuration.BiomeConfig;
import com.khorn.terraincontrol.configuration.BiomeConfig.VillageType;
import com.khorn.terraincontrol.configuration.WorldConfig;

public class VillageGen extends MapGenStructure
{
    /**
     * A list of all the biomes villages can spawn in.
     */
    public List<BiomeGenBase> villageSpawnBiomes;

    /**
     * Village size, 0 for normal, 1 for flat map
     */
    private final int         size;
    private final int         distance;
    private final int         minimumDistance;

    public VillageGen(final WorldConfig worldConfig) {
        size = worldConfig.villageSize;
        distance = worldConfig.villageDistance;
        minimumDistance = 8;

        // Add all village biomes to the list
        villageSpawnBiomes = new ArrayList<BiomeGenBase>();
        for (final BiomeConfig config : worldConfig.biomeConfigs) {
            if (config == null) continue;
            if (config.villageType != VillageType.disabled)
                villageSpawnBiomes.add(((Biome) config.Biome).getHandle());
        }
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int var3 = chunkX;
        final int var4 = chunkZ;

        if (chunkX < 0) chunkX -= distance - 1;

        if (chunkZ < 0) chunkZ -= distance - 1;

        int var5 = chunkX / distance;
        int var6 = chunkZ / distance;
        final Random var7 = worldObj.setRandomSeed(var5, var6, 10387312);
        var5 *= distance;
        var6 *= distance;
        var5 += var7.nextInt(distance - minimumDistance);
        var6 += var7.nextInt(distance - minimumDistance);

        if (var3 == var5 && var4 == var6) {
            final boolean canSpawn = worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8,
                    var4 * 16 + 8, 0, villageSpawnBiomes);

            if (canSpawn) return true;
        }

        return false;
    }

    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new StructureVillageStart(worldObj, rand, chunkX, chunkZ, size);
    }
}
