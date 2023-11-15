package CleaningRobot.breakHandler;

import CleaningRobot.gRPC.Authorizations;
import CleaningRobot.gRPC.MechanicRequests;
import CleaningRobot.gRPC.RobotP2P;

import java.util.Locale;
import java.util.logging.Logger;

public class Mechanic extends Thread {

    boolean stopCondition = false;

    private static final Logger logger = Logger.getLogger(Mechanic.class.getSimpleName());

    public Mechanic() {

    }

    @Override
    public void run() {

        while(!stopCondition) {

            //wait for crash
            crashSimulator.waitingForCrash();

            if(robotState.getInstance().getState() == STATE.NEEDING) {

                try {
                    RobotP2P.requestMechanic();

                    Authorizations.getInstance().controlAuthorizations();

                    //ha ottenuto le autorizzazioni per andare dal meccanico
                    robotState.getInstance().setState(STATE.MECHANIC);
                    logger.info("I'm using the mechanic");
                    sleep(10000); //10s di meccanico
                    logger.info("I'm releasing the mechanic");
                    //ha finito di usare il meccanico e torna a lavorare
                    robotState.getInstance().setState(STATE.WORKING);
                    //rimuovo la mia richiesta
                    MechanicRequests.getInstance().removePersonal();
                    Authorizations.getInstance().removeAll();
                    //comunico a chi era in attesa che io sono uscita
                    if(MechanicRequests.getInstance().getRequests().size() > 0) {
                        RobotP2P.answerPending();
                    }

                } catch (InterruptedException e) {
                    //how to deal?
                    logger.warning("mandato un interrupt, cosa accade?");
                    //throw new RuntimeException(e);
                }


            }

        }

    }

    public void stopMechanic() {
        stopCondition = true;
    }

}
