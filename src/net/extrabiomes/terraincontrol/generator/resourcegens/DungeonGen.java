package net.extrabiomes.terraincontrol.generator.resourcegens;

import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.TerrainControl;
import net.extrabiomes.terraincontrol.exception.InvalidConfigException;


public class DungeonGen extends Resource
{
    private int minAltitude;
    private int maxAltitude;

    @Override
    public void load(List<String> args) throws InvalidConfigException
    {
        assureSize(4, args);

        frequency = readInt(args.get(0), 1, 100);
        rarity = readRarity(args.get(1));
        minAltitude = readInt(args.get(2), TerrainControl.worldDepth, TerrainControl.worldHeight);
        maxAltitude = readInt(args.get(3), minAltitude + 1, TerrainControl.worldHeight);
    }

    @Override
    public void spawn(LocalWorld world, Random random, boolean villageInChunk, int x, int z)
    {
        int y = random.nextInt(maxAltitude - minAltitude) + minAltitude;
        world.PlaceDungeons(random, x, y, z);
    }

    @Override
    public String makeString()
    {
        return "Dungeon(" + frequency + "," + rarity + "," + minAltitude + "," + maxAltitude + ")";
    }
}