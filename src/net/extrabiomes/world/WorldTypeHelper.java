
package net.extrabiomes.world;

import java.util.NoSuchElementException;

import net.extrabiomes.utility.LogWriter;
import net.minecraft.world.WorldType;

abstract class WorldTypeHelper {

    private static final String NO_MORE_WORLD_TYPES = "No more WorldType indexes available.";

    static int getNextWorldTypeID() {
        for (int i = 0; i < WorldType.worldTypes.length; i++)
            if (WorldType.worldTypes[i] == null) return i;
        LogWriter.severe("CAUSE OF CRASH ===>>>", NO_MORE_WORLD_TYPES);
        throw new NoSuchElementException(NO_MORE_WORLD_TYPES);
    }

}
