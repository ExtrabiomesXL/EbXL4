
package net.extrabiomes.terraincontrol.structuregens;

import java.util.ArrayList;
import java.util.List;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.util.WorldHelper;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;


public class NetherFortressGen extends MapGenStructure
{
    public List<SpawnListEntry> spawnList = new ArrayList<SpawnListEntry>();

    public NetherFortressGen() {
        spawnList.add(new SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        spawnList.add(new SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        spawnList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        spawnList.add(new SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }

    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        final int var3 = chunkX >> 4;
        final int var4 = chunkZ >> 4;
        rand.setSeed(var3 ^ var4 << 4 ^ worldObj.getSeed());
        rand.nextInt();

        if (rand.nextInt(3) != 0)
            return false;
        else if (chunkX != (var3 << 4) + 4 + rand.nextInt(8))
            return false;
        else {
            final LocalWorld world = WorldHelper.toLocalWorld(worldObj);
            final int biomeId = world.getCalculatedBiomeId(chunkX * 16 + 8, chunkZ * 16 + 8);
            if (!world.getSettings().biomeConfigs[biomeId].netherFortressesEnabled) return false;
            return chunkZ == (var4 << 4) + 4 + rand.nextInt(8);
        }
    }

    public List getSpawnList() {
        return spawnList;
    }

    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new NetherFortressStart(worldObj, rand, chunkX, chunkZ);
    }
}
