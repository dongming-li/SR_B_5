package edu.iastate.a309.darkplatform.serialization;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerializationTest {

    public static void printFile(byte[] data, String path) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
            stream.write(data);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serializeTest() {
        int[] ints = new int[]{10, 15, 142, 0, 2, 5};
        DPArray array = DPArray.createInts("Many numbahs", ints);
        DPField field = DPField.createLong("Long value", 13);
        DPString string = DPString.create("Best string", "Something Important");

        DPObject object = new DPObject("Swagboi360");
        object.addArray(array);
        object.addField(field);
        object.addString(string);
        object.addString(string);

        DPDatabase database = new DPDatabase("Store all ya stuff here");
        database.addObject(object);
        database.addObject(object);

        int pointer = 0;
        byte[] test = new byte[database.getSize()];
        pointer = database.getBytes(test, pointer);

        printFile(test, "database.dpd");
    }

    public static void deserializeTest() {
        DPDatabase database = DPDatabase.deserialize("database.dpd");

        System.out.println("Database: " + database.getName());

        for (DPObject object : database.objects) {
            System.out.println("\tObject: " + object.getName());
            for (DPField field : object.fields) {
                System.out.println("\t\tField: " + field.getName());
            }
            for (DPArray array : object.arrays) {
                System.out.println("\t\tArray: " + array.getName());
            }
            for (DPString string : object.strings) {
                System.out.println("\t\tString: " + string.getName());
            }

        }
    }

    public static void main(String[] args) {
        deserializeTest();
    }
}
