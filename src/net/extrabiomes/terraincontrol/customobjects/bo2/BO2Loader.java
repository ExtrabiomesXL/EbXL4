package net.extrabiomes.terraincontrol.customobjects.bo2;

import java.io.File;

import net.extrabiomes.terraincontrol.customobjects.CustomObject;
import net.extrabiomes.terraincontrol.customobjects.CustomObjectLoader;

public class BO2Loader implements CustomObjectLoader
{
    public CustomObject loadFromFile(String objectName, File file)
    {
        return new BO2(file, objectName);
    }

    @Override
    public void onShutdown()
    {
        // Stub method
    }
}
