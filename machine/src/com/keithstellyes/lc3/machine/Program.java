package com.keithstellyes.lc3.machine;

import com.keithstellyes.lc3.machine.instruction.Instruction;

public class Program {
    private Instruction[] instructions;
    private int[] instructionIndices;

    private boolean warnOnModifiedInstruction = true;
    private boolean warnOnMisaligned = true;
    private final int origin;

    public Program() {
        this.origin = 0;
    }

    public Program(int origin, Instruction... instructions) {
        this.instructions = instructions;
        this.origin = origin;
    }

    /**
     * Returns the corresponding instruction given a program counter, and the word it is at.
     *
     *                         +--- Instruction is same as before
     * PC is at an instruction +
     *                         +--- Instruction has been modified
     *
     * PC is not at an instruction
     *
     * @param word
     * @param programCounter
     * @return
     * @throws LC3MachineException
     */
    public Instruction get(short word, int programCounter) throws LC3MachineException {
        for(int i = 0; i < instructionIndices.length; i++) {
            if(instructionIndices[i] == programCounter) {
                if(instructions[i].getBinary() == word) {
                    return instructions[i];
                } else if(!warnOnModifiedInstruction){
                    return new Instruction(word);
                } else {
                    throw new LC3MachineException("Program seems to have been modified. " +
                                                  "Expected:" + Integer.toHexString(instructions[i].getBinary()) +
                                                  "Actual:"   +Integer.toHexString(word));
                }
            }
        }
        if(warnOnMisaligned) {
            throw new LC3MachineException("PC seems to be pointing outside the program. It points at:" +
                                          Integer.toHexString(word));
        }
        return new Instruction(word);
    }
}
