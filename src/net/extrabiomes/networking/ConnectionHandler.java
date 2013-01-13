
package net.extrabiomes.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.extrabiomes.utility.LogWriter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.configuration.TCDefaultValues;
import com.khorn.terraincontrol.configuration.WorldConfig;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler {

    @Override
    public void clientLoggedIn(final NetHandler clientHandler, final INetworkManager manager,
            final Packet1Login login)
    {}

    @Override
    public void connectionClosed(final INetworkManager manager) {}

    @Override
    public void connectionOpened(final NetHandler netClientHandler, final MinecraftServer server,
            final INetworkManager manager)
    {}

    @Override
    public void connectionOpened(final NetHandler netClientHandler, final String server,
            final int port, final INetworkManager manager)
    {}

    @Override
    public String connectionReceived(final NetLoginHandler netHandler, final INetworkManager manager)
    {
        return null;
    }

    @Override
    public void playerLoggedIn(final Player player, final NetHandler netHandler,
            final INetworkManager manager)
    {
        // Server-side - called whenever a player logs in
        // I couldn't find a way to detect if the client has
        // TerrainControl,
        // so for now the configs are sent anyway.

        // Get the config
        final String worldName = MinecraftServer.getServer().worldServers[0].getSaveHandler()
                .getSaveDirectoryName();
        final LocalWorld worldTC = TerrainControl.getWorld(worldName);

        if (worldTC == null) // World not loaded
            return;
        final WorldConfig config = worldTC.getSettings();

        // Serialize it
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final DataOutputStream stream = new DataOutputStream(outputStream);
        try {
            stream.writeInt(TCDefaultValues.ProtocolVersion.intValue());
            config.Serialize(stream);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Make the packet
        final Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = TCDefaultValues.ChannelName.stringValue();
        packet.data = outputStream.toByteArray();
        packet.length = outputStream.size();

        // Send the packet
        ((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(packet);
        LogWriter.fine("Terrain Control: sent config");
    }

}
