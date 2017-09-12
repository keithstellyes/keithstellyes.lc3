package com.keithstellyes.lc3.machine.test;

import com.keithstellyes.lc3.machine.*;
import com.keithstellyes.lc3.machine.instruction.Instruction;
import com.keithstellyes.lc3.machine.instruction.SpecialInstructionExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class UT_LC3Machine {
    LC3Machine machine;

    @BeforeEach
    public void setUp() {
        machine = new LC3Machine();
    }

    @Test
    public void testDefault() {
        assertFalse(machine.getPSRNegative());
        assertFalse(machine.getPSRZero());
        assertFalse(machine.getPSRPositive());
    }

    // Tests basic functionality of the builder, while testing it doesn't mutate the original machine
    @Test
    public void testBuilder() {
        LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder(machine);
        builder.setPSRNegative(true);
        machine = builder.build();
        assertTrue(machine.getPSRNegative());
        assertFalse(machine.getPSRZero());
        assertFalse(machine.getPSRPositive());

        LC3Machine originalMachine = machine;
        builder = new LC3Machine.LC3MachineBuilder(machine);
        builder.setMemory(0, (byte) 1);

        machine = builder.build();
        assertTrue(machine.getPSRNegative());
        assertFalse(machine.getPSRZero());
        assertFalse(machine.getPSRPositive());
        assertEquals((byte) 1, machine.getData()[0]);
        byte[] machineData = machine.getData();
        for(int i = 1; i < machineData.length; i++) {
            assertEquals((byte) 0, machineData[i]);
        }

        // test that the machine is still in its original state
        machineData = originalMachine.getData();
        for(int i = 0; i < machineData.length; i++) {
            assertEquals((byte) 0, machineData[i]);
        }
    }

    @Test
    public void testAllDeltaValues() {
        byte i = Byte.MIN_VALUE;
        while(true) {
            LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder();
            builder.setMemory(0, i);
            machine = builder.build();
            assertEquals(i, machine.getData()[0]);
            if(i == Byte.MAX_VALUE) {
                break;
            }
            i++;
        }
    }

    @Test
    public void testAllDeltaIndices() {
        for(int i = 0; i < LC3Machine.MEMORY_SIZE; i++) {
            LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder();
            builder.setMemory(i, (byte) 5);
            byte[] results = builder.build().getData();
            assertEquals(5, results[i]);
        }
    }

    @Test
    public void testTwoDeltas() {
        LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder(machine);
        builder.setMemory(0, (byte)1);
        builder.setMemory(1, (byte)2);
        machine = builder.build();

        byte[] data = machine.getData();
        assertEquals((byte) 1, data[0]);
        assertEquals((byte) 2, data[1]);
    }

    @Test
    public void testThreeDeltas() {
        LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder(machine);
        builder.setMemory(0, (byte) 1)
               .setMemory(1, (byte) 2)
               .setMemory(2, (byte) 3);
        machine = builder.build();
        assertEquals(1, machine.getData()[0]);
        assertEquals(2, machine.getData()[1]);
        assertEquals(3, machine.getData()[2]);
    }

    @Test
    public void testMultipleDeltas() {
        LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder(machine);
        int length = machine.getData().length;
        for(int i = 0; i < length; i++) {
            builder.setMemory(i, (byte) 5);
        }
        machine = builder.build();

        byte[] data = machine.getData();
        for(int i = 0; i < length; i++) {
            assertEquals((byte) 5, data[i]);
        }
    }

    @Test
    public void deleteThisTest() throws LC3MachineException {
        Instruction.InstructionBuilder builder = new Instruction.InstructionBuilder();
        String script = "print('Hello, World!');";
        builder.setOp(Op.SPECIAL)
                .setInstructionExecutor(SpecialInstructionExecutor.createJavaScriptInstructionExecutor(script));
        Instruction instruction = builder.build();
        instruction.getExecutor().execute(machine);
    }
}
