
package net.extrabiomes.terraincontrol.structuregens;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.TerrainControl;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig.VillageType;
import net.extrabiomes.terraincontrol.util.WorldHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.ComponentVillageStartPiece;


public class VillageStartPiece extends ComponentVillageStartPiece
{
    public final WorldChunkManager worldChunkManager;

    @SuppressWarnings("rawtypes")
    public VillageStartPiece(final World world, final int par2, final Random par3Random,
            final int par4, final int par5, final ArrayList par6ArrayList, final int size)
    {
        super(world.getWorldChunkManager(), par2, par3Random, par4, par5, par6ArrayList, size);
        worldChunkManager = world.getWorldChunkManager();

        // Whether the village is a sandstone village
        final BiomeGenBase currentBiomeGenBase = worldChunkManager.getBiomeGenAt(par4, par5);
        final LocalWorld worldTC = WorldHelper.toLocalWorld(world);
        final BiomeConfig config = worldTC.getSettings().biomeConfigs[currentBiomeGenBase.biomeID];
        setSandstoneVillage(config.villageType == VillageType.sandstone);

        startPiece = this;
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return worldChunkManager;
    }

    /**
     * Just sets the first boolean it can find in the
     * WorldGenVillageStartPiece.class to sandstoneVillage.
     * 
     * @param sandstoneVillage
     *            Whether the village should be a sandstone village.
     */
    private void setSandstoneVillage(final boolean sandstoneVillage) {
        for (final Field field : ComponentVillageStartPiece.class.getFields())
            if (field.getType().toString().equals("boolean")) try {
                field.setAccessible(true);
                field.setBoolean(this, sandstoneVillage);
                break;
            } catch (final Exception e) {
                TerrainControl.log("Cannot make village a sandstone village!");
                e.printStackTrace();
            }
    }
}
