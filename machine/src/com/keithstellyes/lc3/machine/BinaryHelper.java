package com.keithstellyes.lc3.machine;

public class BinaryHelper {
    public static final int EIGHT_ONES = 0b11111111;

    public static byte getNthBit(int datum, byte nthBit) {
        int mask = 1 << nthBit;
        return (byte) (datum & mask);
    }

    public static int setNthBit(int datum, byte nthBit, boolean bit) {
        if(bit == true) {
            return setNthBit(datum, nthBit, (byte) 1);
        } else {
            return setNthBit(datum, nthBit, (byte) 0);
        }
    }

    public static int setNthBit(int datum, byte nthBit, byte bit) {
        bit = (byte) (0b1 & bit);
        return datum | (bit << nthBit);
    }

    public static byte getLeftByte(short datum) {
        return (byte) (datum >> 8);
    }

    public static byte getRightByte(short datum) {
        return (byte) datum;
    }
}
