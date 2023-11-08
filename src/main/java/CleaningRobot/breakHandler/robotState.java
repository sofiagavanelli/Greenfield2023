package CleaningRobot.breakHandler;

public class robotState {

    public static STATE current = STATE.WORKING;

    //logical clock?
    private int logicalClock=0;
    Object lock = new Object();
    /* when to increment:
        - state change
        - before sending msg?
        - when receiving msg?
     */

    private static robotState instance;

    //singleton
    public static synchronized robotState getInstance(){
        if(instance==null)
            instance = new robotState();
        return instance;
    }

    public synchronized STATE getState() {
        return current;
    }

    public synchronized void setState(STATE newS) {
        current = newS;
        this.incrementClock();
    }

    //CLOCK
    public void setClock(int value) {
        this.logicalClock = value;
    }

    public int getClock() {
        return logicalClock;
    }

    public void incrementClock() {
        synchronized (lock) {
            this.logicalClock = this.logicalClock + 1;
        }
    }

}