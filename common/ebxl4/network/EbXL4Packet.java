package ebxl4.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import ebxl4.lib.LogHelper;

public class EbXL4Packet implements IPacketHandler {

  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
    // TODO Auto-generated method stub
    //((EntityPlayer) player).worldObj.getWorldInfo().get
    
    if(packet.data != null) {
      String k = "";
      DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
      
      try {
        k = inputStream.readUTF();
      } catch (Exception e) {}
      
      
      LogHelper.info("EbXL++ Logon packet: %s", k);
    } else {
      LogHelper.info("No Packet Data");
    }
    
    
    
    
  }

}
