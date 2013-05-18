package net.extrabiomes.terraincontrol.customobjects.bo3;

import java.util.List;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.exception.InvalidConfigException;


public class LightCheck extends BO3Check
{
    // The minimum and maximum light levels, inclusive
    public int minLightLevel;
    public int maxLightLevel;

    @Override
    public boolean preventsSpawn(LocalWorld world, int x, int y, int z)
    {
        int lightLevel = world.getLightLevel(x, y, z);
        if (lightLevel < minLightLevel || lightLevel > maxLightLevel)
        {
            // Out of bounds
            return true;
        }

        return false;
    }

    @Override
    public void load(List<String> args) throws InvalidConfigException
    {
        assureSize(5, args);
        x = readInt(args.get(0), -100, 100);
        y = readInt(args.get(1), -100, 100);
        z = readInt(args.get(2), -100, 100);
        minLightLevel = readInt(args.get(3), 0, 16);
        maxLightLevel = readInt(args.get(4), minLightLevel, 16);
    }

    @Override
    public String makeString()
    {
        return "LightCheck(" + x + "," + y + "," + z + "," + minLightLevel + "," + maxLightLevel + ")";
    }

    @Override
    public BO3Check rotate()
    {
        LightCheck rotatedCheck = new LightCheck();
        rotatedCheck.x = z;
        rotatedCheck.y = y;
        rotatedCheck.z = -x;
        rotatedCheck.minLightLevel = minLightLevel;
        rotatedCheck.maxLightLevel = maxLightLevel;

        return rotatedCheck;
    }

}
