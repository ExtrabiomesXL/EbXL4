package ebxl4.lib;

import net.minecraft.world.WorldType;
import ebxl4.exceptions.NoFreeWorldIDsException;

public class utils {
  public static int getFreeWorldID() throws NoFreeWorldIDsException {
    for(int i = 3; i < WorldType.worldTypes.length; i++) {
      if(WorldType.worldTypes[i] == null) {
        return i;
      }
    }

    // There were no free world id types
    LogHelper.severe("There are no free world types.");
    throw new NoFreeWorldIDsException();

  }
}
