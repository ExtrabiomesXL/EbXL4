package ebxl4.lib;

import java.io.File;
import java.util.logging.Level;

import net.minecraft.world.WorldType;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import com.google.common.base.Optional;

import ebxl4.exceptions.NoFreeWorldIDsException;
import ebxl4.exceptions.WorldIDTakenException;
import ebxl4.lib.settingsupdates.TestSampleUpdate;

public abstract class EbXL4Configuration {
  
  public static void init(File configFile) {
    Optional<Configuration> optionalConfig = Optional.absent();
    Configuration config;
    WorldType tmp;
    
    try {
        optionalConfig = Optional.of(new Configuration(configFile));
        final Configuration configuration = optionalConfig.get();
        
        // Check to see if we need to update
        CheckVersion(configuration);
        
        // Load the World type id
        Property worldId = configuration.get(Configuration.CATEGORY_GENERAL, "WorldID", GeneralSettings.worldID);
        worldId.comment = "The world id that " + ModInfo.MOD_NAME + " uses to identify it's world type.";
        
        if(worldId.getInt() == -1) {
          if(WorldType.worldTypes[12] == null) {
            worldId.set(12);
          } else {           
            for(int i = 3; i < WorldType.worldTypes.length; i++) {
              if(WorldType.worldTypes[i] == null) {
                worldId.set(i);
                LogHelper.info("The World ID was set to %d.", worldId.getInt());
                break;
              }
            }
            
            if(worldId.getInt() == -1) {
              LogHelper.severe("There are no free world types.");
              throw new NoFreeWorldIDsException();
            }
          }
        } else if(WorldType.worldTypes[worldId.getInt()] != null) {
          LogHelper.severe("Another Mod is using World ID %d. You will need to change %s's World ID, or the one used by the other mod.", worldId.getInt(), ModInfo.MOD_NAME);
          throw new WorldIDTakenException();
        }
               
        GeneralSettings.worldID = worldId.getInt();
    } catch (final Exception e) {
      //final Exception e
      LogHelper.log(Level.SEVERE, e, "%s had had a problem loading its configuration", ModInfo.MOD_NAME);
    } finally {
      if (optionalConfig.isPresent() && optionalConfig.get().hasChanged()) optionalConfig.get().save();
    }
  }
  
  private static void CheckVersion(Configuration configuration) {
    // Store the version of the configuration in the config file
    Property genVersion = configuration.get(Configuration.CATEGORY_GENERAL, "configuration_version", ModInfo.MOD_VERSION);
    genVersion.comment = "The version of " + ModInfo.MOD_NAME + " that was used to generate this configeration.";
    GeneralSettings.cfgVersion.setVersion(genVersion.getString());
    
    // If the config file is older than the mod we may need to update it.
    if(GeneralSettings.modVersion.compareTo(GeneralSettings.cfgVersion) > 0) {
      // Load the updates
      SettingsUpdates.init();
      
      // Run the updates
      for (SettingsUpdates update : SettingsUpdates.updates) {
        if(GeneralSettings.cfgVersion.compareTo(update.getVersion()) < 0) update.Update(configuration);
      }
    }
  }
}
