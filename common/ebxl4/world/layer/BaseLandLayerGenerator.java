package ebxl4.world.layer;

import java.util.Random;

public abstract class BaseLandLayerGenerator {
  
  protected Random seed;
  
  public BaseLandLayerGenerator(Random rnd) {
    this.seed = rnd;
  }
  
  public abstract boolean Generate(double posX, double posY);
}
