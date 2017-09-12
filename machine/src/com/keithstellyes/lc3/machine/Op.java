package com.keithstellyes.lc3.machine;

import com.keithstellyes.lc3.machine.instruction.Instruction;

public enum Op {
    ADD(0b0001),
    AND(0b0101),
    NOT(0b1001),
    BR (0b0000),
    JMP(0b1100),
    JSR(0b0100),
    RET(JMP.opcode),
    LD (0b0010),
    LDI(0b1010),
    LDR(0b0110),
    LEA(0b1110),
    ST (0b0011),
    STI(0b1011),
    STR(0b0111),
    TRAP(0b1111),
    RTI(0b1000),
    RESERVED(0b1101),
    SPECIAL(RESERVED.opcode);
    Op(int opcode) {
        this((byte) opcode);
    }
    Op(byte opcode) {
        this.opcode = opcode;
    }

    private final byte opcode;

    public byte getOpcode() {
        return this.opcode;
    }

    public static Op getOpFromInstruction(Instruction instruction)  throws UnrecognizedOpcodeException {
        return getOpFromInstruction(instruction.getBinary());
    }

    public static Op getOpFromInstruction(short rawInstruction) throws UnrecognizedOpcodeException {
        byte opcode = (byte) (((rawInstruction & OPCODE_MASK) >>> OPCODE_INDEX) & 0b1111);

        for(Op op : Op.values()) {
            if(op.opcode == opcode) {
                return op;
            }
        }

        throw new UnrecognizedOpcodeException("Unrecognized Opcode:" + Integer.toBinaryString(opcode));
    }

    public static final int OPCODE_INDEX = 12;
    public static final short OPCODE_MASK = (short) (0b1111 << OPCODE_INDEX);
}