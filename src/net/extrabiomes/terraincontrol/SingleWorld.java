
package net.extrabiomes.terraincontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.extrabiomes.generation.ExtraBiomesWorldGenerator;
import net.extrabiomes.generation.biomes.ExtraBiomesBiome;
import net.extrabiomes.terraincontrol.structuregens.MineshaftGen;
import net.extrabiomes.terraincontrol.structuregens.NetherFortressGen;
import net.extrabiomes.terraincontrol.structuregens.RareBuildingGen;
import net.extrabiomes.terraincontrol.structuregens.StrongholdGen;
import net.extrabiomes.terraincontrol.structuregens.VillageGen;
import net.extrabiomes.terraincontrol.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;

import com.khorn.terraincontrol.DefaultBiome;
import com.khorn.terraincontrol.DefaultMaterial;
import com.khorn.terraincontrol.IBiomeManager;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.configuration.BiomeConfig;
import com.khorn.terraincontrol.configuration.Tag;
import com.khorn.terraincontrol.configuration.WorldConfig;
import com.khorn.terraincontrol.customobjects.CustomObjectStructureCache;
import com.khorn.terraincontrol.generator.resourcegens.TreeType;

public class SingleWorld implements LocalWorld
{
    public static void restoreBiomes() {
        for (final BiomeGenBase oldBiome : biomesToRestore) {
            if (oldBiome == null) continue;
            BiomeGenBase.biomeList[oldBiome.biomeID] = oldBiome;
        }
        nextBiomeId = 0;
        defaultBiomes.clear();
    }

    private ChunkProvider                     generator;
    private World                             world;
    private WorldConfig                       settings;
    private final String                      name;
    private long                              seed;
    private IBiomeManager                     biomeManager;

    private TCWorldChunkManagerOld            oldBiomeManager;
    private static int                        nextBiomeId     = 0;
    private static int                        maxBiomeCount   = 256;
    private static Biome[]                    biomes          = new Biome[maxBiomeCount];

    private static BiomeGenBase[]             biomesToRestore = new BiomeGenBase[maxBiomeCount];

    private final HashMap<String, LocalBiome> biomeNames      = new HashMap<String, LocalBiome>();

    private static ArrayList<LocalBiome>      defaultBiomes   = new ArrayList<LocalBiome>();
    public StrongholdGen                      strongholdGen;
    public VillageGen                         villageGen;
    public MineshaftGen                       mineshaftGen;
    public RareBuildingGen                    rareBuildingGen;

    public NetherFortressGen                  netherFortressGen;

    private WorldGenDungeons                  dungeonGen;
    private WorldGenTrees                     tree;
    private WorldGenTrees                     cocoaTree;
    private WorldGenBigTree                   bigTree;
    private WorldGenForest                    forest;
    private WorldGenSwamp                     swampTree;
    private WorldGenTaiga1                    taigaTree1;
    private WorldGenTaiga2                    taigaTree2;
    private WorldGenBigMushroom               hugeMushroom;
    private WorldGenHugeTrees                 jungleTree;

    private WorldGenShrub                     groundBush;
    private boolean                           createNewChunks;
    private Chunk[]                           chunkCache;

    private Chunk                             cachedChunk;
    private int                               currentChunkX;

    private int                               currentChunkZ;
    private BiomeGenBase[]                    biomeGenBaseArray;

    private int[]                             biomeIntArray;
    private int                               worldHeight     = 128;

    private int                               heightBits      = 7;

    public SingleWorld(final String _name) {
        name = _name;

        for (int i = 0; i < DefaultBiome.values().length; i++) {
        	AddVanillaBiome(i);
        }
    }
    
    
    /**
     * Constructor that takes an array of Biome IDs to add to world generation
     * 
     * @param _name The name of the world
     * @param idsToAdd An array of Biome IDs to add
     */
    public SingleWorld(final String _name, int[] idsToAdd){
    	name = _name;
    	
    	for(int i: idsToAdd){
    		AddVanillaBiome(i);
    	}
    }
    
    
    /**
     * Adds a vanilla biome with the given ID to world generation and returns it.
     */
    @Override
    public Biome AddVanillaBiome(int biomeID){
    	final BiomeGenBase oldBiome = BiomeGenBase.biomeList[biomeID];
        biomesToRestore[biomeID] = oldBiome;
        final BiomeGenCustom custom = new BiomeGenCustom(nextBiomeId++, oldBiome.biomeName);
        custom.CopyBiome(oldBiome);
        final Biome biome = new Biome(custom);
        biomes[biome.getId()] = biome;
        defaultBiomes.add(biome);
        biomeNames.put(biome.getName(), biome);
        return biome;
    }

    @Override
    public LocalBiome AddBiome(final String name, final int id) {
        final Biome biome = new Biome(new BiomeGenCustom(id, name));
        biomes[biome.getId()] = biome;
        biomeNames.put(biome.getName(), biome);
        return biome;
    }
    
	@Override
	public LocalBiome AddBiome(ExtraBiomesBiome biome, int id) {
		biomesToRestore[biome.biomeID] = biome;
        //biomeNames.put(biome.getName(), biome);
        AddVanillaBiome(id);
        
        return biome.tcBiome;
	}

    @Override
    public void attachMetadata(final int x, final int y, final int z, final Tag tag) {
        // Convert Tag to a native nms tag
        final NBTTagCompound nmsTag = NBTHelper.getNMSFromNBTTagCompound(tag);
        // Add the x, y and z position to it
        nmsTag.setInteger("x", x);
        nmsTag.setInteger("y", y);
        nmsTag.setInteger("z", z);
        // Create a Tile Entity of it and add it to the world
        final TileEntity tileEntity = TileEntity.createAndLoadEntity(nmsTag);
        world.setBlockTileEntity(x, y, z, tileEntity);
    }

    public void FillChunkForBiomes(final Chunk chunk, final int x, final int z) {

        final byte[] arrayOfByte2 = chunk.getBiomeArray();
        biomeIntArray = getBiomes(biomeIntArray, x * 16, z * 16, 16, 16);

        for (int i1 = 0; i1 < arrayOfByte2.length; i1++)
            arrayOfByte2[i1] = (byte) biomeIntArray[i1];
    }

    @Override
    public LocalBiome getBiome(final int x, final int z) {
        return getBiomeById(world.getBiomeGenForCoords(x, z).biomeID);
    }

    @Override
    public LocalBiome getBiomeById(final int id) {
        return biomes[id];
    }

    @Override
    public double getBiomeFactorForOldBM(final int index) {
        return oldBiomeManager.oldTemperature[index] * oldBiomeManager.oldWetness[index];
    }

    @Override
    public int getBiomeId(final int x, final int z) {
        return world.getBiomeGenForCoords(x, z).biomeID;
    }

    @Override
    public int getBiomeIdByName(final String name) {
        return biomeNames.get(name).getId();
    }

    @Override
    public int[] getBiomes(int[] biomeArray, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (biomeManager != null)
            return biomeManager.getBiomesTC(biomeArray, x, z, x_size, z_size);

        biomeGenBaseArray = world.provider.worldChunkMgr.getBiomeGenAt(biomeGenBaseArray, x, z,
                x_size, z_size, true);
        if (biomeArray == null || biomeArray.length < x_size * z_size)
            biomeArray = new int[x_size * z_size];
        for (int i = 0; i < x_size * z_size; i++)
            biomeArray[i] = biomeGenBaseArray[i].biomeID;
        return biomeArray;
    }

    @Override
    public int[] getBiomesUnZoomed(int[] biomeArray, final int x, final int z, final int x_size,
            final int z_size)
    {
        if (biomeManager != null)
            return biomeManager.getBiomesUnZoomedTC(biomeArray, x, z, x_size, z_size);

        biomeGenBaseArray = world.provider.worldChunkMgr.getBiomesForGeneration(biomeGenBaseArray,
                x, z, x_size, z_size);
        if (biomeArray == null || biomeArray.length < x_size * z_size)
            biomeArray = new int[x_size * z_size];
        for (int i = 0; i < x_size * z_size; i++)
            biomeArray[i] = biomeGenBaseArray[i].biomeID;
        return biomeArray;
    }

    @Override
    public LocalBiome getCalculatedBiome(final int x, final int z) {
        return getBiomeById(getCalculatedBiomeId(x, z));
    }

    @Override
    public int getCalculatedBiomeId(final int x, final int z) {
        if (biomeManager != null) return biomeManager.getBiomeTC(x, z);
        return world.provider.worldChunkMgr.getBiomeGenAt(x, z).biomeID;
    }

    private Chunk getChunk(int x, final int y, int z) {
        if (y < 0 || y >= worldHeight) return null;

        x = x >> 4;
        z = z >> 4;
        if (cachedChunk != null && cachedChunk.xPosition == x && cachedChunk.zPosition == z)
            return cachedChunk;

        final int index_x = x - currentChunkX;
        final int index_z = z - currentChunkZ;
        if ((index_x == 0 || index_x == 1) && (index_z == 0 || index_z == 1))
            return cachedChunk = chunkCache[index_x | index_z << 1];
        else if (createNewChunks || world.getChunkProvider().chunkExists(x, z))
            return cachedChunk = world.getChunkFromBlockCoords(x, z);
        else
            return null;

    }

    public ChunkProvider getChunkGenerator() {
        return generator;
    }

    @Override
    public ArrayList<LocalBiome> getDefaultBiomes() {
    	
        return defaultBiomes;
    }

    @Override
    public int getFreeBiomeId() {
        return nextBiomeId++;
    }

    @Override
    public int getHeight() {
        return worldHeight;
    }

    @Override
    public int getHeightBits() {
        return heightBits;
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        final Chunk chunk = getChunk(x, 0, z);
        if (chunk == null) return -1;
        z = z & 0xF;
        x = x & 0xF;
        int y = chunk.getHeightValue(x, z);
        while (chunk.getBlockID(x, y, z) != DefaultMaterial.AIR.id && y <= worldHeight)
            // Fix for incorrect lightmap
            y += 1;
        return y;
    }

    @Override
    public int getLightLevel(final int x, final int y, final int z) {
        return world.getBlockLightValue(x, y, z);
    }

    @Override
    public int getLiquidHeight(int x, int z) {
        final Chunk chunk = getChunk(x, 0, z);
        if (chunk == null) return -1;
        z = z & 0xF;
        x = x & 0xF;
        for (int y = worldHeight - 1; y > 0; y--) {
            final int id = chunk.getBlockID(x, y, z);
            if (DefaultMaterial.getMaterial(id).isLiquid()) return y;
        }
        return -1;
    }

    @Override
    public DefaultMaterial getMaterial(final int x, final int y, final int z) {
        final int id = getTypeId(x, y, z);
        return DefaultMaterial.getMaterial(id);
    }

    @Override
    public int getMaxBiomesCount() {
        return maxBiomeCount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalBiome getNullBiome(final String name) {
        return null;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public WorldConfig getSettings() {
        return settings;
    }

    @Override
    public int getSolidHeight(int x, int z) {
        final Chunk chunk = getChunk(x, 0, z);
        if (chunk == null) return -1;
        z = z & 0xF;
        x = x & 0xF;
        for (int y = worldHeight - 1; y > 0; y--) {
            final int id = chunk.getBlockID(x, y, z);
            if (DefaultMaterial.getMaterial(id).isSolid()) return y;
        }
        return -1;
    }

    @Override
    public float[] getTemperatures(final int x, final int z, final int x_size, final int z_size) {
        if (biomeManager != null) return biomeManager.getTemperaturesTC(x, z, x_size, z_size);
        return world.provider.worldChunkMgr.getTemperatures(new float[0], x, z, x_size, z_size);
    }

    @Override
    public byte getTypeData(int x, final int y, int z) {
        final Chunk chunk = getChunk(x, y, z);
        if (chunk == null) return 0;

        z = z & 0xF;
        x = x & 0xF;

        return (byte) chunk.getBlockMetadata(x, y, z);
    }

    @Override
    public int getTypeId(final int x, final int y, final int z) {
        if (world.getChunkProvider().chunkExists(x / 16, z / 16) || createNewChunks)
            return world.getBlockId(x, y, z);
        else
            return 0;
    }

    public World getWorld() {
        return world;
    }

    public void Init(final World world, final WorldConfig config) {
        settings = config;

        this.world = world;
        seed = world.getSeed();

        dungeonGen = new WorldGenDungeons();
        strongholdGen = new StrongholdGen(config);

        villageGen = new VillageGen(config);
        mineshaftGen = new MineshaftGen();
        rareBuildingGen = new RareBuildingGen(config);
        netherFortressGen = new NetherFortressGen();

        tree = new WorldGenTrees(false);
        cocoaTree = new WorldGenTrees(false, 5, 3, 3, true);
        bigTree = new WorldGenBigTree(false);
        forest = new WorldGenForest(false);
        swampTree = new WorldGenSwamp();
        taigaTree1 = new WorldGenTaiga1();
        taigaTree2 = new WorldGenTaiga2(false);
        hugeMushroom = new WorldGenBigMushroom();
        jungleTree = new WorldGenHugeTrees(false, 15, 3, 3);
        groundBush = new WorldGenShrub(3, 0);

        chunkCache = new Chunk[4];
        generator = new ChunkProvider(this);
        
        
        //TODO 
        ExtraBiomesWorldGenerator.instance = new ExtraBiomesWorldGenerator(this);
    }

    public void InitM(final World world, final WorldConfig config) {
        settings = config;
        this.world = world;
        seed = world.getSeed();
        for (final Biome biome : biomes)
            // Apply settings for biomes
            if (biome != null && config.biomeConfigs[biome.getId()] != null)
                biome.setEffects(config.biomeConfigs[biome.getId()]);
    }

    @Override
    public boolean isEmpty(final int x, final int y, final int z) {
        return getTypeId(x, y, z) == 0;
    }

    @Override
    public boolean isLoaded(int x, final int y, int z) {
        if (y < 0 || y >= worldHeight) return false;
        x = x >> 4;
        z = z >> 4;

        return world.getChunkProvider().chunkExists(x, z);
    }

    public void LoadChunk(final int x, final int z) {
        currentChunkX = x;
        currentChunkZ = z;
        chunkCache[0] = world.getChunkFromChunkCoords(x, z);
        chunkCache[1] = world.getChunkFromChunkCoords(x + 1, z);
        chunkCache[2] = world.getChunkFromChunkCoords(x, z + 1);
        chunkCache[3] = world.getChunkFromChunkCoords(x + 1, z + 1);
        createNewChunks = true;
    }

    @Override
    public void PlaceDungeons(final Random rand, final int x, final int y, final int z) {
        dungeonGen.generate(world, rand, x, y, z);
    }

    @Override
    public void placePopulationMobs(final BiomeConfig config, final Random random,
            final int chunkX, final int chunkZ)
    {
        SpawnerAnimals.performWorldGenSpawning(getWorld(), ((Biome) config.Biome).getHandle(),
                chunkX * 16 + 8, chunkZ * 16 + 8, 16, 16, random);
    }

    @Override
    public boolean PlaceTerrainObjects(final Random rand, final int chunk_x, final int chunk_z) {
        boolean isVillagePlaced = false;
        if (settings.strongholdsEnabled)
            strongholdGen.generateStructuresInChunk(world, rand, chunk_x, chunk_z);
        if (settings.mineshaftsEnabled)
            mineshaftGen.generateStructuresInChunk(world, rand, chunk_x, chunk_z);
        if (settings.villagesEnabled)
            isVillagePlaced = villageGen.generateStructuresInChunk(world, rand, chunk_x, chunk_z);
        if (settings.rareBuildingsEnabled)
            rareBuildingGen.generateStructuresInChunk(world, rand, chunk_x, chunk_z);
        if (settings.netherFortressesEnabled)
            netherFortressGen.generateStructuresInChunk(world, rand, chunk_x, chunk_z);

        return isVillagePlaced;
    }

    @Override
    public boolean PlaceTree(final TreeType type, final Random rand, final int x, final int y,
            final int z)
    {
        switch (type)
        {
            case Tree:
                return tree.generate(world, rand, x, y, z);
            case BigTree:
                bigTree.setScale(1.0D, 1.0D, 1.0D);
                return bigTree.generate(world, rand, x, y, z);
            case Forest:
                return forest.generate(world, rand, x, y, z);
            case HugeMushroom:
                hugeMushroom.setScale(1.0D, 1.0D, 1.0D);
                return hugeMushroom.generate(world, rand, x, y, z);
            case SwampTree:
                return swampTree.generate(world, rand, x, y, z);
            case Taiga1:
                return taigaTree1.generate(world, rand, x, y, z);
            case Taiga2:
                return taigaTree2.generate(world, rand, x, y, z);
            case JungleTree:
                return jungleTree.generate(world, rand, x, y, z);
            case GroundBush:
                return groundBush.generate(world, rand, x, y, z);
            case CocoaTree:
                return cocoaTree.generate(world, rand, x, y, z);
            default:
                break;
        }
        return false;
    }

    @Override
    public void PrepareTerrainObjects(final int x, final int z, final byte[] chunkArray,
            final boolean dry)
    {
        if (settings.strongholdsEnabled) strongholdGen.generate(null, world, x, z, chunkArray);

        if (settings.mineshaftsEnabled) mineshaftGen.generate(null, world, x, z, chunkArray);
        if (settings.villagesEnabled && dry) villageGen.generate(null, world, x, z, chunkArray);
        if (settings.rareBuildingsEnabled) rareBuildingGen.generate(null, world, x, z, chunkArray);
        if (settings.netherFortressesEnabled)
            netherFortressGen.generate(null, world, x, z, chunkArray);

    }

    //@Override
    public void replaceBiomesLate() {
        if (settings.HaveBiomeReplace) {
            final byte[] ChunkBiomes = chunkCache[0].getBiomeArray();

            for (int i = 0; i < ChunkBiomes.length; i++)
                ChunkBiomes[i] = (byte) (settings.ReplaceMatrixBiomes[ChunkBiomes[i] & 0xFF] & 0xFF);
        }

    }

    @Override
    public void replaceBlocks() {
        if (settings.BiomeConfigsHaveReplacement) {

            final Chunk rawChunk = chunkCache[0];

            final ExtendedBlockStorage[] sectionsArray = rawChunk.getBlockStorageArray();

            final byte[] ChunkBiomes = rawChunk.getBiomeArray();

            final int x = currentChunkX * 16;
            final int z = currentChunkZ * 16;

            for (final ExtendedBlockStorage section : sectionsArray) {
                if (section == null) continue;

                for (int sectionX = 0; sectionX < 16; sectionX++)
                    for (int sectionZ = 0; sectionZ < 16; sectionZ++) {
                        final BiomeConfig biomeConfig = settings.biomeConfigs[ChunkBiomes[sectionZ << 4
                                | sectionX] & 0xFF];

                        if (biomeConfig.ReplaceCount > 0)
                            for (int sectionY = 0; sectionY < 16; sectionY++) {
                                final int blockId = section.getExtBlockID(sectionX, sectionY,
                                        sectionZ);
                                if (biomeConfig.replaceMatrixBlocks[blockId] == null) continue;

                                final int replaceTo = biomeConfig.replaceMatrixBlocks[blockId][section
                                        .getYLocation() + sectionY];
                                if (replaceTo == -1) continue;

                                section.setExtBlockID(sectionX, sectionY, sectionZ, replaceTo >> 4);
                                section.setExtBlockMetadata(sectionX, sectionY, sectionZ,
                                        replaceTo & 0xF);
                                world.getFullBlockLightValue(x + sectionX, section.getYLocation()
                                        + sectionY, z + sectionZ);

                            }
                    }
            }
        }
    }

    public void setBiomeManager(final IBiomeManager manager) {
        biomeManager = manager;
    }

    @Override
    public void setBlock(final int x, final int y, final int z, final int typeId, final int data) {
        this.setBlock(x, y, z, typeId, data, false, false, false);
    }

    @Override
    public void setBlock(final int x, final int y, final int z, final int typeId, final int data, final boolean updateLight, final boolean applyPhysics, final boolean notifyPlayers)
    {
        if (applyPhysics)
        {
            world.setBlock(x, y, z, typeId, data, notifyPlayers ? 0x02 : 0x02 & 0x04);
        } else
        {
            world.setBlock(x, y, z, typeId, data, 0);
        }

        if (updateLight)
        {
            this.world.updateAllLightTypes(x, y, z);
        }
    }

    @Override
    public void setChunksCreations(final boolean createNew) {
        createNewChunks = createNew;
    }

    @Override
    public void setHeightBits(final int heightBits) {
        this.heightBits = heightBits;
        worldHeight = 1 << heightBits;
    }

    public void setOldBiomeManager(final TCWorldChunkManagerOld manager) {
        oldBiomeManager = manager;
        biomeManager = manager;
    }

	@Override
	public void replaceBiomes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tag getMetadata(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomObjectStructureCache getStructureCache() {
		// TODO Auto-generated method stub
		return null;
	}



}
