package ebxl4.proxy;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy {
  public void addGrassPlant(Block block, int metadata, int weight) {
    MinecraftForge.addGrassPlant(block, metadata, weight);
  }
  
  public void addName(Object object, String name) {
    LanguageRegistry.addName(object, name);
  }
  
  public void addRecipe(IRecipe recipe) {
    CraftingManager.getInstance().getRecipeList().add(recipe);
  }
  
  public void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience) {
    FurnaceRecipes.smelting().addSmelting(itemID, metadata, itemstack, experience);
  }
  
  public void addSmelting(ItemStack input, ItemStack output, float experience) {
    FurnaceRecipes.smelting().addSmelting(input.itemID, output, experience);
  }
  
  public ArrayList<ItemStack> getOres(String name) {
    return OreDictionary.getOres(name);
  }
  
  public boolean isModLoaded(String modID) {
    return Loader.isModLoaded(modID);
  }
  
  public boolean postEventToBus(Event event) {
    return MinecraftForge.EVENT_BUS.post(event);
  }
  
  public void registerEventHandler(Object target) {
    MinecraftForge.EVENT_BUS.register(target);
  }

  public int registerBlockHandler(ISimpleBlockRenderingHandler handler) {
    return 0;
  }

  public Logger getFMLLogger() {
    return FMLLog.getLogger();
  }

}
