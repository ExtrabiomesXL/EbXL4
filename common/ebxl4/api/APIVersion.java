package ebxl4.api;

/*
 *  IF you include any of the EbXL++ API classes make sure to include this file,
 *  as we use it to detect if a mod using an out dated API.
 *  
 *  The modID field is optional, but if you do fill it and your mod accidently replaces
 *  the EbXL++ API, EbXL++ will be able to tell users which mods are using out dated api's.
 *  
 *  Alternatively you can use the 'dependencies="after:EbXL++"' property for the @Mod
 *  annotation and have fore make sure you don't overwrite the EbXL++ API.
 */
public class APIVersion {
  public static final String version = "0.0.1.16";
  public static final String modId = "EbXL++";
}
