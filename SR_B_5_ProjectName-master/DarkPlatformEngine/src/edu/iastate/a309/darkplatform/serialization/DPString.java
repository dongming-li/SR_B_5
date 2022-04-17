package edu.iastate.a309.darkplatform.serialization;

import static edu.iastate.a309.darkplatform.serialization.SerializationWriter.*;

public class DPString {

    public static final byte CONTAINER_TYPE = ContainerType.STRING;
    public short nameLength;
    public byte[] name;
    private int size = 1 + 2 + 4 + 4;
    private int count = 0;
    public char[] characters;

    private DPString() {

    }

    public void setName(String name) {
        assert (name.length() < Short.MAX_VALUE);

        nameLength = (short) name.length();
        this.name = name.getBytes();
    }

    public String getName() {
        return new String(name, 0, nameLength);
    }


    public int getBytes(byte[] destination, int pointer) {
        pointer = writeBytes(destination, pointer, CONTAINER_TYPE);
        pointer = writeBytes(destination, pointer, nameLength);
        pointer = writeBytes(destination, pointer, name);
        pointer = writeBytes(destination, pointer, getSize());
        pointer = writeBytes(destination, pointer, count);
        pointer = writeBytes(destination, pointer, characters);
        return pointer;
    }

    public int getSize() {
        return size + nameLength + getDataSize();
    }

    public int getDataSize() {
        return characters.length * Type.getSize(Type.CHAR);
    }

    public static DPString create(String name, String data) {
        DPString string = new DPString();
        string.count = data.length();
        string.characters = data.toCharArray();
        string.setName(name);
        return string;
    }

    public static DPString deserialize(byte[] data, int pointer) {
        if (data[pointer++] != CONTAINER_TYPE) {
            return null;
        }

        DPString result = new DPString();

        result.nameLength = readShort(data, pointer);
        pointer += 2;

        result.setName(readString(data, pointer, result.nameLength));
        pointer += result.nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        result.count = readInt(data, pointer);
        pointer += 4;

        result.characters = new char[Type.getSize(Type.CHAR)];
        readChars(data, pointer, result.characters);

        result.size -= (result.nameLength + result.getDataSize());
        return result;
    }
}
