package com.keithstellyes.lc3.machine.test;

import com.keithstellyes.lc3.machine.instruction.Instruction;
import com.keithstellyes.lc3.machine.Op;
import com.keithstellyes.lc3.machine.UnrecognizedOpcodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UT_Instruction {
    @Test
    public void testSettingAllOps() throws UnrecognizedOpcodeException {
        for(Op op : Op.values()) {
            Instruction.InstructionBuilder builder = new Instruction.InstructionBuilder();
            builder.setOp(op);
            Instruction instruction = builder.build();
            if(op != Op.JMP && op != Op.RET) {
                assertEquals(op, instruction.getOp());
            } else { //jmp and ret use the same opcode
                assertTrue(Op.RET == instruction.getOp() || Op.JMP == instruction.getOp());
            }
        }
    }

    @Test
    public void setAddRegisterBuiltCorrectly() {
        Instruction instruction = Instruction.buildAddRegisterInstruction((byte) 0, (byte) 0, (byte) 0);
        assertEquals(0b0001000000000000, instruction.getBinary());

        instruction = Instruction.buildAddRegisterInstruction((byte) 0, (byte) 1, (byte) 2);
        assertEquals(0b0001000001000010, instruction.getBinary());
    }
}
