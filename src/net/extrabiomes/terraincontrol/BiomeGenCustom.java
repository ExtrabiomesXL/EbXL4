
package net.extrabiomes.terraincontrol;

import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;

import com.khorn.terraincontrol.MobAlternativeNames;
import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.configuration.BiomeConfig;
import com.khorn.terraincontrol.configuration.WeightedMobSpawnGroup;

public class BiomeGenCustom extends BiomeGenBase
{
    private int     skyColor;
    private int     grassColor;
    private boolean grassColorIsMultiplier;
    private int     foliageColor;
    private boolean foliageColorIsMultiplier;

    private boolean grassColorSet   = false;
    private boolean foliageColorSet = false;

    public BiomeGenCustom(final int id, final String name) {
        super(id);
        setBiomeName(name);

    }

    // Adds the mobs to the internal list. Displays a warning for each
    // mob type
    // it doesn't understand
    protected void addMobs(final List<SpawnListEntry> internalList, final boolean addDefaults,
            final List<WeightedMobSpawnGroup> configList)
    {
        if (!addDefaults) internalList.clear();
        for (final WeightedMobSpawnGroup mobGroup : configList) {
            final Class<? extends Entity> entityClass = getEntityClass(mobGroup);
            if (entityClass != null)
                internalList.add(new SpawnListEntry(entityClass, mobGroup.getWeight(), mobGroup
                        .getMin(), mobGroup.getMax()));
            else
                TerrainControl.log(Level.WARNING, "Mob type " + mobGroup.getMobName()
                        + " not found in " + biomeName);
        }
    }

    public void CopyBiome(final BiomeGenBase baseBiome) {
        fillerBlock = baseBiome.fillerBlock;
        topBlock = baseBiome.topBlock;
        biomeName = baseBiome.biomeName;
        color = baseBiome.color;
        minHeight = baseBiome.minHeight;
        maxHeight = baseBiome.maxHeight;
        temperature = baseBiome.temperature;

        theBiomeDecorator = baseBiome.theBiomeDecorator;
        waterColorMultiplier = baseBiome.waterColorMultiplier;

        spawnableMonsterList = baseBiome.getSpawnableList(EnumCreatureType.monster);
        spawnableCreatureList = baseBiome.getSpawnableList(EnumCreatureType.creature);
        spawnableWaterCreatureList = baseBiome.getSpawnableList(EnumCreatureType.waterCreature);
        field_82914_M = baseBiome.getSpawnableList(EnumCreatureType.ambient);
    }

    // getFoliageColorAtCoords
    @Override
    public int getBiomeFoliageColor() {
        if (!foliageColorSet) return super.getBiomeFoliageColor();
        if (foliageColorIsMultiplier) {
            final double temperature = getFloatTemperature();
            final double rainfall = getFloatRainfall();

            return ((ColorizerFoliage.getFoliageColor(temperature, rainfall) & 0xFEFEFE) + foliageColor) / 2;
        } else
            return foliageColor;
    }

    // getGrassColorAtCoords
    @Override
    public int getBiomeGrassColor() {
        if (!grassColorSet) return super.getBiomeGrassColor();
        if (grassColorIsMultiplier) {
            final double temperature = getFloatTemperature();
            final double rainfall = getFloatRainfall();

            return ((ColorizerFoliage.getFoliageColor(temperature, rainfall) & 0xFEFEFE) + grassColor) / 2;
        } else
            return grassColor;

    }

    // Gets the class of the entity.
    @SuppressWarnings("unchecked")
    protected Class<? extends Entity> getEntityClass(final WeightedMobSpawnGroup mobGroup) {
        final String mobName = MobAlternativeNames.getInternalMinecraftName(mobGroup.getMobName());
        return (Class<? extends Entity>) EntityList.stringToClassMapping.get(mobName);
    }

    // Sky color from Temp
    @Override
    public int getSkyColorByTemp(final float v) {
        return skyColor;
    }

    /**
     * Needs a BiomeConfig that has all the visual settings present.
     * 
     * @param config
     */
    @SuppressWarnings("unchecked")
    public void setEffects(final BiomeConfig config) {
        temperature = config.BiomeTemperature;
        rainfall = config.BiomeWetness;
        waterColorMultiplier = config.WaterColor;
        skyColor = config.SkyColor;
        grassColor = config.GrassColor;
        grassColorIsMultiplier = config.GrassColorIsMultiplier;
        foliageColor = config.FoliageColor;
        foliageColorIsMultiplier = config.FoliageColorIsMultiplier;

        if (grassColor != 0xffffff) grassColorSet = true;

        if (foliageColor != 0xffffff) foliageColorSet = true;

        // Mob spawning
        addMobs(spawnableMonsterList, config.spawnMonstersAddDefaults, config.spawnMonsters);
        addMobs(spawnableCreatureList, config.spawnCreaturesAddDefaults, config.spawnCreatures);
        addMobs(spawnableWaterCreatureList, config.spawnWaterCreaturesAddDefaults,
                config.spawnWaterCreatures);
        addMobs(field_82914_M, config.spawnAmbientCreaturesAddDefaults,
                config.spawnAmbientCreatures);

        // color ?
        // this.x = 522674;

        // duno.
        // this.A = 9154376;

    }

    @Override
    public String toString() {
        return "BiomeGenCustom of " + biomeName;
    }
}
