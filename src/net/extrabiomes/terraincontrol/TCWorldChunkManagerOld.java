
package net.extrabiomes.terraincontrol;

import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.IBiomeManager;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.extrabiomes.terraincontrol.util.NoiseGeneratorOctaves2;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;


public class TCWorldChunkManagerOld extends WorldChunkManager implements IBiomeManager
{

    private WorldConfig                  localWrk;

    private final NoiseGeneratorOctaves2 temperatureGenerator;
    private final NoiseGeneratorOctaves2 wetnessGenerator;
    private final NoiseGeneratorOctaves2 temperatureGenerator2;
    public double[]                      oldTemperature;
    public double[]                      oldWetness;
    private double[]                     oldTemperature2;
    private BiomeGenBase[]               tempBiomeBases;
    private final BiomeCache             cache        = new BiomeCache(this);

    private static BiomeGenBase[]        biomeDiagram = new BiomeGenBase[4096];

    static {
        for (int i = 0; i < 64; i++)
            for (int j = 0; j < 64; j++)
                biomeDiagram[i + j * 64] = getBiomeDiagram(i / 63.0F, j / 63.0F);
    }

    private static BiomeGenBase getBiomeDiagram(final double paramFloat1, double paramFloat2) {

        paramFloat2 *= paramFloat1;
        if (paramFloat1 < 0.1F) return BiomeGenBase.plains;
        if (paramFloat2 < 0.2F) {
            if (paramFloat1 < 0.5F) return BiomeGenBase.plains;
            if (paramFloat1 < 0.95F) return BiomeGenBase.plains;
            return BiomeGenBase.desert;
        }
        if (paramFloat2 > 0.5F && paramFloat1 < 0.7F) return BiomeGenBase.swampland;
        if (paramFloat1 < 0.5F) return BiomeGenBase.taiga;
        if (paramFloat1 < 0.97F) {
            if (paramFloat2 < 0.35F) return BiomeGenBase.taiga;
            return BiomeGenBase.forest;
        }

        if (paramFloat2 < 0.45F) return BiomeGenBase.forest;
        if (paramFloat2 < 0.9F) return BiomeGenBase.forest;
        return BiomeGenBase.forest;
    }

    private static BiomeGenBase getBiomeFromDiagram(final double temp, final double rain) {
        final int i = (int) (temp * 63.0D);
        final int j = (int) (rain * 63.0D);
        return biomeDiagram[i + j * 64];
    }

    private final float[] Tbuffer = new float[256];

    public TCWorldChunkManagerOld(final LocalWorld world) {

        localWrk = world.getSettings();
        temperatureGenerator = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 9871L), 4);
        wetnessGenerator = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 39811L), 4);
        temperatureGenerator2 = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 543321L), 2);

    }

    // Check biomes list
    @Override
    @SuppressWarnings("rawtypes")
    public boolean areBiomesViable(final int paramInt1, final int paramInt2, final int paramInt3,
            final List paramList)
    {
        final int i = paramInt1 - paramInt3 >> 2;
        final int j = paramInt2 - paramInt3 >> 2;
        final int k = paramInt1 + paramInt3 >> 2;
        final int m = paramInt2 + paramInt3 >> 2;

        final int n = k - i + 1;
        final int i1 = m - j + 1;

        BiomeGenBase[] biomeArray = null;

        biomeArray = getBiomesForGeneration(biomeArray, i, j, n, i1);
        for (int i2 = 0; i2 < n * i1; i2++)
            if (!paramList.contains(biomeArray[i2])) return false;

        return true;
    }

    // Not use IniCache
    @Override
    public void cleanupCache() {
        cache.cleanupCache();
    }

    // StrongholdPosition
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
        BiomeGenBase[] biomeArray = null;

        biomeArray = getBiomesForGeneration(biomeArray, i, j, n, i1);
        ChunkPosition localChunkPosition = null;
        int i2 = 0;
        for (int i3 = 0; i3 < biomeArray.length; i3++) {
            final int i4 = i + i3 % n << 2;
            final int i5 = j + i3 / n << 2;
            if (!paramList.contains(biomeArray[i2]) || localChunkPosition != null
                    && paramRandom.nextInt(i2 + 1) != 0) continue;
            localChunkPosition = new ChunkPosition(i4, 0, i5);
            i2++;
        }

        return localChunkPosition;
    }

    @Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] paramArrayOfzp, final int x, final int z,
            final int x_size, final int z_size, final boolean useCache)
    {
        if (paramArrayOfzp == null || paramArrayOfzp.length < x_size * z_size)
            paramArrayOfzp = new BiomeGenBase[x_size * z_size];
        if (useCache && x_size == 16 && z_size == 16 && (x & 0xF) == 0 && (z & 0xF) == 0) {
            final BiomeGenBase[] localObject = cache.getCachedBiomes(x, z);
            System.arraycopy(localObject, 0, paramArrayOfzp, 0, x_size * z_size);
            return paramArrayOfzp;
        }

        oldTemperature = temperatureGenerator.a(oldTemperature, x, z, x_size, x_size,
                0.025000000372529D / localWrk.oldBiomeSize,
                0.025000000372529D / localWrk.oldBiomeSize, 0.25D);
        oldWetness = wetnessGenerator.a(oldWetness, x, z, x_size, x_size,
                0.0500000007450581D / localWrk.oldBiomeSize,
                0.0500000007450581D / localWrk.oldBiomeSize, 0.3333333333333333D);
        oldTemperature2 = temperatureGenerator2.a(oldTemperature2, x, z, x_size, x_size,
                0.25D / localWrk.oldBiomeSize, 0.25D / localWrk.oldBiomeSize, 0.5882352941176471D);

        int i = 0;
        for (int j = 0; j < x_size; j++)
            for (int k = 0; k < z_size; k++) {
                final double d1 = oldTemperature2[i] * 1.1D + 0.5D;

                double d2 = 0.01D;
                double d3 = 1.0D - d2;
                double d4 = (oldTemperature[i] * 0.15D + 0.7D) * d3 + d1 * d2;
                d2 = 0.002D;
                d3 = 1.0D - d2;
                double d5 = (oldWetness[i] * 0.15D + 0.5D) * d3 + d1 * d2;
                d4 = 1.0D - (1.0D - d4) * (1.0D - d4);

                if (d4 < localWrk.minTemperature) d4 = localWrk.minTemperature;
                if (d5 < localWrk.minMoisture) d5 = localWrk.minMoisture;
                if (d4 > localWrk.maxTemperature) d4 = localWrk.maxTemperature;
                if (d5 > localWrk.maxMoisture) d5 = localWrk.maxMoisture;
                oldTemperature[i] = d4;
                oldWetness[i] = d5;

                paramArrayOfzp[i++] = TCWorldChunkManagerOld.getBiomeFromDiagram(d4, d5);
            }

        if (localWrk.isDeprecated) localWrk = localWrk.newSettings;

        return paramArrayOfzp;
    }

    @Override
    public BiomeGenBase getBiomeGenAt(final int i, final int i1) {
        return cache.getBiomeGenAt(i, i1);
    }

    @Override
    public int[] getBiomesTC(final int[] biomeArray, final int x, final int z, final int x_size,
            final int z_size)
    {
        return getBiomesUnZoomedTC(biomeArray, x, z, x_size, z_size);
    }

    @Override
    public int[] getBiomesUnZoomedTC(int[] biomeArray, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (biomeArray == null || biomeArray.length < x_size * z_size)
            biomeArray = new int[x_size * z_size];
        if (x_size == 16 && z_size == 16 && (x & 0xF) == 0 && (z & 0xF) == 0) {
            final BiomeGenBase[] localObject = cache.getCachedBiomes(x, z);
            for (int i = 0; i < x_size * z_size; i++)
                biomeArray[i] = localObject[i].biomeID;
            return biomeArray;
        }

        oldTemperature = temperatureGenerator.a(oldTemperature, x, z, x_size, x_size,
                0.025000000372529D / localWrk.oldBiomeSize,
                0.025000000372529D / localWrk.oldBiomeSize, 0.25D);
        oldWetness = wetnessGenerator.a(oldWetness, x, z, x_size, x_size,
                0.0500000007450581D / localWrk.oldBiomeSize,
                0.0500000007450581D / localWrk.oldBiomeSize, 0.3333333333333333D);
        oldTemperature2 = temperatureGenerator2.a(oldTemperature2, x, z, x_size, x_size,
                0.25D / localWrk.oldBiomeSize, 0.25D / localWrk.oldBiomeSize, 0.5882352941176471D);

        int i = 0;
        for (int j = 0; j < x_size; j++)
            for (int k = 0; k < z_size; k++) {
                final double d1 = oldTemperature2[i] * 1.1D + 0.5D;

                double d2 = 0.01D;
                double d3 = 1.0D - d2;
                double d4 = (oldTemperature[i] * 0.15D + 0.7D) * d3 + d1 * d2;
                d2 = 0.002D;
                d3 = 1.0D - d2;
                double d5 = (oldWetness[i] * 0.15D + 0.5D) * d3 + d1 * d2;
                d4 = 1.0D - (1.0D - d4) * (1.0D - d4);

                if (d4 < localWrk.minTemperature) d4 = localWrk.minTemperature;
                if (d5 < localWrk.minMoisture) d5 = localWrk.minMoisture;
                if (d4 > localWrk.maxTemperature) d4 = localWrk.maxTemperature;
                if (d5 > localWrk.maxMoisture) d5 = localWrk.maxMoisture;
                oldTemperature[i] = d4;
                oldWetness[i] = d5;

                biomeArray[i++] = TCWorldChunkManagerOld.getBiomeFromDiagram(d4, d5).biomeID;
            }

        if (localWrk.isDeprecated) localWrk = localWrk.newSettings;

        return biomeArray;
    }

    @Override
    public int getBiomeTC(final int x, final int z) {
        return this.getBiomeGenAt(x, z).biomeID;
    }

    // Rain
    @Override
    public float[] getRainfall(float[] temp_out, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (temp_out == null || temp_out.length < x_size * z_size)
            temp_out = new float[x_size * z_size];
        tempBiomeBases = this.getBiomeGenAt(tempBiomeBases, x, z, x_size, z_size, false);

        for (int i = 0; i < temp_out.length; i++)
            temp_out[i] = (float) oldWetness[i];

        return temp_out;

    }

    // Temperature
    @Override
    public float[] getTemperatures(float[] temp_out, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (temp_out == null || temp_out.length < x_size * z_size)
            temp_out = new float[x_size * z_size];

        oldTemperature = temperatureGenerator.a(oldTemperature, x, z, x_size, z_size,
                0.025000000372529D / localWrk.oldBiomeSize,
                0.025000000372529D / localWrk.oldBiomeSize, 0.25D);
        oldTemperature2 = temperatureGenerator2.a(oldTemperature2, x, z, x_size, z_size,
                0.25D / localWrk.oldBiomeSize, 0.25D / localWrk.oldBiomeSize, 0.5882352941176471D);

        int i = 0;
        for (int j = 0; j < x_size; j++)
            for (int k = 0; k < z_size; k++) {
                final double d1 = oldTemperature2[i] * 1.1D + 0.5D;

                final double d2 = 0.01D;
                final double d3 = 1.0D - d2;
                double d4 = (temp_out[i] * 0.15D + 0.7D) * d3 + d1 * d2;
                d4 = 1.0D - (1.0D - d4) * (1.0D - d4);

                if (d4 < localWrk.minTemperature) d4 = localWrk.minTemperature;
                if (d4 > localWrk.maxTemperature) d4 = localWrk.maxTemperature;
                temp_out[i] = (float) d4;
                i++;
            }
        if (localWrk.isDeprecated) localWrk = localWrk.newSettings;

        return temp_out;
    }

    @Override
    public float[] getTemperaturesTC(final int x, final int z, final int x_size, final int z_size) {
        return getTemperatures(Tbuffer, x, z, x_size, z_size);
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(final BiomeGenBase[] biomeBases, final int x,
            final int z, final int x_size, final int z_size)
    {
        return this.getBiomeGenAt(biomeBases, x, z, x_size, z_size, false);
    }
}
