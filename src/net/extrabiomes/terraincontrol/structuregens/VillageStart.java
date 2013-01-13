
package net.extrabiomes.terraincontrol.structuregens;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentVillageRoadPiece;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

class StructureVillageStart extends StructureStart
{
    /**
     * well ... thats what it does
     */
    private boolean hasMoreThanTwoComponents = false;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public StructureVillageStart(final World world, final Random random, final int chunkX,
            final int chunkZ, final int size)
    {
        final ArrayList<StructureComponent> villagePieces = StructureVillagePieces
                .getStructureVillageWeightedPieceList(random, size);
        final VillageStartPiece startPiece = new VillageStartPiece(world, 0, random,
                (chunkX << 4) + 2, (chunkZ << 4) + 2, villagePieces, size);
        components.add(startPiece);
        startPiece.buildComponent(startPiece, components, random);
        final ArrayList var8 = startPiece.field_74930_j;
        final ArrayList var9 = startPiece.field_74932_i;
        int var10;

        while (!var8.isEmpty() || !var9.isEmpty()) {
            StructureComponent var11;

            if (var8.isEmpty()) {
                var10 = random.nextInt(var9.size());
                var11 = (StructureComponent) var9.remove(var10);
                var11.buildComponent(startPiece, components, random);
            } else {
                var10 = random.nextInt(var8.size());
                var11 = (StructureComponent) var8.remove(var10);
                var11.buildComponent(startPiece, components, random);
            }
        }

        updateBoundingBox();
        var10 = 0;

        for (final Object component : components) {
            final StructureComponent var12 = (StructureComponent) component;

            if (!(var12 instanceof ComponentVillageRoadPiece)) ++var10;
        }

        hasMoreThanTwoComponents = var10 > 2;
    }

    /**
     * currently only defined for Villages, returns true if Village has
     * more than 2 non-road components
     */
    @Override
    public boolean isSizeableStructure() {
        return hasMoreThanTwoComponents;
    }
}
