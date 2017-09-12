package com.keithstellyes.lc3.machine.instruction;

import com.keithstellyes.lc3.machine.LC3Machine;
import com.keithstellyes.lc3.machine.LC3MachineException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public interface InstructionExecutor {
    /**
     * Should not mutate the machine.
     * @param machine
     * @return
     */
    public LC3Machine execute(LC3Machine machine) throws LC3MachineException;
}
