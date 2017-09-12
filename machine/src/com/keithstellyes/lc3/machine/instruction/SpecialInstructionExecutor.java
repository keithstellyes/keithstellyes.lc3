package com.keithstellyes.lc3.machine.instruction;

import com.keithstellyes.lc3.machine.LC3Machine;
import com.keithstellyes.lc3.machine.LC3MachineException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class SpecialInstructionExecutor implements InstructionExecutor {
    private static final ScriptEngineFactory scriptEngineFactoryJavaScript = new NashornScriptEngineFactory();
    private final InstructionExecutor instructionExecutor;

    private SpecialInstructionExecutor(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    public static SpecialInstructionExecutor createJavaScriptInstructionExecutor(String script) {
        InstructionExecutor instructionExecutor = new InstructionExecutor() {
            private final ScriptEngine scriptEngine = scriptEngineFactoryJavaScript.getScriptEngine();

            @Override
            public LC3Machine execute(LC3Machine machine) throws LC3MachineException {
                scriptEngine.put("machine", machine);
                LC3Machine.LC3MachineBuilder builder = new LC3Machine.LC3MachineBuilder(machine);
                scriptEngine.put("machineBuilder", builder);

                try {
                    scriptEngine.eval(script);
                } catch (ScriptException e) {
                    throw new LC3MachineException("Script exception:" + e.toString());
                }

                return builder.build();
            }
        };

        return new SpecialInstructionExecutor(instructionExecutor);
    }

    @Override
    public LC3Machine execute(LC3Machine machine) throws LC3MachineException {
        return instructionExecutor.execute(machine);
    }
}
