package ebxl4.lib.settingsupdates;

import net.minecraftforge.common.Configuration;
import ebxl4.lib.LogHelper;
import ebxl4.lib.SettingsUpdates;
import ebxl4.lib.Version;

public class TestSampleUpdate extends SettingsUpdates {

  private Version ver = new Version("0.0.1.8");
    
  @Override
  public Version getVersion() {
    return ver;
  }

  @Override
  public void Update(Configuration configuration) {
    LogHelper.info("This is the Sample Test Update.");
  }

}
