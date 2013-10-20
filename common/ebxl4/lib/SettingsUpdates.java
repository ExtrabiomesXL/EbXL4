package ebxl4.lib;

import java.util.LinkedList;
import java.util.List;

import ebxl4.lib.settingsupdates.TestSampleUpdate;
import ebxl4.lib.settingsupdates.UpdateVersionNumber;
import net.minecraftforge.common.Configuration;

public abstract class SettingsUpdates {
  public static List<SettingsUpdates> updates = new LinkedList<SettingsUpdates>();
    
  public abstract Version getVersion(); 
  public abstract void Update(Configuration configuration);
  
  public static void init() {
    // Add any updates here, they must extend this class.
    updates.add(new TestSampleUpdate());
    
    // This is always the last update as it updates the version number
    updates.add(new UpdateVersionNumber());
  }
}
