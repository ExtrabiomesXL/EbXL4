package ebxl4.lib;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.oredict.OreDictionary;

public class EBXLCommandHandler2 extends CommandBase {

  @Override
  public String getCommandName() {
    // TODO Auto-generated method stub
    return "tb";
  }

  @Override
  public String getCommandUsage(ICommandSender icommandsender) {
    // TODO Auto-generated method stub
    return "/tb";
  }

  @Override
  public void processCommand(ICommandSender icommandsender, String[] cmds) {
    // TODO Auto-generated method stub
    if (icommandsender instanceof EntityPlayer) {
      EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(icommandsender);
      
      //player.rotationYaw
      
      switch(MathHelper.floor_double((double)(entityplayermp.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
        case 0:
          entityplayermp.posZ += 2000;
          break;
        case 1:
          entityplayermp.posX -= 2000;
          break;
        case 2:
          entityplayermp.posZ -= 2000;
          break;
        case 3:
          entityplayermp.posX += 2000;
          break;
      }
      
      entityplayermp.playerNetServerHandler.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
      
      
      
      entityplayermp.addChatMessage("Moved Player");
      
    }

  }

  

  private void helpList(EntityPlayer player) {
    // List the available commands
    player.addChatMessage("\u00A72-ExtrabiomesXl Commands-\u00A7r");
    player.addChatMessage("/ebxl help [command]");
    player.addChatMessage("/ebxl lastseed <treetype>");
    player.addChatMessage("/ebxl killtree <x> <y> <z>");
    player.addChatMessage("/ebxl saplingdespawntime [ticks]");
    player.addChatMessage("/ebxl spawntree <treetype> <x> <y> <z> [seed]");
    player.addChatMessage("/ebxl version");
  }
}
