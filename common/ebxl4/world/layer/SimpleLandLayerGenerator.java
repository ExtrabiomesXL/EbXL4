// Using a single 

package ebxl4.world.layer;

import java.util.Random;

import ebxl4.world.noise.SimplexNoise;
import argo.jdom.JsonRootNode;

public class SimpleLandLayerGenerator extends BaseLandLayerGenerator {
  
  private int octaves = 8;
  private double persistance = 0.76D;
  private double frequency = 2.125D;
  private double detail = 20480D;
  
  private double balance;
  private double [] offsetValues;
  
  private SimplexNoise noise;
  
  public SimpleLandLayerGenerator(Random rnd) {
    super(rnd);
    
    // Create the noise generator
    noise = new SimplexNoise(seed.nextLong());

    // Create the list of offets for the octives
    offsetValues = new double[octaves];
    for(int octave = 0; octave < octaves; octave++) {
      offsetValues[octave] = seed.nextDouble();
    }
    
    // Create the area where water can spawn
    balance = 0D;
  }
  
  public SimpleLandLayerGenerator(Random rnd, JsonRootNode options) {
    super(rnd);
    
    // Create the noise generator
    noise = new SimplexNoise(seed.nextLong());
    double zoom = 1D;
    
    // Read the settings
    if(options.isNumberValue("landLayer", "oct")) octaves = Integer.parseInt(options.getNumberValue("landLayer", "oct"));
    if(options.isNumberValue("landLayer", "bal")) balance = Double.parseDouble(options.getNumberValue("landLayer", "bal"));
    if(options.isNumberValue("landLayer", "pers")) persistance = Double.parseDouble(options.getNumberValue("landLayer", "pers"));
    if(options.isNumberValue("landLayer", "freq")) frequency = Double.parseDouble(options.getNumberValue("landLayer", "freq"));
    if(options.isNumberValue("landLayer", "det")) detail = Double.parseDouble(options.getNumberValue("landLayer", "det"));
    if(options.isNumberValue("zoom")) zoom = Double.parseDouble(options.getNumberValue("zoom"));
    
    // make sure settings are in range
    octaves = (octaves >= 1) ? ((octaves <= 12) ? octaves : 12) : 1;
    balance = (balance >= -1D) ? ((balance <= 1D) ? balance : 1D) : -1D;
    persistance = (persistance >= 0.01D) ? ((persistance <= 0.99D) ? persistance : 0.99D) : 0.01D;
    frequency = (frequency >= 1.5D) ? ((frequency <= 7D) ? frequency : 7D) : 1.5D;
    detail = (detail >= 128D) ? ((detail <= 32768D) ? detail : 32768D) : 128D;
    zoom = (zoom >= 0.015625D) ? ((zoom <= 8D) ? zoom : 8D) : 0.015625D;
    
    // Adjust the detail based on zoom
    detail *= zoom;
    
    // Create the list of offets for the octives
    offsetValues = new double[octaves];
    for(int octave = 0; octave < octaves; octave++) {
      offsetValues[octave] = seed.nextDouble() * zoom;
    }
  }
  
  @Override
  public boolean Generate(double posX, double posY) {
    // Starting constants
    double pow = 1D;
    double val = 0D;
    double detail1 = detail;
    
    // Calculate the level
    for(int octave = 0; octave < octaves; octave++) {
      double offset = (octave % 2 == 0) ? offsetValues[octave] : -offsetValues[octave];
      val += noise.noise(((posX) / detail1) + offset, ((posY) / detail1) - offset) * pow;
      detail1 /= frequency;
      pow *= persistance;
    }
    
    if(val < balance) {
        return false;
    } else {
      return true;
    }
  }

}
