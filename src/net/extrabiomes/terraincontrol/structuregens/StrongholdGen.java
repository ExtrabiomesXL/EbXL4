
package net.extrabiomes.terraincontrol.structuregens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.Biome;
import net.extrabiomes.terraincontrol.configuration.BiomeConfig;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.ComponentStrongholdStairs2;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;


public class StrongholdGen extends MapGenStructure
{
    private final List<BiomeGenBase>  allowedBiomeGenBases;

    private boolean                   ranBiomeCheck;
    private final ChunkCoordIntPair[] structureCoords;
    private final double              distance;
    private int                       spread;

    public StrongholdGen(final WorldConfig worldConfig) {
        distance = worldConfig.strongholdDistance;
        structureCoords = new ChunkCoordIntPair[worldConfig.strongholdCount];
        spread = worldConfig.strongholdSpread;

        allowedBiomeGenBases = new ArrayList<BiomeGenBase>();

        for (final BiomeConfig biomeConfig : worldConfig.biomeConfigs) {
            if (biomeConfig == null) continue;
            if (biomeConfig.strongholdsEnabled)
                allowedBiomeGenBases.add(((Biome) biomeConfig.Biome).getHandle());
        }
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected boolean canSpawnStructureAtCoords(final int par1, final int par2) {
        if (!ranBiomeCheck) {
            final Random random = new Random();
            random.setSeed(worldObj.getSeed());
            double randomNumBetween0and2PI = random.nextDouble() * Math.PI * 2.0D;
            int var6 = 1;

            for (int i = 0; i < structureCoords.length; ++i) {
                final double var8 = (1.25D * var6 + random.nextDouble()) * distance * var6;
                int var10 = (int) Math.round(Math.cos(randomNumBetween0and2PI) * var8);
                int var11 = (int) Math.round(Math.sin(randomNumBetween0and2PI) * var8);
                final ArrayList var12 = new ArrayList();
                Collections.addAll(var12, allowedBiomeGenBases);
                final ChunkPosition var13 = worldObj.getWorldChunkManager().findBiomePosition(
                        (var10 << 4) + 8, (var11 << 4) + 8, 112, var12, random);

                if (var13 != null) {
                    var10 = var13.x >> 4;
                    var11 = var13.z >> 4;
                }

                structureCoords[i] = new ChunkCoordIntPair(var10, var11);
                randomNumBetween0and2PI += Math.PI * 2D * var6 / spread;

                if (i == spread) {
                    var6 += 2 + random.nextInt(5);
                    spread += 1 + random.nextInt(2);
                }
            }

            ranBiomeCheck = true;
        }

        final ChunkCoordIntPair[] structureCoords = this.structureCoords;

        for (final ChunkCoordIntPair structureCoord : structureCoords)
            if (par1 == structureCoord.chunkXPos && par2 == structureCoord.chunkZPos) return true;

        return false;
    }

    /**
     * Returns a list of other locations at which the structure
     * generation has been run, or null if not relevant to this
     * structure generator.
     */
    @Override
    protected List<ChunkPosition> getCoordList() {
        final ArrayList<ChunkPosition> chunkPositions = new ArrayList<ChunkPosition>();

        for (final ChunkCoordIntPair structureCoord : structureCoords)
            if (structureCoord != null) chunkPositions.add(structureCoord.getChunkPosition(64));

        return chunkPositions;
    }

    @Override
    protected StructureStart getStructureStart(final int par1, final int par2) {
        StrongholdStart start = new StrongholdStart(worldObj, rand, par1, par2);

        while (start.getComponents().isEmpty()
                || ((ComponentStrongholdStairs2) start.getComponents().get(0)).strongholdPortalRoom == null)
            start = new StrongholdStart(worldObj, rand, par1, par2);

        return start;
    }
}
