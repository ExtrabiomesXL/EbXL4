package ebxl4.lib;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Version implements Comparable<Version>{
  private int major = 0;
  private int minor = 0;
  private int maintenance = 0;
  private int build = 0;
  
  public Version(String ver) {
    String[] parts = ver.split("\\.");
    
    if(parts[0].length() < 1) return;
    major = Integer.parseInt(parts[0]);
    
    if(parts.length < 2 && parts[1].length() < 1) return;
    minor = Integer.parseInt(parts[1]);
    
    if(parts.length < 3 && parts[2].length() < 1) return;
    maintenance = Integer.parseInt(parts[2]);

    if(parts.length < 4 && parts[3].length() < 1) return;
    build = Integer.parseInt(parts[3]);
  }
  
  public int getMajor() {
    return major;
  }
  
  public int getMinor() {
    return minor;
  }
  
  public int getMaintenance() {
    return maintenance;
  }
  
  public int getBuild() {
    return build;
  }
  
  public String getVersion() {
    return major + "." + minor + "." + maintenance + "." + build;
  }
  
  public void setMajor(int maj) {
    major = maj;
  }
  
  public void setMinor(int min) {
    minor = min;
  }
  
  public void setMaintenance(int main) {
    maintenance = main;
  }
  
  public void setBuild(int bld) {
    build = bld;
  }
  
  public void setVersion(String ver) {
    String[] parts = ver.split("\\.");
    
    if(parts.length < 1 && parts[0].length() < 1) {
      major = 0;
    } else {
      major = Integer.parseInt(parts[0]);
    }
    
    if(parts.length < 2 && parts[1].length() < 1) {
      minor = 0;
    } else {
      minor = Integer.parseInt(parts[1]);
    }
    
    if(parts.length < 3 && parts[2].length() < 1) {
      maintenance = 0;
    } else {
      maintenance = Integer.parseInt(parts[2]);
    }
    
    if(parts.length < 4 && parts[3].length() < 1) {
      build = 0;
    } else {
      build = Integer.parseInt(parts[3]);
    }
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (other == this) return true;
    if (!(other instanceof Version)) return false;
    Version otherMyClass = (Version) other;
  
    if (otherMyClass.major == this.major && otherMyClass.minor == this.minor && otherMyClass.maintenance == this.maintenance && otherMyClass.build == this.build) return true;
    return false;
  }
    
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(major).append(minor).append(maintenance).append(build).toHashCode();
  }

  @Override
  public int compareTo(Version other) {
    // Check the major version
    if(other.major != this.major) return this.major - other.major;
    if(other.minor != this.minor) return this.minor - other.minor;
    if(other.maintenance != this.maintenance) return this.maintenance - other.maintenance;
    if(other.build != this.build) return this.build - other.build;
    
    // The two versions are the same
    return 0;
  }

}
