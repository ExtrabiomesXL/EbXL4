
package net.extrabiomes.terraincontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.DefaultBiome;
import net.extrabiomes.terraincontrol.IBiomeManager;
import net.extrabiomes.terraincontrol.biomelayers.layers.Layer;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.MapGenVillage;


public class TCWorldChunkManager extends WorldChunkManager implements IBiomeManager
{
    private Layer                         unZoomedLayer;
    private Layer                         biomeLayer;

    private final Object                  lockObject      = new Object();

    private BiomeCache                    cache           = new BiomeCache(this);

    private final ArrayList<BiomeGenBase> biomesToSpawnIn = new ArrayList<BiomeGenBase>();

    private WorldConfig                   worldConfig;
    private SingleWorld                   localWorld;

    private final float[]                 Tbuffer         = new float[256];

    public TCWorldChunkManager(final SingleWorld world) {
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.FOREST.Id]);
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.PLAINS.Id]);
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.TAIGA.Id]);
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.DESERT_HILLS.Id]);
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.FOREST_HILLS.Id]);
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.JUNGLE.Id]);
        biomesToSpawnIn.add(BiomeGenBase.biomeList[DefaultBiome.JUNGLE_HILLS.Id]);

        Init(world);

    }

    @SuppressWarnings("rawtypes")
    public List a() {
        return biomesToSpawnIn;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean areBiomesViable(final int paramInt1, final int paramInt2, final int paramInt3,
            List paramList)
    {
        // Hack for
        // StructureVillagePieces.getNextComponentVillagePath(..)
        // (The alternative would be to completely override the village
        // spawn code)
        if (paramList == MapGenVillage.villageSpawnBiomes)
            paramList = localWorld.villageGen.villageSpawnBiomes;

        final int i = paramInt1 - paramInt3 >> 2;
        final int j = paramInt2 - paramInt3 >> 2;
        final int k = paramInt1 + paramInt3 >> 2;
        final int m = paramInt2 + paramInt3 >> 2;

        final int n = k - i + 1;
        final int i1 = m - j + 1;
        final BiomeGenBase[] arrayOfInt = getBiomesForGeneration(null, i, j, n, i1);
        for (int i2 = 0; i2 < n * i1; i2++) {
            final BiomeGenBase localBiomeBase = arrayOfInt[i2];
            if (!paramList.contains(localBiomeBase)) return false;
        }

        return true;
    }

    @Override
    public void cleanupCache() {
        synchronized (lockObject) {
            cache.cleanupCache();
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ChunkPosition findBiomePosition(final int paramInt1, final int paramInt2,
            final int paramInt3, final List paramList, final Random paramRandom)
    {
        final int i = paramInt1 - paramInt3 >> 2;
        final int j = paramInt2 - paramInt3 >> 2;
        final int k = paramInt1 + paramInt3 >> 2;
        final int m = paramInt2 + paramInt3 >> 2;

        final int n = k - i + 1;
        final int i1 = m - j + 1;
        final int[] arrayOfInt = unZoomedLayer.Calculate(i, j, n, i1);
        ChunkPosition localChunkPosition = null;
        int i2 = 0;
        for (int i3 = 0; i3 < arrayOfInt.length; i3++) {
            if (arrayOfInt[i3] >= DefaultBiome.values().length) continue;
            final int i4 = i + i3 % n << 2;
            final int i5 = j + i3 / n << 2;
            final BiomeGenBase localBiomeBase = BiomeGenBase.biomeList[arrayOfInt[i3]];
            if (!paramList.contains(localBiomeBase) || localChunkPosition != null
                    && paramRandom.nextInt(i2 + 1) != 0) continue;
            localChunkPosition = new ChunkPosition(i4, 0, i5);
            i2++;
        }

        return localChunkPosition;
    }

    @Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] paramArrayOfBiomeBase, final int paramInt1,
            final int paramInt2, final int paramInt3, final int paramInt4,
            final boolean paramBoolean)
    {
        if (paramArrayOfBiomeBase == null || paramArrayOfBiomeBase.length < paramInt3 * paramInt4)
            paramArrayOfBiomeBase = new BiomeGenBase[paramInt3 * paramInt4];

        if (paramBoolean && paramInt3 == 16 && paramInt4 == 16 && (paramInt1 & 0xF) == 0
                && (paramInt2 & 0xF) == 0)
        {
            synchronized (lockObject) {
                final BiomeGenBase[] localObject = cache.getCachedBiomes(paramInt1, paramInt2);
                System.arraycopy(localObject, 0, paramArrayOfBiomeBase, 0, paramInt3 * paramInt4);
            }
            return paramArrayOfBiomeBase;
        }
        final int[] localObject = biomeLayer.Calculate(paramInt1, paramInt2, paramInt3, paramInt4);
        for (int i = 0; i < paramInt3 * paramInt4; i++)
            paramArrayOfBiomeBase[i] = BiomeGenBase.biomeList[localObject[i]];

        return paramArrayOfBiomeBase;
    }

    // get biome
    @Override
    public BiomeGenBase getBiomeGenAt(final int paramInt1, final int paramInt2) {
        synchronized (lockObject) {
            return cache.getBiomeGenAt(paramInt1, paramInt2);
        }
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] paramArrayOfBiomeBase,
            final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
    {
        if (paramArrayOfBiomeBase == null || paramArrayOfBiomeBase.length < paramInt3 * paramInt4)
            paramArrayOfBiomeBase = new BiomeGenBase[paramInt3 * paramInt4];

        final int[] arrayOfInt = unZoomedLayer
                .Calculate(paramInt1, paramInt2, paramInt3, paramInt4);
        for (int i = 0; i < paramInt3 * paramInt4; i++)
            paramArrayOfBiomeBase[i] = BiomeGenBase.biomeList[arrayOfInt[i]];

        return paramArrayOfBiomeBase;
    }

    @Override
    public int[] getBiomesTC(int[] biomeArray, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (biomeArray == null || biomeArray.length < x_size * z_size)
            biomeArray = new int[x_size * z_size];

        if (x_size == 16 && z_size == 16 && (x & 0xF) == 0 && (z & 0xF) == 0) {
            synchronized (lockObject) {
                final BiomeGenBase[] localObject = cache.getCachedBiomes(x, z);
                for (int i = 0; i < x_size * z_size; i++)
                    biomeArray[i] = localObject[i].biomeID;
            }

            return biomeArray;
        }

        final int[] arrayOfInt = biomeLayer.Calculate(x, z, x_size, z_size);

        System.arraycopy(arrayOfInt, 0, biomeArray, 0, x_size * z_size);

        return biomeArray;

    }

    @Override
    public int[] getBiomesUnZoomedTC(int[] biomeArray, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (biomeArray == null || biomeArray.length < x_size * z_size)
            biomeArray = new int[x_size * z_size];

        final int[] arrayOfInt = unZoomedLayer.Calculate(x, z, x_size, z_size);

        System.arraycopy(arrayOfInt, 0, biomeArray, 0, x_size * z_size);

        return biomeArray;
    }

    @Override
    public int getBiomeTC(final int x, final int z) {
        return this.getBiomeGenAt(x, z).biomeID;
    }

    // rain
    @Override
    public float[] getRainfall(float[] paramArrayOfFloat, final int paramInt1, final int paramInt2,
            final int paramInt3, final int paramInt4)
    {
        if (paramArrayOfFloat == null || paramArrayOfFloat.length < paramInt3 * paramInt4)
            paramArrayOfFloat = new float[paramInt3 * paramInt4];

        final int[] arrayOfInt = biomeLayer.Calculate(paramInt1, paramInt2, paramInt3, paramInt4);
        for (int i = 0; i < paramInt3 * paramInt4; i++) {
            float f1 = worldConfig.biomeConfigs[arrayOfInt[i]].getWetness() / 65536.0F;
            if (f1 < worldConfig.minMoisture) f1 = worldConfig.minMoisture;
            if (f1 > worldConfig.maxMoisture) f1 = worldConfig.maxMoisture;
            paramArrayOfFloat[i] = f1;
        }

        return paramArrayOfFloat;
    }

    // Temperature
    @Override
    public float[] getTemperatures(float[] paramArrayOfFloat, final int paramInt1,
            final int paramInt2, final int paramInt3, final int paramInt4)
    {
        if (paramArrayOfFloat == null || paramArrayOfFloat.length < paramInt3 * paramInt4)
            paramArrayOfFloat = new float[paramInt3 * paramInt4];

        final int[] arrayOfInt = biomeLayer.Calculate(paramInt1, paramInt2, paramInt3, paramInt4);
        for (int i = 0; i < paramInt3 * paramInt4; i++) {
            float f1 = worldConfig.biomeConfigs[arrayOfInt[i]].getTemperature() / 65536.0F;
            if (f1 < worldConfig.minTemperature) f1 = worldConfig.minTemperature;
            if (f1 > worldConfig.maxTemperature) f1 = worldConfig.maxTemperature;
            paramArrayOfFloat[i] = f1;
        }

        return paramArrayOfFloat;
    }

    @Override
    public float[] getTemperaturesTC(final int x, final int z, final int x_size, final int z_size) {
        return getTemperatures(Tbuffer, x, z, x_size, z_size);
    }

    public void Init(final SingleWorld world) {
        worldConfig = world.getSettings();
        localWorld = world;

        synchronized (lockObject) {
            cache = new BiomeCache(this);
        }

        final Layer[] layers = Layer.Init(world.getSeed(), world);

        unZoomedLayer = layers[0];
        biomeLayer = layers[1];

    }
}