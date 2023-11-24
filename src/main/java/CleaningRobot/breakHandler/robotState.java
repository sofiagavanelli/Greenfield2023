package CleaningRobot.breakHandler;

public class robotState {

    public static STATE current = STATE.WORKING;

    private int logicalClock = 0;
    final Object lock = new Object();
    /* when to increment:
        - state change
        - before sending msg
        - when receiving msg you set the clock depending on lamport
     */

    private static robotState instance;

    //singleton
    public static synchronized robotState getInstance(){
        if(instance==null)
            instance = new robotState();
        return instance;
    }

    public STATE getState() {
        synchronized(current) {
            return current;
        }
    }

    public void setState(STATE newS) {
        incrementClock();
        synchronized(current) {
            current = newS;
        }
    }

    //CLOCK
    public void setClock(int value) {
        synchronized(lock) {
            logicalClock = value;
        }
    }

    public int getClock() {
        synchronized(lock) {
            return logicalClock;
        }
    }

    public void incrementClock() {
        synchronized (lock) {
            logicalClock = logicalClock + 1;
        }
    }

    public void adjustClock(int senderClock) {
        synchronized (lock) {
            logicalClock = Math.max(senderClock, logicalClock) + 1;
        }
    }

}