
package net.extrabiomes.terraincontrol.util;

import java.lang.reflect.Field;
import java.util.Map;

import net.extrabiomes.terraincontrol.configuration.Tag;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;


public class NBTHelper
{
    /**
     * Creates a net.minecraft.server.NBTBase tag. Doesn't work for
     * ends, lists and compounds.
     * 
     * @param type
     * @param name
     * @param value
     * @return
     */
    private static NBTBase createTagNms(final Tag.Type type, final String name, final Object value)
    {
        switch (type)
        {
            case TAG_Byte:
                return new NBTTagByte(name, (Byte) value);
            case TAG_Short:
                return new NBTTagShort(name, (Short) value);
            case TAG_Int:
                return new NBTTagInt(name, (Integer) value);
            case TAG_Long:
                return new NBTTagLong(name, (Long) value);
            case TAG_Float:
                return new NBTTagFloat(name, (Float) value);
            case TAG_Double:
                return new NBTTagDouble(name, (Double) value);
            case TAG_Byte_Array:
                return new NBTTagByteArray(name, (byte[]) value);
            case TAG_String:
                return new NBTTagString(name, (String) value);
            case TAG_Int_Array:
                return new NBTTagIntArray(name, (int[]) value);
            default:
                // Cannot make this into a tag
                throw new IllegalArgumentException(type + "doesn't have a simple value!");
        }
    }

    /**
     * Converts a net.minecraft.server compound NBT tag to a
     * net.minecraftwiki.wiki.NBTClass NBT compound tag.
     * 
     * @param nmsTag
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Tag getNBTFromNMSTagCompound(final NBTTagCompound nmsTag) {
        final Tag compoundTag = new Tag(Tag.Type.TAG_Compound, nmsTag.getName(),
                new Tag[] { new Tag(Tag.Type.TAG_End, null, null) });

        // Get the child tags using some reflection magic
        Field mapField;
        Map nmsChildTags = null;
        try {
            mapField = NBTTagCompound.class.getDeclaredField("map");
            mapField.setAccessible(true);
            nmsChildTags = (Map) mapField.get(nmsTag);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        if (nmsChildTags == null) // Cannot load the tag, return an
                                  // empty tag
            return compoundTag;

        // Add all child tags to the compound tag
        for (final Object nmsChildTagName : nmsChildTags.keySet()) {
            final NBTBase nmsChildTag = (NBTBase) nmsChildTags.get(nmsChildTagName);
            final Tag.Type type = Tag.Type.values()[nmsChildTag.getId()];
            switch (type)
            {
                case TAG_End:
                    break;
                case TAG_Byte:
                case TAG_Short:
                case TAG_Int:
                case TAG_Long:
                case TAG_Float:
                case TAG_Double:
                case TAG_Byte_Array:
                case TAG_String:
                case TAG_Int_Array:
                    compoundTag.addTag(new Tag(type, nmsChildTag.getName(),
                            getValueFromNms(nmsChildTag)));
                    break;
                case TAG_List:
                    final Tag listChildTag = getNBTFromNMSTagList((NBTTagList) nmsChildTag);
                    if (listChildTag != null) compoundTag.addTag(listChildTag);
                    break;
                case TAG_Compound:
                    compoundTag.addTag(getNBTFromNMSTagCompound((NBTTagCompound) nmsChildTag));
                    break;
                default:
                    break;
            }
        }

        return compoundTag;
    }

    // Internal methods below

    /**
     * Converts a net.minecraft.server list NBT tag to a
     * net.minecraftwiki.wiki.NBTClass NBT list tag.
     * 
     * @param nmsListTag
     * @return
     */
    private static Tag getNBTFromNMSTagList(final NBTTagList nmsListTag) {
        if (nmsListTag.tagCount() == 0) // Nothing to return
            return null;

        final Tag.Type listType = Tag.Type.values()[nmsListTag.tagAt(0).getId()];
        final Tag listTag = new Tag(nmsListTag.getName(), listType);

        // Add all child tags
        for (int i = 0; i < nmsListTag.tagCount(); i++) {
            final NBTBase nmsChildTag = nmsListTag.tagAt(i);
            switch (listType)
            {
                case TAG_End:
                    break;
                case TAG_Byte:
                case TAG_Short:
                case TAG_Int:
                case TAG_Long:
                case TAG_Float:
                case TAG_Double:
                case TAG_Byte_Array:
                case TAG_String:
                case TAG_Int_Array:
                    listTag.addTag(new Tag(listType, nmsChildTag.getName(),
                            getValueFromNms(nmsChildTag)));
                    break;
                case TAG_List:
                    final Tag listChildTag = getNBTFromNMSTagList((NBTTagList) nmsChildTag);
                    if (listChildTag != null) listTag.addTag(listChildTag);
                    break;
                case TAG_Compound:
                    listTag.addTag(getNBTFromNMSTagCompound((NBTTagCompound) nmsChildTag));
                    break;
                default:
                    break;
            }
        }
        return listTag;
    }

    /**
     * Converts a net.minecraftwiki.wiki.NBTClass NBT compound tag into
     * an net.minecraft.server NBT compound tag.
     * 
     * @param compoundTag
     * @return
     */
    public static NBTTagCompound getNMSFromNBTTagCompound(final Tag compoundTag) {
        final NBTTagCompound nmsTag = new NBTTagCompound(compoundTag.getName());
        final Tag[] childTags = (Tag[]) compoundTag.getValue();
        for (final Tag tag : childTags)
            switch (tag.getType())
            {
                case TAG_End:
                    break;
                case TAG_Byte:
                case TAG_Short:
                case TAG_Int:
                case TAG_Long:
                case TAG_Float:
                case TAG_Double:
                case TAG_Byte_Array:
                case TAG_String:
                case TAG_Int_Array:
                    nmsTag.setTag(tag.getName(),
                            createTagNms(tag.getType(), tag.getName(), tag.getValue()));
                    break;
                case TAG_List:
                    nmsTag.setTag(tag.getName(), getNMSFromNBTTagList(tag));
                    break;
                case TAG_Compound:
                    nmsTag.setTag(tag.getName(), getNMSFromNBTTagCompound(tag));
                    break;
                default:
                    break;
            }
        return nmsTag;
    }

    /**
     * Converts a net.minecraftwiki.wiki.NBTClass NBT list tag into an
     * net.minecraft.server NBT list tag.
     * 
     * @param listTag
     * @return
     */
    private static NBTTagList getNMSFromNBTTagList(final Tag listTag) {
        final NBTTagList nmsTag = new NBTTagList(listTag.getName());
        final Tag[] childTags = (Tag[]) listTag.getValue();
        for (final Tag tag : childTags)
            switch (tag.getType())
            {
                case TAG_End:
                    break;
                case TAG_Byte:
                case TAG_Short:
                case TAG_Int:
                case TAG_Long:
                case TAG_Float:
                case TAG_Double:
                case TAG_Byte_Array:
                case TAG_String:
                case TAG_Int_Array:
                    nmsTag.appendTag(createTagNms(tag.getType(), tag.getName(), tag.getValue()));
                    break;
                case TAG_List:
                    nmsTag.appendTag(getNMSFromNBTTagList(tag));
                    break;
                case TAG_Compound:
                    nmsTag.appendTag(getNMSFromNBTTagCompound(tag));
                    break;
                default:
                    break;
            }
        return nmsTag;
    }

    /**
     * Gets the value from a nms tag (since that object doesn't have a
     * simple value field)
     * 
     * @param nmsTag
     * @return
     */
    private static Object getValueFromNms(final NBTBase nmsTag) {
        final Tag.Type type = Tag.Type.values()[nmsTag.getId()];
        switch (type)
        {
            case TAG_Byte:
                return ((NBTTagByte) nmsTag).data;
            case TAG_Short:
                return ((NBTTagShort) nmsTag).data;
            case TAG_Int:
                return ((NBTTagInt) nmsTag).data;
            case TAG_Long:
                return ((NBTTagLong) nmsTag).data;
            case TAG_Float:
                return ((NBTTagFloat) nmsTag).data;
            case TAG_Double:
                return ((NBTTagDouble) nmsTag).data;
            case TAG_Byte_Array:
                return ((NBTTagByteArray) nmsTag).byteArray;
            case TAG_String:
                return ((NBTTagString) nmsTag).data;
            case TAG_Int_Array:
                return ((NBTTagIntArray) nmsTag).intArray;
            default:
                // Cannot read this from a tag
                throw new IllegalArgumentException(type + "doesn't have a simple value!");
        }
    }
}
