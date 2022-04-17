package edu.iastate.a309.darkplatform.serialization;

import java.util.ArrayList;
import java.util.List;

import static edu.iastate.a309.darkplatform.serialization.SerializationWriter.*;

public class DPObject {

    public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
    public short nameLength;
    public byte[] name;
    private int size = 1 + 2 + 4 + 4 + 4 + 4;

    public List<DPField> fields = new ArrayList<DPField>();
    public List<DPString> strings = new ArrayList<DPString>();
    public List<DPArray> arrays = new ArrayList<DPArray>();

    public DPObject(String name) {
        setName(name);
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

        pointer = writeBytes(destination, pointer, fields.size());
        for (DPField field : fields) {
            pointer = field.getBytes(destination, pointer);
        }
        pointer = writeBytes(destination, pointer, arrays.size());
        for (DPArray array : arrays) {
            pointer = array.getBytes(destination, pointer);
        }
        pointer = writeBytes(destination, pointer, strings.size());
        for (DPString string : strings) {
            pointer = string.getBytes(destination, pointer);
        }

        return pointer;
    }

    public void addArray(DPArray array) {
        arrays.add(array);
        size += array.getSize();
    }

    public void addField(DPField field) {
        fields.add(field);
        size += field.getSize();
    }

    public void addString(DPString string) {
        strings.add(string);
        size += string.getSize();
    }

    public int getSize() {
        return size + nameLength;
    }

    public static DPObject deserialize(byte[] data, int pointer) {
        if (data[pointer++] != CONTAINER_TYPE) {
            return null;
        }

        int nameLength = readShort(data, pointer);
        pointer += 2;

        DPObject result = new DPObject(readString(data, pointer, nameLength));
        pointer += nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        int fields = readInt(data, pointer);
        pointer += 4;

        for (int i = 0; i < fields; i++) {
            DPField field = DPField.deserialize(data, pointer);
            result.fields.add(field);
            pointer += field.getSize();
        }

        int arrays = readInt(data, pointer);
        pointer += 4;

        for (int i = 0; i < arrays; i++) {
            DPArray array = DPArray.deserialize(data, pointer);
            result.arrays.add(array);
            pointer += array.getSize();
        }

        int strings = readInt(data, pointer);
        pointer += 4;

        for (int i = 0; i < strings; i++) {
            DPString string = DPString.deserialize(data, pointer);
            result.strings.add(string);
            pointer += string.getSize();
        }

        result.size -= nameLength;
        return result;
    }
}
