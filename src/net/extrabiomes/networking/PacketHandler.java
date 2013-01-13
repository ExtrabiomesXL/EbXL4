
package net.extrabiomes.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.extrabiomes.lib.Reference;
import net.extrabiomes.terraincontrol.SingleWorld;
import net.extrabiomes.utility.LogWriter;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.khorn.terraincontrol.configuration.TCDefaultValues;
import com.khorn.terraincontrol.configuration.WorldConfig;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

@SideOnly(Side.CLIENT)
public class PacketHandler implements IPacketHandler
{

    @Override
    public void onPacketData(final INetworkManager manager,
            final Packet250CustomPayload receivedPacket, final Player player)
    {
        // This method receives the TerrainControl packet with the
        // custom biome
        // colors and weather.

        if (!receivedPacket.channel.equals(Reference.MOD_CHANNEL))
        // Make sure that the right channel is being received
            return;

        // We're on the client, receive the packet
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(receivedPacket.data);
        final DataInputStream stream = new DataInputStream(inputStream);
        try {
            final int serverProtocolVersion = stream.readInt();
            final int clientProtocolVersion = TCDefaultValues.ProtocolVersion.intValue();
            if (serverProtocolVersion == clientProtocolVersion) {
                // Server sent config

                // Restore old biomes
                SingleWorld.restoreBiomes();

                if (receivedPacket.length > 4) {
                    // If the packet wasn't empty, add the new biomes
                    final WorldClient worldMC = FMLClientHandler.instance().getClient().theWorld;

                    final SingleWorld worldTC = new SingleWorld("external");
                    final WorldConfig config = new WorldConfig(stream, worldTC);

                    worldTC.InitM(worldMC, config);
                }

                LogWriter.fine("Terrain Control: config received from server");
            } else
                // Server or client is outdated
                LogWriter.fine("Terrain Control: server has different protocol version! "
                        + "Client: " + TCDefaultValues.ProtocolVersion.intValue() + " Server: "
                        + serverProtocolVersion);
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

}
