package com.keithstellyes.lc3.machine.test;

import com.keithstellyes.lc3.machine.Delta;
import com.keithstellyes.lc3.machine.LC3Machine;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UT_Delta {
    @Test
    public void testDelta() {
        Delta delta = new Delta(0, (byte) 0);
        byte[] data = new byte[1];
        byte[] expectedData = new byte[1];
        byte[] actual = Delta.dataAfterDeltas(data, delta.toDeltaBytes());
        assertTrue(Objects.deepEquals(expectedData, actual));

        Delta[] deltas = { delta };
        actual = Delta.dataAfterDeltas(data, deltas
        );
    }

    @Test
    public void testDeltaSerialization() {
        for(int i = 0; i < LC3Machine.MEMORY_SIZE; i++) {
            Delta delta = new Delta(i, (byte) 1);
            Delta generatedDelta = Delta.parseDeltas(delta.toDeltaBytes())[0];
            assertEquals(delta, generatedDelta);
        }
    }
}
