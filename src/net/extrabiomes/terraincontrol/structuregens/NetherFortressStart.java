
package net.extrabiomes.terraincontrol.structuregens;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentNetherBridgeStartPiece;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class NetherFortressStart extends StructureStart
{
    public NetherFortressStart(final World world, final Random random, final int chunkX,
            final int chunkZ)
    {
        final ComponentNetherBridgeStartPiece var5 = new ComponentNetherBridgeStartPiece(random,
                (chunkX << 4) + 2, (chunkZ << 4) + 2);
        components.add(var5);
        var5.buildComponent(var5, components, random);
        final ArrayList list = var5.field_74967_d;

        while (!list.isEmpty()) {
            final int var7 = random.nextInt(list.size());
            final StructureComponent var8 = (StructureComponent) list.remove(var7);
            var8.buildComponent(var5, components, random);
        }

        updateBoundingBox();
        setRandomHeight(world, random, 48, 70);
    }
}
