
package net.extrabiomes.terraincontrol;

import net.extrabiomes.terraincontrol.LocalBiome;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.minecraft.world.biome.BiomeGenBase;


public class Biome implements LocalBiome
{
    private final BiomeGenCustom biomeBase;

    public Biome(final BiomeGenCustom biome) {
        biomeBase = biome;
    }

    @Override
    public int getCustomId() {
        return getId();
    }

    @Override
    public byte getGroundBlock() {
        return biomeBase.fillerBlock;
    }

    public BiomeGenBase getHandle() {
        return biomeBase;
    }

    @Override
    public int getId() {
        return biomeBase.biomeID;
    }

    @Override
    public String getName() {
        return biomeBase.biomeName;
    }

    @Override
    public byte getSurfaceBlock() {
        return biomeBase.topBlock;
    }

    @Override
    public float getSurfaceHeight() {
        return biomeBase.minHeight;
    }

    @Override
    public float getSurfaceVolatility() {
        return biomeBase.maxHeight;
    }

    @Override
    public float getTemperature() {
        return biomeBase.temperature;
    }

    @Override
    public float getWetness() {
        return biomeBase.rainfall;
    }

    @Override
    public boolean isCustom() {
        return true;
    }

    @Override
    public void setEffects(final BiomeConfig config) {
        biomeBase.setEffects(config);
    }
}
