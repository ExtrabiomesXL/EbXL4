package net.extrabiomes.terraincontrol.generator.resourcegens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.TerrainControl;
import net.extrabiomes.terraincontrol.customobjects.CustomObject;
import net.extrabiomes.terraincontrol.exception.InvalidResourceException;
import net.extrabiomes.terraincontrol.util.Txt;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class CustomObjectGen extends Resource
{
    private List<CustomObject> objects;
    private List<String> objectNames;

    @Override
    public void load(List<String> args) throws InvalidResourceException
    {
        if (args.size() == 0 || (args.size() == 1 && args.get(0).trim().equals("")))
        {
            // Backwards compability
            args.set(0, "UseWorld");
        }
        objects = new ArrayList<CustomObject>();
        objectNames = new ArrayList<String>();
        for (String arg : args)
        {
            CustomObject object = TerrainControl.getCustomObjectManager().getObjectFromString(arg, getHolder());
            if (object == null || !object.canSpawnAsObject())
            {
                throw new InvalidResourceException("No custom object found with the name " + arg);
            }
            objects.add(object);
            objectNames.add(arg);
        }
    }

    @Override
    public void spawn(LocalWorld world, Random random, int x, int z)
    {
        // Left blank, as process(..) already handles this.
    }

    @Override
    public void process(LocalWorld world, Random random, int chunkX, int chunkZ)
    {
        if (TerrainGen.decorate(world.getMCWorld(), random, chunkX, chunkZ, EventType.CUSTOM))
            for (CustomObject object : objects)
            {
                object.process(world, random, chunkX, chunkZ);
            }
    }

    @Override
    public String makeString()
    {
        return "CustomObject(" + Txt.implode(objectNames, ",") + ")";
    }

}
