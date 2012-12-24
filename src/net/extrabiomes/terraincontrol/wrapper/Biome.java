package net.extrabiomes.terraincontrol.wrapper;

import net.extrabiomes.terraincontrol.LocalBiome;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;

public class Biome implements LocalBiome
{
    private BiomeGenCustom biomeBase;

    public Biome(BiomeGenCustom biome)
    {
        this.biomeBase = biome;
    }

    public boolean isCustom()
    {
        return true;
    }

    public int getCustomId()
    {
        return getId();
    }

    public void setVisuals(BiomeConfig config)
    {
        biomeBase.setVisuals(config);
    }

    public String getName()
    {
        return biomeBase.biomeName;
    }

    public int getId()
    {
        return biomeBase.biomeID;
    }

    public float getTemperature()
    {
        return biomeBase.temperature;
    }

    public float getWetness()
    {
        return biomeBase.rainfall;
    }

    public float getSurfaceHeight()
    {
        return biomeBase.minHeight;
    }

    public float getSurfaceVolatility()
    {
        return biomeBase.maxHeight;
    }

    public byte getSurfaceBlock()
    {
        return biomeBase.topBlock;
    }

    public byte getGroundBlock()
    {
        return biomeBase.fillerBlock;
    }
}
