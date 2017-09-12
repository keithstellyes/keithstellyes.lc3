package com.keithstellyes.lc3.machine.instruction;

/**
 * A plain old executor for the add instruction.
 */

import com.keithstellyes.lc3.machine.LC3Machine;
import com.keithstellyes.lc3.machine.LC3MachineException;

public class AddExecutor implements InstructionExecutor {
    public static final AddExecutor INSTANCE = new AddExecutor();
    private AddExecutor() { }

    @Override
    public LC3Machine execute(LC3Machine machine) throws LC3MachineException {
        Instruction instruction = machine.getCurrentInstruction();
        LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder(machine);
        builder.incrementProgramCounter();

        if(instruction.getIsImmediate()) {

        } else {
            byte resultRegister = instruction.getRegister(Instruction.RESULT_REGISTER_INDEX);
            byte registerArg1 = instruction.getRegister(Instruction.ARGUMENT1_REGISTER_INDEX);
            byte registerArg2 = instruction.getRegister(Instruction.ARGUMENT2_REGISTER_INDEX);
            short[] registers = machine.getRegisters();
            short result = (short) (registers[registerArg1] + registers[registerArg2]);
            builder.setRegister(resultRegister, result);
            if(result > 0) {
                builder.setPSRPositive(true);
            } else if(result == 0) {
                builder.setPSRZero(true);
            } else {
                builder.setPSRNegative(true);
            }
        }

        return builder.build();
    }
}
