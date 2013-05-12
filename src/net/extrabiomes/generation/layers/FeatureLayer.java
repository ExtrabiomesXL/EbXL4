package net.extrabiomes.generation.layers;

import java.util.Random;

import net.extrabiomes.generation.ExtraBiomesDoubleCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.IntCache;

/**
 * @author Chris
 * FeatureLayer is the layer that defines large world features such as forests that exist independently of biomes. This will allow large multibiome forests
 * that slowly change based on their location
 */
public class FeatureLayer implements IWorldGenLayer{

	private long baseSeed;
	private long worldGenSeed;
	private long chunkSeed;
	private Random random;
	
	public FeatureLayer(long seed){
		random = new Random(seed);
	}
	
	@Override
	public double[] getValues(int x, int z, int rangeX, int rangeZ) {
		double[] values = createFeatures(x, z, rangeX, rangeZ);
		values = fuzzyZoom(x, z, rangeX, rangeZ, values);
		values = addFeatures(x, z, rangeX, rangeZ, values);
		values = zoom(x, z, rangeX, rangeZ, values);
		
		return values;
	}

	@Override
	public double getValue(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private double[] createFeatures(int x, int z, int rangeX, int rangeZ){
		
		double[] cache = ExtraBiomesDoubleCache.getDoubleCache(rangeX * rangeZ);
		
		for(int i = 0; i < rangeZ; i++){
			for(int j = 0; j < rangeX; j++){
				cache[j + i * rangeX] = (int)(random.nextDouble() * 10) == 0 ? 1 : 0;
			}
			
			if(x > -rangeX && x <= 0 && z > -rangeZ && z <= 0){
				cache[-x + -z * rangeX] = 1;
			}
		}
		return cache;
	}
	
	private double[] fuzzyZoom(int x, int z, int rangeX, int rangeZ, double[] cache){
        int i1 = x >> 1;
        int j1 = z >> 1;
        int k1 = (rangeX >> 1) + 3;
        int l1 = (rangeZ >> 1) + 3;
        double[] cache1 = ExtraBiomesDoubleCache.getDoubleCache(k1 * 2 * l1 * 2);
        int i2 = k1 << 1;
        int j2;

        for (int k2 = 0; k2 < l1 - 1; ++k2)
        {
            j2 = k2 << 1;
            int l2 = j2 * i2;
            double i3 = cache[0 + (k2 + 0) * k1];
            double j3 = cache[0 + (k2 + 1) * k1];

            for (int k3 = 0; k3 < k1 - 1; ++k3)
            {
                this.initChunkSeed((long)(k3 + i1 << 1), (long)(k2 + j1 << 1));
                double l3 = cache[k3 + 1 + (k2 + 0) * k1];
                double i4 = cache[k3 + 1 + (k2 + 1) * k1];
                cache1[l2] = i3;
                cache1[l2++ + i2] = this.choose(i3, j3);
                cache1[l2] = this.choose(i3, l3);
                cache1[l2++ + i2] = this.choose(i3, l3, j3, i4);
                i3 = l3;
                j3 = i4;
            }
        }

        double[] cache2 = ExtraBiomesDoubleCache.getDoubleCache(rangeX * rangeZ);

        for (j2 = 0; j2 < rangeZ; ++j2)
        {
            System.arraycopy(cache1, (j2 + (z & 1)) * (k1 << 1) + (x & 1), cache2, j2 * rangeX, rangeX);
        }
		return cache2;
	}
	
	private double[] addFeatures(int x, int z, int rangeX, int rangeZ, double[] cache){
		int i1 = x - 1;
        int j1 = z - 1;
        int k1 = rangeX + 2;
        int l1 = rangeZ + 2;
        
        double[] cache1 = ExtraBiomesDoubleCache.getDoubleCache(rangeX * rangeZ);

        for (int i2 = 0; i2 < rangeZ; ++i2)
        {
            for (int j2 = 0; j2 < rangeX; ++j2)
            {
                double k2 = cache[j2 + 0 + (i2 + 0) * k1];
                double l2 = cache[j2 + 2 + (i2 + 0) * k1];
                double i3 = cache[j2 + 0 + (i2 + 2) * k1];
                double j3 = cache[j2 + 2 + (i2 + 2) * k1];
                double k3 = cache[j2 + 1 + (i2 + 1) * k1];
                this.initChunkSeed((long)(j2 + x), (long)(i2 + z));

                if (k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0))
                {
                	double l3 = 1;
                	double i4 = 1;

                    if (k2 != 0 && random.nextDouble() * l3++ == 0)
                    {
                        i4 = k2;
                    }

                    if (l2 != 0 && random.nextDouble() * l3++ == 0)
                    {
                        i4 = l2;
                    }

                    if (i3 != 0 && random.nextDouble() * l3++ == 0)
                    {
                        i4 = i3;
                    }

                    if (j3 != 0 && random.nextDouble() * l3++ == 0)
                    {
                        i4 = j3;
                    }

                    if (random.nextDouble() * 3 == 0)
                    {
                        cache1[j2 + i2 * rangeX] = i4;
                    }

                }
                else if (k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0))
                {

                	cache1[j2 + i2 * rangeX] = k3;
                }
                else
                {
                    cache1[j2 + i2 * rangeX] = k3;
                }
            }
        }

        return cache1;
	}
	
	private double[] zoom(int x, int z, int rangeX, int rangeZ, double[] cache){
		int i1 = x >> 1;
        int j1 = z >> 1;
        int k1 = (rangeX >> 1) + 3;
        int l1 = (rangeZ >> 1) + 3;
        
        double[] cache1 = ExtraBiomesDoubleCache.getDoubleCache(k1 * 2 * l1 * 2);
        int i2 = k1 << 1;
        int j2;

        for (int k2 = 0; k2 < l1 - 1; ++k2)
        {
            j2 = k2 << 1;
            int l2 = j2 * i2;
            double i3 = cache[0 + (k2 + 0) * k1];
            double j3 = cache[0 + (k2 + 1) * k1];

            for (int k3 = 0; k3 < k1 - 1; ++k3)
            {
                this.initChunkSeed((long)(k3 + i1 << 1), (long)(k2 + j1 << 1));
                double l3 = cache[k3 + 1 + (k2 + 0) * k1];
                double i4 = cache[k3 + 1 + (k2 + 1) * k1];
                cache1[l2] = i3;
                cache1[l2++ + i2] = this.choose(i3, j3);
                cache1[l2] = this.choose(i3, l3);
                cache1[l2++ + i2] = this.modeOrRandom(i3, l3, j3, i4);
                i3 = l3;
                j3 = i4;
            }
        }

        double[] cache2 = ExtraBiomesDoubleCache.getDoubleCache(rangeX * rangeZ);

        for (j2 = 0; j2 < rangeZ; ++j2)
        {
            System.arraycopy(cache1, (j2 + (z & 1)) * (k1 << 1) + (x & 1), cache2, j2 * rangeX, rangeX);
        }

        return cache2;
	}
	
	
	/**
     * randomly choose between the two args
     */
    protected double choose(double par1, double par2)
    {
        return (int)random.nextDouble() * 2 == 0 ? par1 : par2;
    }
	
    /**
     * randomly choose between the four args
     */
    protected double choose(double par1, double par2, double par3, double par4)
    {
        double d1 = random.nextDouble() * 4;
        return (int)d1 == 0 ? par1 : ((int)d1 == 1 ? par2 : ((int)d1 == 2 ? par3 : par4));
    }
	
    /**
     * returns the mode (most frequently occuring number) or a random number from the 4 integers provided
     */
    protected double modeOrRandom(double par1, double par2, double par3, double par4)
    {
        if (par2 == par3 && par3 == par4)
        {
            return par2;
        }
        else if (par1 == par2 && par1 == par3)
        {
            return par1;
        }
        else if (par1 == par2 && par1 == par4)
        {
            return par1;
        }
        else if (par1 == par3 && par1 == par4)
        {
            return par1;
        }
        else if (par1 == par2 && par3 != par4)
        {
            return par1;
        }
        else if (par1 == par3 && par2 != par4)
        {
            return par1;
        }
        else if (par1 == par4 && par2 != par3)
        {
            return par1;
        }
        else if (par2 == par1 && par3 != par4)
        {
            return par2;
        }
        else if (par2 == par3 && par1 != par4)
        {
            return par2;
        }
        else if (par2 == par4 && par1 != par3)
        {
            return par2;
        }
        else if (par3 == par1 && par2 != par4)
        {
            return par3;
        }
        else if (par3 == par2 && par1 != par4)
        {
            return par3;
        }
        else if (par3 == par4 && par1 != par2)
        {
            return par3;
        }
        else if (par4 == par1 && par2 != par3)
        {
            return par3;
        }
        else if (par4 == par2 && par1 != par3)
        {
            return par3;
        }
        else if (par4 == par3 && par1 != par2)
        {
            return par3;
        }
        else
        {
            int i1 = this.nextInt(4);
            return i1 == 0 ? par1 : (i1 == 1 ? par2 : (i1 == 2 ? par3 : par4));
        }
    }
	
	/**
     * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
     * argument).
     */
    public void initWorldGenSeed(long seed)
    {
        this.worldGenSeed = seed;
        
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }

    /**
     * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
     */
    public void initChunkSeed(long par1, long par3)
    {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par1;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par3;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par1;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par3;
    }

    /**
     * returns a LCG pseudo random number from [0, x). Args: int x
     */
    protected int nextInt(int par1)
    {
        int j = (int)((this.chunkSeed >> 24) % (long)par1);

        if (j < 0)
        {
            j += par1;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return j;
    }

}
