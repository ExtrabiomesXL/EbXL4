package net.extrabiomes.generation.layers;

import net.extrabiomes.terraincontrol.SingleWorld;

public class LayerManager {
	
	public TemperatureLayer temperatureLayer;
	
	public FeatureLayer featureLayer;
	
	public LayerManager(SingleWorld world){
		
		long seed = world.getSeed();
		
		temperatureLayer = new TemperatureLayer(seed);
		featureLayer = new FeatureLayer(seed);
		
	}
	
	

}
