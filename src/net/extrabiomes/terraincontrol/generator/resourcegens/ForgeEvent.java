package net.extrabiomes.terraincontrol.generator.resourcegens;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.exception.InvalidResourceException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public class ForgeEvent extends Resource {

    public enum Type {
        DECORATE_PRE, DECORATE_POST, ORE_PRE, ORE_POST, POPULATE_PRE, POPULATE_POST, MOD_GENERATE
    }
    
    private Type type;
    
    @Override
    public void spawn(LocalWorld world, Random random, int x, int z) {
        // Left blank, as process(..) already handles this.
    }

    @Override
    public void process(LocalWorld world, Random random, int chunkX, int chunkZ) {
        switch (type) {
            case DECORATE_PRE:
                MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(world.getMCWorld(), random, chunkX, chunkZ));
                break;
            case DECORATE_POST:
                MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(world.getMCWorld(), random, chunkX, chunkZ));
                break;
            case ORE_PRE:
                MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(world.getMCWorld(), random, chunkX, chunkZ));
                break;
            case ORE_POST:
                MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(world.getMCWorld(), random, chunkX, chunkZ));
                break;
            case POPULATE_PRE:
                MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(world.getMCWorld().getChunkProvider(), world.getMCWorld(), random, chunkX, chunkZ, false));
                break;
            case POPULATE_POST:
                MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(world.getMCWorld().getChunkProvider(), world.getMCWorld(), random, chunkX, chunkZ, false));
                break;
            default:
                GameRegistry.generateWorld(chunkX, chunkZ, world.getMCWorld(), world.getMCWorld().getChunkProvider(), world.getMCWorld().getChunkProvider());
        }
    }

    @Override
    public void load(List<String> args) throws InvalidResourceException {
        if (args.size() < 1)
        {
            throw new InvalidResourceException("Too few arguments supplied");
        }
        type = Type.valueOf(args.get(0));
    }

    @Override
    public String makeString() {
        return "ForgeEvent(" + type.toString() + ")";
    }

}
