package ebxl4.world;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ebxl4.gui.GuiCreateEbXL4World;
import ebxl4.lib.LogHelper;
import ebxl4.world.biome.EbXL4ChunkManager;
import ebxl4.world.chunk.EbXL4ChunkProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class EbXL4WorldType extends WorldType {
  public EbXL4WorldType(int id, String name) {
    super(id, name);
  }

  //Sets up the biome and WorldChunkManager.
  @Override
  public WorldChunkManager getChunkManager(World world) {
    return new EbXL4ChunkManager(world); //WorldChunkManager can be used but will
  }

  //Sets up the ChunkProvider. Use ChunkProviderHell for single biome worlds else use ChunkProviderGenerate or a custom provider.
  @Override
  public IChunkProvider getChunkGenerator(World world, String options) {
    return new EbXL4ChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), options); //
  }

  //Gets the spawn fuzz for players who join the world.
  @Override
  public int getSpawnFuzz() {
      return 100;
  }
  
  //public String getTranslateName()
  //{
  //  return "EbXL++";
  //}
  
  /**
   * Called when the 'Customize' button is pressed on world creation GUI
   * @param instance The minecraft instance
   * @param guiCreateWorld the createworld GUI
   */
  @Override
  @SideOnly(Side.CLIENT)
  public void onCustomizeButton(Minecraft instance, GuiCreateWorld guiCreateWorld)
  {
      LogHelper.info("Customise EbXL++ World Event.");
      instance.displayGuiScreen(new GuiCreateEbXL4World(guiCreateWorld, guiCreateWorld.generatorOptionsToUse));
  }

  /*
   * Should world creation GUI show 'Customize' button for this world type?
   * @return if this world type has customization parameters
   */
  @Override
  public boolean isCustomizable()
  {
      return true;
  }
}
