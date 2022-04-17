package edu.iastate.a309.darkplatform.serialization;

import static edu.iastate.a309.darkplatform.serialization.SerializationWriter.*;

public class DPArray {

    public static final byte CONTAINER_TYPE = ContainerType.ARRAY;
    public short nameLength;
    public byte[] name;
    private int size = 1 + 2 + 4 + 1 + 4;
    public byte type;
    public int count;
    public byte[] data;

    public short[] shortData;
    public char[] charData;
    public int[] intData;
    public long[] longData;
    public float[] floatData;
    public double[] doubleData;
    public boolean[] booleanData;

    private DPArray() {

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
        pointer = writeBytes(destination, pointer, type);
        pointer = writeBytes(destination, pointer, count);

        switch (type) {
            case Type.BYTE:
                pointer = writeBytes(destination, pointer, data);
                break;
            case Type.SHORT:
                pointer = writeBytes(destination, pointer, shortData);
                break;
            case Type.CHAR:
                pointer = writeBytes(destination, pointer, charData);
                break;
            case Type.INT:
                pointer = writeBytes(destination, pointer, intData);
                break;
            case Type.LONG:
                pointer = writeBytes(destination, pointer, longData);
                break;
            case Type.FLOAT:
                pointer = writeBytes(destination, pointer, floatData);
                break;
            case Type.DOUBLE:
                pointer = writeBytes(destination, pointer, doubleData);
                break;
            case Type.BOOLEAN:
                pointer = writeBytes(destination, pointer, booleanData);
                break;
        }
        return pointer;
    }

    public int getSize() {
        return size + nameLength + getDataSize();
    }

    public int getDataSize() {
        switch (type) {
            case Type.BYTE:
                return data.length * Type.getSize(type);
            case Type.SHORT:
                return shortData.length * Type.getSize(type);
            case Type.CHAR:
                return charData.length * Type.getSize(type);
            case Type.INT:
                return intData.length * Type.getSize(type);
            case Type.LONG:
                return longData.length * Type.getSize(type);
            case Type.FLOAT:
                return floatData.length * Type.getSize(type);
            case Type.DOUBLE:
                return doubleData.length * Type.getSize(type);
            case Type.BOOLEAN:
                return booleanData.length * Type.getSize(type);
        }
        return 0;
    }

    public static DPArray createBytes(String name, byte[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.BYTE;
        array.count = values.length;
        array.data = values;
        return array;
    }

    public static DPArray createShorts(String name, short[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.SHORT;
        array.count = values.length;
        array.shortData = values;
        return array;
    }

    public static DPArray createChars(String name, char[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.CHAR;
        array.count = values.length;
        array.charData = values;
        return array;
    }

    public static DPArray createInts(String name, int[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.INT;
        array.count = values.length;
        array.intData = values;
        return array;
    }

    public static DPArray createLongs(String name, long[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.LONG;
        array.count = values.length;
        array.longData = values;
        return array;
    }

    public static DPArray createFloats(String name, float[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.FLOAT;
        array.count = values.length;
        array.floatData = values;
        return array;
    }

    public static DPArray createDoubles(String name, double[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.DOUBLE;
        array.count = values.length;
        array.doubleData = values;
        return array;
    }

    public static DPArray createBooleans(String name, boolean[] values) {
        DPArray array = new DPArray();
        array.setName(name);
        array.type = Type.BOOLEAN;
        array.count = values.length;
        array.booleanData = values;
        return array;
    }

    public static DPArray deserialize(byte[] data, int pointer) {
        if (data[pointer++] != CONTAINER_TYPE) {
            return null;
        }

        DPArray result = new DPArray();

        result.nameLength = readShort(data, pointer);
        pointer += 2;

        result.setName(readString(data, pointer, result.nameLength));
        pointer += result.nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        result.type = data[pointer++];

        result.count = readInt(data, pointer);
        pointer += 4;

        switch (result.type) {
            case Type.BYTE:
                result.data = new byte[result.count];
                readBytes(data, pointer, result.data);
                break;
            case Type.SHORT:
                result.shortData = new short[result.count];
                readShorts(data, pointer, result.shortData);
                break;
            case Type.CHAR:
                result.charData = new char[result.count];
                readChars(data, pointer, result.charData);
                break;
            case Type.INT:
                result.intData = new int[result.count];
                readInts(data, pointer, result.intData);
                break;
            case Type.LONG:
                result.longData = new long[result.count];
                readLongs(data, pointer, result.longData);
                break;
            case Type.FLOAT:
                result.floatData = new float[result.count];
                readFloats(data, pointer, result.floatData);
                break;
            case Type.DOUBLE:
                result.doubleData = new double[result.count];
                readDoubles(data, pointer, result.doubleData);
                break;
            case Type.BOOLEAN:
                result.booleanData = new boolean[result.count];
                readBooleans(data, pointer, result.booleanData);
                break;
        }

        result.size -= (result.nameLength + result.getDataSize());

        return result;
    }
}

