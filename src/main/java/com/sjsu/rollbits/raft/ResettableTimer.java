package com.sjsu.rollbits.raft;

public abstract class ResettableTimer implements Runnable {

    private Object lock = new Object();
    private long timeout_ms;
    private long last;

    public ResettableTimer(long timeout_ms) {
        this.timeout_ms = timeout_ms;
    }

    public void reset() {
        synchronized (lock) {
            last = System.currentTimeMillis();
            lock.notify();
        }
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                for (;;) {
                    lock.wait(timeout_ms);
                    long delta = System.currentTimeMillis() - last;
                    if (delta >= timeout_ms) {
                        timeout();
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    protected abstract void timeout();
}
