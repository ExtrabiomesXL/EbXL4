package net.extrabiomes.generation.layers;

import java.util.Random;

import net.extrabiomes.generation.ExtraBiomesDoubleCache;
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
		
	}
	
	private double[] zoom(int x, int z, int rangeX, int rangeZ, double[] cache){
		
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
