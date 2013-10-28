package ebxl4.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import ebxl4.EbXL4;
import ebxl4.lib.LogHelper;

public class EbXL4Packet implements IPacketHandler {

  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
    // TODO Auto-generated method stub
    //((EntityPlayer) player).worldObj.getWorldInfo().getGeneratorOptions();
    
    if(packet.data != null && ((EntityPlayer) player).worldObj.getWorldInfo().getTerrainType() == EbXL4.worldType) {
      String k = "";
      float cloudHeight = 0F;
      DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
      
      try {
        cloudHeight = inputStream.readFloat();
      } catch (Exception e) {}
      LogHelper.info("EbXL++ Cloud height: %f", cloudHeight);
    } else {
      LogHelper.info("No Packet Data");
    }
    
    
    
    
  }

}
