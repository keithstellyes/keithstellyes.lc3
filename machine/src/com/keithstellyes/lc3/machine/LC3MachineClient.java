package com.keithstellyes.lc3.machine;

import java.io.InputStream;
import java.io.OutputStream;

public class LC3MachineClient {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private LC3Machine machine;

    public LC3MachineClient(LC3Machine machine) {
        this(System.in, System.out, machine);
    }

    public LC3MachineClient(InputStream inputStream, OutputStream outputStream, LC3Machine machine) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.machine = machine;
    }

    public void step() throws LC3MachineException {
        machine = machine.getCurrentInstruction().getExecutor().execute(machine);
    }
}
