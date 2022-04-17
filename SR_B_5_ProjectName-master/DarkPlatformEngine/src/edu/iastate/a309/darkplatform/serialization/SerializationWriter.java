package edu.iastate.a309.darkplatform.serialization;

import java.nio.ByteBuffer;

public class SerializationWriter {

    public static final byte[] HEADER = "DP".getBytes();

    public static final short VERSION = 0x0100;

    public static int writeBytes(byte[] destination, int pointer, byte value) {
        assert (destination.length > pointer + Type.getSize(Type.BYTE));
        destination[pointer++] = value;
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, byte[] values) {
        assert (destination.length > pointer + Type.getSize(Type.BYTE) * values.length);
        for (int i = 0; i < values.length; i++) {
            destination[pointer++] = values[i];
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, short value) {
        assert (destination.length > pointer + Type.getSize(Type.SHORT));
        destination[pointer++] = (byte) ((value >> 8) & 0xff);
        destination[pointer++] = (byte) ((value) & 0xff);
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, short[] values) {
        assert (destination.length > pointer + Type.getSize(Type.SHORT) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
            pointer += 2;
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, char value) {
        assert (destination.length > pointer + Type.getSize(Type.CHAR));
        destination[pointer++] = (byte) ((value >> 8) & 0xff);
        destination[pointer++] = (byte) ((value) & 0xff);
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, char[] values) {
        assert (destination.length > pointer + Type.getSize(Type.CHAR) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
            pointer += 2;
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, int value) {
        assert (destination.length > pointer + Type.getSize(Type.INT));
        destination[pointer++] = (byte) ((value >> 24) & 0xff);
        destination[pointer++] = (byte) ((value >> 16) & 0xff);
        destination[pointer++] = (byte) ((value >> 8) & 0xff);
        destination[pointer++] = (byte) ((value) & 0xff);
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, int[] values) {
        assert (destination.length > pointer + Type.getSize(Type.INT) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
            pointer += 4;
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, long value) {
        assert (destination.length > pointer + Type.getSize(Type.LONG));
        destination[pointer++] = (byte) ((value >> 56) & 0xff);
        destination[pointer++] = (byte) ((value >> 48) & 0xff);
        destination[pointer++] = (byte) ((value >> 40) & 0xff);
        destination[pointer++] = (byte) ((value >> 32) & 0xff);
        destination[pointer++] = (byte) ((value >> 24) & 0xff);
        destination[pointer++] = (byte) ((value >> 16) & 0xff);
        destination[pointer++] = (byte) ((value >> 8) & 0xff);
        destination[pointer++] = (byte) ((value) & 0xff);
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, long[] values) {
        assert (destination.length > pointer + Type.getSize(Type.LONG) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
            pointer += 8;
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, float value) {
        assert (destination.length > pointer + Type.getSize(Type.FLOAT));
        int data = Float.floatToIntBits(value);
        return writeBytes(destination, pointer, data);
    }

    public static int writeBytes(byte[] destination, int pointer, float[] values) {
        assert (destination.length > pointer + Type.getSize(Type.FLOAT) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
            pointer += 4;
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, double value) {
        assert (destination.length > pointer + Type.getSize(Type.DOUBLE));
        long data = Double.doubleToLongBits(value);
        return writeBytes(destination, pointer, data);
    }

    public static int writeBytes(byte[] destination, int pointer, double[] values) {
        assert (destination.length > pointer + Type.getSize(Type.DOUBLE) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
            pointer += 8;
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, boolean value) {
        assert (destination.length > pointer + Type.getSize(Type.BOOLEAN));
        destination[pointer++] = (byte) (value ? 1 : 0);
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, boolean[] values) {
        assert (destination.length > pointer + Type.getSize(Type.BOOLEAN) * values.length);
        for (int i = 0; i < values.length; i++) {
            writeBytes(destination, pointer, values[i]);
        }
        return pointer;
    }

    public static int writeBytes(byte[] destination, int pointer, String string) {
        pointer = writeBytes(destination, pointer, (short) string.length());
        pointer = writeBytes(destination, pointer, string.getBytes());
        return pointer;
    }

    public static byte readByte(byte[] src, int pointer) {
        return src[pointer];
    }

    public static void readBytes(byte[] src, int pointer, byte[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = src[pointer + i];
        }
    }

    public static short readShort(byte[] src, int pointer) {
        return ByteBuffer.wrap(src, pointer, 2).getShort();
    }

    public static void readShorts(byte[] src, int pointer, short[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readShort(src, pointer);
            pointer += Type.getSize(Type.SHORT);
        }
    }

    public static char readChar(byte[] src, int pointer) {
        return ByteBuffer.wrap(src, pointer, 2).getChar();
    }

    public static void readChars(byte[] src, int pointer, char[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readChar(src, pointer);
            pointer += Type.getSize(Type.CHAR);
        }
    }

    public static int readInt(byte[] src, int pointer) {
        return ByteBuffer.wrap(src, pointer, 4).getInt();
    }

    public static void readInts(byte[] src, int pointer, int[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readInt(src, pointer);
            pointer += Type.getSize(Type.INT);
        }
    }

    public static long readLong(byte[] src, int pointer) {
        return ByteBuffer.wrap(src, pointer, 8).getLong();
    }

    public static void readLongs(byte[] src, int pointer, long[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readLong(src, pointer);
            pointer += Type.getSize(Type.LONG);
        }
    }

    public static float readFloat(byte[] src, int pointer) {
        return Float.intBitsToFloat(readInt(src, pointer));
    }

    public static void readFloats(byte[] src, int pointer, float[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readFloat(src, pointer);
            pointer += Type.getSize(Type.FLOAT);
        }
    }

    public static double readDouble(byte[] src, int pointer) {
        return Double.longBitsToDouble(readLong(src, pointer));
    }

    public static void readDoubles(byte[] src, int pointer, double[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readDouble(src, pointer);
            pointer += Type.getSize(Type.DOUBLE);
        }
    }

    public static boolean readBoolean(byte[] src, int pointer) {
        return src[pointer] != 0;
    }

    public static void readBooleans(byte[] src, int pointer, boolean[] dest) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = readBoolean(src, pointer);
            pointer += Type.getSize(Type.BOOLEAN);
        }
    }

    public static String readString(byte[] src, int pointer, int length) {
        return new String(src, pointer, length);
    }
}
