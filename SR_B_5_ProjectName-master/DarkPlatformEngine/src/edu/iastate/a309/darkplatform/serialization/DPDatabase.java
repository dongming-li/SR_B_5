package edu.iastate.a309.darkplatform.serialization;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.iastate.a309.darkplatform.serialization.SerializationWriter.*;

public class DPDatabase {

    public static final byte[] HEADER = "DPDB".getBytes();

    public byte[] name;
    public short nameLength;

    private int size = 4 + 1 + 2 + 4 + 4;

    public static final byte CONTAINER_TYPE = ContainerType.DATABASE;

    public List<DPObject> objects = new ArrayList<DPObject>();

    public DPDatabase(String name) {
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
        pointer = writeBytes(destination, pointer, HEADER);
        pointer = writeBytes(destination, pointer, CONTAINER_TYPE);
        pointer = writeBytes(destination, pointer, nameLength);
        pointer = writeBytes(destination, pointer, name);
        pointer = writeBytes(destination, pointer, getSize());

        pointer = writeBytes(destination, pointer, objects.size());
        for (DPObject object : objects) {
            pointer = object.getBytes(destination, pointer);
        }

        return pointer;
    }

    public void addObject(DPObject object) {
        objects.add(object);
        size += object.getSize();
    }

    public int getSize() {
        return size + nameLength;
    }

    public static DPDatabase deserialize(byte[] data) {
        int pointer = 0;

        if (data.length < 4) return null;
        String header = readString(data, pointer, HEADER.length);
        pointer += 4;

        byte containerType = readByte(data, pointer++);

        int nameLength = readShort(data, pointer);
        pointer += 2;

        DPDatabase result = new DPDatabase(readString(data, pointer, nameLength));
        pointer += nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        int objCount = readInt(data, pointer);
        pointer += 4;

        for (int i = 0; i < objCount; i++) {
            DPObject object = DPObject.deserialize(data, pointer);
            result.objects.add(object);
            if (object == null) {
                continue;
            }
            pointer += object.getSize();
        }

        return result;
    }

    public static DPDatabase deserialize(String path) {
        byte[] data = null;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
            data = new byte[stream.available()];
            stream.read(data);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deserialize(data);
    }
}
