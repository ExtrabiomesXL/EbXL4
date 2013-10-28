package ebxl4.network;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import ebxl4.EbXL4;
import ebxl4.lib.LogHelper;

public class EbXL4Connection implements IPacketHandler, IConnectionHandler {

  @Override
  public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
    if(((EntityPlayer) player).worldObj.getWorldInfo().getTerrainType() == EbXL4.worldType) {
    
      String k = "This is a test";
      
      //((EntityPlayer) player).worldObj.getWorldInfo().getTerrainType();
      
      LogHelper.info("Player Logged In. Worldgenerator options: %s", ((EntityPlayer) player).worldObj.getWorldInfo().getGeneratorOptions());
      
      ByteArrayOutputStream bytearray = new ByteArrayOutputStream(64);
      DataOutputStream outputStream = new DataOutputStream(bytearray);
      
      try {
        
        outputStream.writeFloat(((EntityPlayer) player).worldObj.getWorldInfo().getTerrainType().getCloudHeight());
      } catch (Exception e) {}
      
      
      //((EntityPlayer) player)
      
      
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "EbXL++channel";
      packet.data = bytearray.toByteArray();
      packet.length = bytearray.size();
      
      manager.addToSendQueue(packet);
      
      //netHandler.
      
      //((NetServerHandler)netHandler).handleCustomPayload(packet);
    } else {
      LogHelper.info("Player Logged In. Not an EbXL World");
    }
    
  }

  @Override
  public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
    return null;
  }

  @Override
  public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}

  @Override
  public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

  @Override
  public void connectionClosed(INetworkManager manager) {}

  @Override
  public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {}

  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {}

}
