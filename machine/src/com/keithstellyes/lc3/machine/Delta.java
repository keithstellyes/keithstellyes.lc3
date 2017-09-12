package com.keithstellyes.lc3.machine;

import java.util.*;

public class Delta {
    public static final int DELTA_BYTE_LEN = 5;
    private static final int[] INDEX_MASKS = {BinaryHelper.EIGHT_ONES << (8 * 3),
                                              BinaryHelper.EIGHT_ONES << (8 * 2),
                                              BinaryHelper.EIGHT_ONES << (8 * 1),
                                              BinaryHelper.EIGHT_ONES << (8 * 0)};

    private final int index;
    private final byte value;

    public Delta(int index, byte value) {
        this.index = index;
        this.value = value;
    }

    public byte[] toDeltaBytes() {
        byte[] deltaBytes = new byte[DELTA_BYTE_LEN];
        deltaBytes[0] = (byte) ((index & INDEX_MASKS[0]) >>> (8 * 3));
        deltaBytes[1] = (byte) ((index & INDEX_MASKS[1]) >>> (8 * 2));
        deltaBytes[2] = (byte) ((index & INDEX_MASKS[2]) >>> (8 * 1));
        deltaBytes[3] = (byte) ((index & INDEX_MASKS[3]) >>> (8 * 0));
        deltaBytes[4] = value;

        return deltaBytes;
    }

    public static byte[] deltaBytesFromDeltaArray(Delta[] deltas) {
        byte[] deltaBytes = new byte[deltas.length * DELTA_BYTE_LEN];
        int deltaBytesIndex = 0;

        for(Delta delta : deltas) {
            byte[] thisDeltaBytes = delta.toDeltaBytes();
            deltaBytes[deltaBytesIndex++] = thisDeltaBytes[0];
            deltaBytes[deltaBytesIndex++] = thisDeltaBytes[1];
            deltaBytes[deltaBytesIndex++] = thisDeltaBytes[2];
            deltaBytes[deltaBytesIndex++] = thisDeltaBytes[3];
            deltaBytes[deltaBytesIndex++] = thisDeltaBytes[4];
        }

        return deltaBytes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Delta(");
        sb.append(Integer.toHexString(value));
        sb.append("@");
        sb.append(Integer.toString(index));
        sb.append(")");

        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Delta)) {
            return false;
        }

        Delta delta = (Delta) other;
        return index == delta.index && value == delta.value;
    }

    // delta indices are little-endian
    // [index3, index2, index1, index0, byte]
    public static Delta[] parseDeltas(byte[] deltaBytes) {
        DeltaIterator deltaIterator = new DeltaIterator(deltaBytes);
        Delta results[] = new Delta[deltaIterator.size()];
        for(int i = 0; i < deltaIterator.size(); i++) {
            results[i] = deltaIterator.next();
        }

        return results;
    }

    public static int getDeltaIndexFromDeltas(byte[] deltaBytes, int byteIndex) {
        // Need to do the 0xff... masks at the end because of Java's signed integer
        // If we were using C, we would've just use unsigned integers

        return  (deltaBytes[byteIndex + 0] << (8 * 3) & 0xff000000) |
                (deltaBytes[byteIndex + 1] << (8 * 2) & 0x00ff0000) |
                (deltaBytes[byteIndex + 2] << (8 * 1) & 0x0000ff00) |
                (deltaBytes[byteIndex + 3] << (8 * 0) & 0x000000ff);
    }

    public static byte getValueAtIndexAfterDelta(byte[] data, byte[] deltaBytes, int index) {
        Delta[] deltas = parseDeltas(deltaBytes);

        for(Delta delta : deltas) {
            if(delta.index == index) {
                return delta.value;
            }
        }

        return data[index];
    }

    public static Map buildDeltaMap(Iterable<Delta> deltaIterable) {
        Map<Integer, Byte> map = new HashMap<>();
        for(Delta delta : deltaIterable) {
            map.put(delta.index, delta.value);
        }

        return map;
    }

    public static byte[] serializeDeltaMap(Map<Integer, Byte> map) {
        Set<Integer> indices = map.keySet();
        byte[] deltaBytes = new byte[indices.size() * DELTA_BYTE_LEN];
        int deltaBytesIndex = 0;

        for(Integer index : indices) {
            byte[] bytesToAdd = new Delta(index, map.get(index)).toDeltaBytes();

            for(byte b : bytesToAdd) {
                deltaBytes[deltaBytesIndex++] = b;
            }
        }

        return deltaBytes;
    }

    public static byte[] dataAfterDeltas(byte[] data, byte[] deltaBytes) {
        return dataAfterDeltas(data, parseDeltas(deltaBytes));
    }

    public static byte[] dataAfterDeltas(byte[] data, Delta[] deltas) {
        data = data.clone();

        for(Delta delta : deltas) {
            data[delta.index] = delta.value;
        }

        return data;
    }

    public static class DeltaIterator implements Iterator<Delta>, Iterable<Delta> {
        private final byte[] deltaBytes;
        private int deltaByteIndex = 0;

        public DeltaIterator(byte[] deltaBytes) {
            if(deltaBytes.length % DELTA_BYTE_LEN != 0) {
                throw new IllegalArgumentException("Delta byte stream incorrectly formatted.");
            }

            this.deltaBytes = deltaBytes;
        }

        @Override
        public boolean hasNext() {
            return deltaByteIndex * DELTA_BYTE_LEN < deltaBytes.length;
        }

        @Override
        public Delta next() {
            int index = getDeltaIndexFromDeltas(deltaBytes, deltaByteIndex);
            byte value = deltaBytes[deltaByteIndex + 4];

            deltaByteIndex += DELTA_BYTE_LEN;
            return new Delta(index, value);
        }

        public int size() {
            return deltaBytes.length / DELTA_BYTE_LEN;
        }

        @Override
        public Iterator<Delta> iterator() {
            return this;
        }
    }
}
