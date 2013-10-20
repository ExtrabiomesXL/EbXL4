package ebxl4.lib.settingsupdates;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import ebxl4.lib.GeneralSettings;
import ebxl4.lib.LogHelper;
import ebxl4.lib.ModInfo;
import ebxl4.lib.SettingsUpdates;
import ebxl4.lib.Version;

public class UpdateVersionNumber extends SettingsUpdates {
  private Version ver = new Version(ModInfo.MOD_VERSION);
  
  @Override
  public Version getVersion() {
    return ver;
  }

  @Override
  public void Update(Configuration configuration) {
    Property genVersion = configuration.get(Configuration.CATEGORY_GENERAL, "configuration_version", ModInfo.MOD_VERSION);
    genVersion.comment = "The version of " + ModInfo.MOD_NAME + " that was used to generate this configeration.";
    
    genVersion.set(ver.getVersion());
    GeneralSettings.cfgVersion.setVersion(genVersion.getString());    
  }

}
