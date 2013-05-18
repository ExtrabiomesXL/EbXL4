
package net.extrabiomes.terraincontrol.structuregens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.Biome;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig.RareBuildingType;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;


public class RareBuildingGen extends MapGenStructure
{
    public List<BiomeGenBase> biomeList;

    /**
     * contains possible spawns for scattered features
     */
    @SuppressWarnings("rawtypes")
    private final List        scatteredFeatureSpawnList;

    /**
     * the maximum distance between scattered features
     */
    private final int         maxDistanceBetweenScatteredFeatures;

    /**
     * the minimum distance between scattered features
     */
    private final int         minDistanceBetweenScatteredFeatures;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RareBuildingGen(final WorldConfig worldConfig) {
        biomeList = new ArrayList<BiomeGenBase>();

        for (final BiomeConfig biomeConfig : worldConfig.biomeConfigs) {
            if (biomeConfig == null) continue;
            if (biomeConfig.rareBuildingType != RareBuildingType.disabled)
                biomeList.add(((Biome) biomeConfig.Biome).getHandle());
        }

        scatteredFeatureSpawnList = new ArrayList();
        maxDistanceBetweenScatteredFeatures = worldConfig.maximumDistanceBetweenRareBuildings;
        // Minecraft's internal minimum distance is one chunk lower than
        // TC's value
        minDistanceBetweenScatteredFeatures = worldConfig.minimumDistanceBetweenRareBuildings - 1;
        scatteredFeatureSpawnList.add(new SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int var3 = chunkX;
        final int var4 = chunkZ;

        if (chunkX < 0) chunkX -= maxDistanceBetweenScatteredFeatures - 1;

        if (chunkZ < 0) chunkZ -= maxDistanceBetweenScatteredFeatures - 1;

        int var5 = chunkX / maxDistanceBetweenScatteredFeatures;
        int var6 = chunkZ / maxDistanceBetweenScatteredFeatures;
        final Random random = worldObj.setRandomSeed(var5, var6, 14357617);
        var5 *= maxDistanceBetweenScatteredFeatures;
        var6 *= maxDistanceBetweenScatteredFeatures;
        var5 += random.nextInt(maxDistanceBetweenScatteredFeatures
                - minDistanceBetweenScatteredFeatures);
        var6 += random.nextInt(maxDistanceBetweenScatteredFeatures
                - minDistanceBetweenScatteredFeatures);

        if (var3 == var5 && var4 == var6) {
            final BiomeGenBase biomeAtPosition = worldObj.getWorldChunkManager().getBiomeGenAt(
                    var3 * 16 + 8, var4 * 16 + 8);

            for (final BiomeGenBase biome : biomeList)
                if (biomeAtPosition.biomeID == biome.biomeID) return true;
        }

        return false;
    }

    /**
     * returns possible spawns for scattered features
     */
    public List getScatteredFeatureSpawnList() {
        return scatteredFeatureSpawnList;
    }

    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new RareBuildingStart(worldObj, rand, chunkX, chunkZ);
    }
}
