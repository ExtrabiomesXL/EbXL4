package net.extrabiomes.terraincontrol.biomegenerators;

import net.extrabiomes.terraincontrol.LocalWorld;

/**
 * Extends the NormalBiomeMode. The code that makes it generate differently can be found inside the layer code.
 *
 */
public class FromImageBiomeGenerator extends NormalBiomeGenerator
{
    public FromImageBiomeGenerator(LocalWorld world, BiomeCache cache)
    {
        super(world, cache);
    }
}
