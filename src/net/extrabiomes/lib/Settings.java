
package net.extrabiomes.lib;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.world.ExtrabiomesWorldType;
import net.minecraft.creativetab.CreativeTabs;

import com.google.common.base.Optional;

public abstract class Settings
{
    private static int                  creativeTabIndex;
    private static ExtrabiomesWorldType worldType;

    public static CreativeTabs getCreativeTab() {
        return CreativeTabs.creativeTabArray[creativeTabIndex];
    }

    public static Optional<LocalWorld> getTCWorld() {
    	Optional<LocalWorld> tcWorld = Optional.fromNullable((LocalWorld)worldType.getWorldTC());
        return tcWorld;
    }

    public static ExtrabiomesWorldType getWorldType() {
        return worldType;
    }

    public static void setCreativeTab(final CreativeTabs tab) {
        creativeTabIndex = tab.getTabIndex();
    }

    public static void setWorldType(final ExtrabiomesWorldType worldType) {
        Settings.worldType = worldType;
    }
}
