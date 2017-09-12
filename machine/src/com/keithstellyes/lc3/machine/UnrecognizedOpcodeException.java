package com.keithstellyes.lc3.machine;

public class UnrecognizedOpcodeException extends LC3MachineException {
    public UnrecognizedOpcodeException(String detailMessage) {
        super(detailMessage);
    }
}
