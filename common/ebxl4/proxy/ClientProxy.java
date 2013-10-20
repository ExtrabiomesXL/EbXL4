package ebxl4.proxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
  @Override
  public int registerBlockHandler(ISimpleBlockRenderingHandler handler) { 
    final int renderId = RenderingRegistry.getNextAvailableRenderId();
    RenderingRegistry.registerBlockHandler(renderId, handler);
    return renderId;
  }
}
