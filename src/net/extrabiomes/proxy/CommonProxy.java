
package net.extrabiomes.proxy;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * CommonProxy - Provides access to most things outside the mod
 * 
 * @author ScottKillen
 * 
 */
public class CommonProxy {

    public void addStringLocalization(final String key, final String value) {
        LanguageRegistry.instance().addStringLocalization(key, value);
    }

    public void addStringLocalization(final String key, final String lang, final String value) {
        LanguageRegistry.instance().addStringLocalization(key, lang, value);
    }

    public Logger getFMLLogger() {
        return FMLLog.getLogger();
    }

    public void loadLocalization(final String filename, final String locale) {
        LanguageRegistry.instance().loadLocalization(filename, locale, true);
    }

}
