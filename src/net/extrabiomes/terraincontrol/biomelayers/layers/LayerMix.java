package net.extrabiomes.terraincontrol.biomelayers.layers;


import net.extrabiomes.terraincontrol.DefaultBiome;
import net.extrabiomes.terraincontrol.biomelayers.ArraysCache;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;

public class LayerMix extends Layer
{
    public LayerMix(long paramLong, Layer paramGenLayer, WorldConfig config)
    {
        super(paramLong);
        this.child = paramGenLayer;
        this.worldConfig = config;
    }

    private WorldConfig worldConfig;

    @Override
    public int[] GetBiomes(int cacheId, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {

        int[] arrayOfInt1 = this.child.GetBiomes(cacheId, paramInt1, paramInt2, paramInt3, paramInt4);

        int[] arrayOfInt2 = ArraysCache.GetArray(cacheId, paramInt3 * paramInt4);
        for (int i = 0; i < paramInt4; i++)
        {
            for (int j = 0; j < paramInt3; j++)
            {
                int currentPiece = arrayOfInt1[(j + i * paramInt3)];
                if ((currentPiece & LandBit) != 0)
                {
                    if (this.worldConfig.RiversEnabled && (currentPiece & RiverBits) != 0 && this.worldConfig.biomeConfigs[currentPiece & BiomeBits].BiomeRivers)
                        if (this.worldConfig.FrozenRivers && (currentPiece & IceBit) != 0)
                            currentPiece = DefaultBiome.FROZEN_RIVER.Id;
                        else
                            currentPiece = DefaultBiome.RIVER.Id;
                    else
                        currentPiece = currentPiece & BiomeBits;

                } else if (this.worldConfig.FrozenOcean && (currentPiece & IceBit) != 0)
                    currentPiece = DefaultBiome.FROZEN_OCEAN.Id;
                else
                    currentPiece = DefaultBiome.OCEAN.Id;
                arrayOfInt2[(j + i * paramInt3)] = currentPiece;
            }
        }

        return arrayOfInt2;
    }
}
