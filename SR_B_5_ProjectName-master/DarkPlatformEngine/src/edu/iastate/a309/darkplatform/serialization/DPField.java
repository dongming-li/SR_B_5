package edu.iastate.a309.darkplatform.serialization;

import static edu.iastate.a309.darkplatform.serialization.SerializationWriter.*;

public class DPField {

    public byte[] name;
    public short nameLength;

    public byte type;
    public byte[] data;

    public static final byte CONTAINER_TYPE = ContainerType.FEILD;

    private DPField() {

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
        pointer = SerializationWriter.writeBytes(destination, pointer, CONTAINER_TYPE);
        pointer = SerializationWriter.writeBytes(destination, pointer, nameLength);
        pointer = SerializationWriter.writeBytes(destination, pointer, name);
        pointer = SerializationWriter.writeBytes(destination, pointer, type);
        pointer = SerializationWriter.writeBytes(destination, pointer, data);

        return pointer;
    }

    public int getSize() {
        assert (data.length == Type.getSize(type));
        return 1 + 2 + nameLength + 1 + data.length;
    }

    public static DPField createByte(String name, byte value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.BYTE;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createShort(String name, short value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.SHORT;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createChar(String name, char value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.CHAR;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createInt(String name, int value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.INT;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createLong(String name, long value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.LONG;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createFloat(String name, float value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.FLOAT;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createDouble(String name, double value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.DOUBLE;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField createBoolean(String name, boolean value) {
        DPField field = new DPField();
        field.setName(name);
        field.type = Type.BOOLEAN;
        field.data = new byte[Type.getSize(field.type)];
        writeBytes(field.data, 0, value);
        return field;
    }

    public static DPField deserialize(byte[] data, int pointer) {
        if (data[pointer++] != CONTAINER_TYPE) {
            return null;
        }

        DPField result = new DPField();

        result.nameLength = readShort(data, pointer);
        pointer += 2;

        result.setName(readString(data, pointer, result.nameLength));
        pointer += result.nameLength;

        result.type = data[pointer++];

        result.data = new byte[Type.getSize(result.type)];
        readBytes(data, pointer, result.data);

        return result;
    }
}
