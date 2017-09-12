package com.keithstellyes.lc3.machine.instruction;

import com.keithstellyes.lc3.machine.Op;
import com.keithstellyes.lc3.machine.UnrecognizedOpcodeException;

/**
 * This LC3 implementation uses the reserved opcode informally called
 * the SPECIAL instruction, which may have special scripts, breakpoints, etc.
 */

public class Instruction {
    public static final byte RESULT_REGISTER_INDEX = 9;
    public static final byte ARGUMENT1_REGISTER_INDEX = 6;
    public static final byte ARGUMENT2_REGISTER_INDEX = 0;
    // A register index for ST* instructions
    public static final byte STORE_REGISTER_INDEX = 9;

    // returns non-zero if immediate
    public static final short IS_IMMEDIATE_MASK = 0b100000;

    protected short binary;
    protected String toStringOutput;
    protected InstructionExecutor executor;

    public Instruction() {
        this(0);
    }

    public Instruction(byte byteLeft, byte byteRight) {
        this((short) ((byteLeft << 8) | byteRight));
    }

    public Instruction(int binary) {
        this((short) binary);
    }

    public Instruction(short binary) {
        this.binary = binary;
    }

    public short getBinary() {
        return this.binary;
    }

    public static Instruction buildAddRegisterInstruction(byte destinationRegister,
                                                   byte sourceRegister1,
                                                   byte sourceRegister2) {
        InstructionBuilder instructionBuilder = new InstructionBuilder();
        instructionBuilder.setOp(Op.ADD)
                           .setRegister(RESULT_REGISTER_INDEX, destinationRegister)
                           .setRegister(ARGUMENT1_REGISTER_INDEX, sourceRegister1)
                           .setRegister(ARGUMENT2_REGISTER_INDEX, sourceRegister2);

        return instructionBuilder.build();
    }

    public Op getOp() throws UnrecognizedOpcodeException {
        return Op.getOpFromInstruction(this);
    }

    public byte getRegister(byte registerIndex) {
        return getRegister(this.getBinary(), registerIndex);
    }

    public static byte getRegister(short binary, byte registerIndex) {
        int mask = 0b111;
        int masked = binary & (mask << registerIndex);
        return (byte) (masked >> registerIndex);
    }

    public boolean getIsImmediate() {
        return (binary & IS_IMMEDIATE_MASK) != 0;
    }

    @Override
    public String toString() {
        if(toStringOutput == null) {
            toStringOutput = Integer.toBinaryString(binary);
        }

        return toStringOutput;
    }

    public InstructionExecutor getExecutor() {
        return executor;
    }

    public static class InstructionBuilder {
        private short binary;
        private InstructionExecutor instructionExecutor;

        public InstructionBuilder() {
            this((short) 0);
        }

        public InstructionBuilder(short binary) {
            this.binary = binary;
        }

        public InstructionBuilder setOp(Op op) {
            setOp(op.getOpcode(), binary);
            return this;
        }

        public InstructionBuilder setOp(byte opcode, short binary) {
            this.binary = (short) (binary | (opcode << Op.OPCODE_INDEX));
            return this;
        }

        public InstructionBuilder setInstructionExecutor(InstructionExecutor instructionExecutor) {
            this.instructionExecutor = instructionExecutor;
            return this;
        }

        public InstructionBuilder setRegister(byte registerIndex, byte register) {
            binary = (short) (binary | (register << registerIndex));
            return this;
        }

        public InstructionBuilder setImmediate() {
            return setImmediate(true);
        }

        public InstructionBuilder setImmediate(boolean isImmediate) {
            if(isImmediate) {
                binary = (short) (binary | IS_IMMEDIATE_MASK);
            } else {
                short mask = (short) (~IS_IMMEDIATE_MASK);
                binary = (short) (binary & mask);
            }

            return this;
        }

        public Instruction build() {
            Instruction instruction = new Instruction(binary);
            instruction.executor = instructionExecutor;
            return instruction;
        }
    }
}
