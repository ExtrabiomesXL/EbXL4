package net.extrabiomes.terraincontrol.biomelayers.layers;


import net.extrabiomes.terraincontrol.DefaultBiome;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.biomelayers.ArraysCache;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;

public class LayerMix extends Layer
{
    public LayerMix(long paramLong, Layer paramGenLayer, WorldConfig config, LocalWorld world)
    {
        super(paramLong);
        this.child = paramGenLayer;
        this.worldConfig = config;
        this.RiverBiomes = new int[world.getMaxBiomesCount()];

        for (int id = 0; id < this.RiverBiomes.length; id++)
        {
            BiomeConfig biomeConfig = config.biomeConfigs[id];

            if (biomeConfig == null || biomeConfig.RiverBiome.isEmpty())
                this.RiverBiomes[id] = -1;
            else
                this.RiverBiomes[id] = world.getBiomeIdByName(biomeConfig.RiverBiome);

        }
    }

    private WorldConfig worldConfig;
    private int[] RiverBiomes;

    @Override
    public int[] GetBiomes(int cacheId, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {

        int[] arrayOfInt1 = this.child.GetBiomes(cacheId, paramInt1, paramInt2, paramInt3, paramInt4);

        int[] arrayOfInt2 = ArraysCache.GetArray(cacheId, paramInt3 * paramInt4);

        int currentPiece;
        int cachedId;
        for (int i = 0; i < paramInt4; i++)
        {
            for (int j = 0; j < paramInt3; j++)
            {
                currentPiece = arrayOfInt1[(j + i * paramInt3)];

                if ((currentPiece & LandBit) != 0)
                    cachedId = currentPiece & BiomeBits;
                else if (this.worldConfig.FrozenOcean && (currentPiece & IceBit) != 0)
                    cachedId = DefaultBiome.FROZEN_OCEAN.Id;
                else
                    cachedId = DefaultBiome.OCEAN.Id;

                if (this.worldConfig.biomeConfigs[cachedId] != null && this.worldConfig.RiversEnabled && (currentPiece & RiverBits) != 0 && !this.worldConfig.biomeConfigs[cachedId].RiverBiome.isEmpty())
                    currentPiece = this.RiverBiomes[cachedId];
                else
                    currentPiece = cachedId;

                arrayOfInt2[(j + i * paramInt3)] = currentPiece;
            }
        }

        return arrayOfInt2;
    }
}
