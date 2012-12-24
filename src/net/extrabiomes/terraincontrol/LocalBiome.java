package net.extrabiomes.terraincontrol;

import net.extrabiomes.terraincontrol.configuration.BiomeConfig;

public interface LocalBiome
{
    public abstract boolean isCustom();

    public abstract void setVisuals(BiomeConfig config);

    public abstract String getName();

    public abstract int getId();
    public abstract int getCustomId();

    public abstract float getTemperature();

    public abstract float getWetness();

    public abstract float getSurfaceHeight();
    public abstract float getSurfaceVolatility();

    public abstract byte getSurfaceBlock();
    public abstract byte getGroundBlock();
}