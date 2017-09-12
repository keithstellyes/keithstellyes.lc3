package com.keithstellyes.lc3.machine;

import com.keithstellyes.lc3.machine.instruction.Instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An immutable LC3 Machine. Stores a delta to avoid reinstantiating
 * all of the data.
 */
public class LC3Machine {
    public static final int REGISTER_COUNT = 8;
    public static final int MEMORY_SIZE = (int) Math.pow(2, 16);
    public static final byte PSR_NEGATIVE_INDEX = 2;
    public static final byte PSR_ZERO_INDEX = 1;
    public static final byte PSR_POSITIVE_INDEX = 0;

    private int programCounter;
    private byte[] data;

    private byte[] memoryDelta;
    private short[] registers ;
    private byte[] outputData;
    private short programStatusRegister;
    private Program program;

    public LC3Machine() {
        programCounter = 0;
        data = new byte[MEMORY_SIZE];
        memoryDelta = new byte[0];
        registers = new short[REGISTER_COUNT];
        outputData = new byte[0];
        programStatusRegister = 0;
        program = new Program();
    }

    public LC3Machine(LC3Machine machine) {
        this.programCounter = machine.programCounter;
        this.data = machine.data;
        this.memoryDelta = machine.memoryDelta;
        this.registers = machine.registers;
        this.outputData = machine.outputData;
        this.programStatusRegister = machine.programStatusRegister;
        this.program = new Program();
    }

    public LC3Machine(LC3Machine machine, Program program) {
        this(machine);
        this.program = program;
    }

    /**
     * Makes a new copy of this machine, but with the delta applied.
     * Does not mutate itself.
     *
     * Note that
     * @return
     */
    public LC3Machine applyDelta() {
        return null;
    }

    public boolean getPSRNegative() {
        return BinaryHelper.getNthBit(programStatusRegister, PSR_NEGATIVE_INDEX) != 0;
    }

    public boolean getPSRZero() {
        return BinaryHelper.getNthBit(programStatusRegister, PSR_ZERO_INDEX) != 0;
    }

    public boolean getPSRPositive() {
        return BinaryHelper.getNthBit(programStatusRegister, PSR_POSITIVE_INDEX) != 0;
    }

    public Instruction getCurrentInstruction() throws LC3MachineException {
        short word = getCurrentWord();
        return program.get(word, programCounter);
    }

    public short[] getRegisters() {
        return registers.clone();
    }

    public byte[] getData() {
        return Delta.dataAfterDeltas(data, memoryDelta);
    }

    public short getCurrentWord() {
        short left = Delta.getValueAtIndexAfterDelta(data, memoryDelta, programCounter);
        short right = Delta.getValueAtIndexAfterDelta(data, memoryDelta, programCounter + 1);
        return (short) ((left << 8) | right);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof LC3Machine)) {
            return false;
        }

        return false;
    }

    public static class LC3MachineBuilder {
        private final LC3Machine machine;
        private boolean registersHaveBeenModified = false;
        private final List<Delta> deltaList;

        public LC3MachineBuilder() {
            this(new LC3Machine());
        }

        public LC3MachineBuilder(LC3Machine machine) {
            this.machine = new LC3Machine(machine);
            this.deltaList = new ArrayList<>(Arrays.asList(Delta.parseDeltas(machine.memoryDelta)));
        }

        public LC3MachineBuilder setRegister(byte register, short value) {
            if(!registersHaveBeenModified) {
                machine.registers = machine.registers.clone();
                registersHaveBeenModified = true;
            }
            machine.registers[register] = value;
            return this;
        }

        public LC3MachineBuilder setMemory(int index, byte value) {
            return setMemory(new Delta(index, value));
        }

        public LC3MachineBuilder setMemory(Delta delta) {
            deltaList.add(delta);
            return this;
        }

        public LC3MachineBuilder incrementProgramCounter() {
            machine.programCounter++;
            return this;
        }

        public LC3MachineBuilder setPSRNegative(boolean flag) {
            machine.programStatusRegister = (short) BinaryHelper.setNthBit(machine.programStatusRegister, PSR_NEGATIVE_INDEX, flag);
            return this;
        }

        public LC3MachineBuilder setPSRZero(boolean flag) {
            machine.programStatusRegister = (short) BinaryHelper.setNthBit(machine.programStatusRegister, PSR_ZERO_INDEX, flag);
            return this;
        }

        public LC3MachineBuilder setPSRPositive(boolean flag) {
            machine.programStatusRegister = (short) BinaryHelper.setNthBit(machine.programStatusRegister, PSR_POSITIVE_INDEX, flag);
            return this;
        }

        public LC3Machine build() {
            Delta[] deltaArray = new Delta[deltaList.size()];
            deltaList.toArray(deltaArray);
            machine.memoryDelta = Delta.deltaBytesFromDeltaArray(deltaArray);
            return machine;
        }
    }
}
