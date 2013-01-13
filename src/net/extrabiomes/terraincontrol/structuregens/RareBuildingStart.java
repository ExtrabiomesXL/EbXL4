
package net.extrabiomes.terraincontrol.structuregens;

import java.util.Random;

import net.extrabiomes.terraincontrol.util.WorldHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeatureDesertPyramid;
import net.minecraft.world.gen.structure.ComponentScatteredFeatureJunglePyramid;
import net.minecraft.world.gen.structure.ComponentScatteredFeatureSwampHut;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.configuration.BiomeConfig;

public class RareBuildingStart extends StructureStart
{
    public RareBuildingStart(final World world, final Random random, final int chunkX,
            final int chunkZ)
    {
        final LocalWorld localWorld = WorldHelper.toLocalWorld(world);
        final BiomeConfig biomeConfig = localWorld.getSettings().biomeConfigs[localWorld
                .getCalculatedBiomeId(chunkX * 16 + 8, chunkZ * 16 + 8)];
        StructureComponent building;
        switch (biomeConfig.rareBuildingType)
        {
            case desertPyramid:
                building = new ComponentScatteredFeatureDesertPyramid(random, chunkX * 16,
                        chunkZ * 16);
                break;
            case jungleTemple:
                building = new ComponentScatteredFeatureJunglePyramid(random, chunkX * 16,
                        chunkZ * 16);
                break;
            case swampHut:
                building = new ComponentScatteredFeatureSwampHut(random, chunkX * 16, chunkZ * 16);
                break;
            case disabled:
            default:
                // Should never happen, but on biome borders there is
                // chance that a
                // structure gets started in a biome where it shouldn't.
                building = null;
                break;
        }

        if (building != null) components.add(building);

        updateBoundingBox();
    }
}
