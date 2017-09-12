package com.keithstellyes.lc3.machine.instruction;

import com.keithstellyes.lc3.machine.LC3Machine;
import com.keithstellyes.lc3.machine.LC3MachineException;

import java.util.*;

public class InstructionExecutorList implements InstructionExecutor, List<InstructionExecutor> {
    private final List<InstructionExecutor> executorList = new ArrayList<>();

    public InstructionExecutorList() { }

    public InstructionExecutorList(Iterable<InstructionExecutor> instructionExecutors) {
        for(InstructionExecutor executor : instructionExecutors) {
            executorList.add(executor);
        }
    }

    public InstructionExecutorList(InstructionExecutor... executors) {
        for(InstructionExecutor executor : executors) {
            executorList.add(executor);
        }
    }

    @Override
    public LC3Machine execute(LC3Machine machine) throws LC3MachineException {
        for(InstructionExecutor instructionExecutor : this) {
            machine = instructionExecutor.execute(machine);
        }

        return machine;
    }

    @Override
    public int size() {
        return executorList.size();
    }

    @Override
    public boolean isEmpty() {
        return executorList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return executorList.contains(o);
    }

    @Override
    public Iterator<InstructionExecutor> iterator() {
        return executorList.iterator();
    }

    @Override
    public Object[] toArray() {
        return executorList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return executorList.toArray(a);
    }

    @Override
    public boolean add(InstructionExecutor instructionExecutor) {
        return executorList.add(instructionExecutor);
    }

    @Override
    public boolean remove(Object o) {
        return executorList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return executorList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends InstructionExecutor> c) {
        return executorList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends InstructionExecutor> c) {
        return executorList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return executorList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return executorList.retainAll(c);
    }

    @Override
    public void clear() {
        executorList.clear();
    }

    @Override
    public InstructionExecutor get(int index) {
        return executorList.get(index);
    }

    @Override
    public InstructionExecutor set(int index, InstructionExecutor element) {
        return executorList.set(index, element);
    }

    @Override
    public void add(int index, InstructionExecutor element) {
        executorList.add(index,  element);
    }

    @Override
    public InstructionExecutor remove(int index) {
        return executorList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return executorList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return executorList.lastIndexOf(o);
    }

    @Override
    public ListIterator<InstructionExecutor> listIterator() {
        return executorList.listIterator();
    }

    @Override
    public ListIterator<InstructionExecutor> listIterator(int index) {
        return executorList.listIterator(index);
    }

    @Override
    public List<InstructionExecutor> subList(int fromIndex, int toIndex) {
        return executorList.subList(fromIndex, toIndex);
    }
}
