
package net.extrabiomes.terraincontrol.structuregens;

import net.extrabiomes.terraincontrol.util.WorldHelper;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureMineshaftStart;
import net.minecraft.world.gen.structure.StructureStart;

import com.khorn.terraincontrol.LocalWorld;

public class MineshaftGen extends MapGenStructure
{
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        if (rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ))) {
            final LocalWorld world = WorldHelper.toLocalWorld(worldObj);
            final int biomeId = world.getCalculatedBiomeId(chunkX * 16 + 8, chunkZ * 16 + 8);
            if (rand.nextDouble() * 100.0 < world.getSettings().biomeConfigs[biomeId].mineshaftsRarity)
                return true;
        }

        return false;
    }

    @Override
    protected StructureStart getStructureStart(final int par1, final int par2) {
        return new StructureMineshaftStart(worldObj, rand, par1, par2);
    }
}
