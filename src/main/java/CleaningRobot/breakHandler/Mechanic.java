package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.Authorizations;
import CleaningRobot.gRPC.MechanicRequests;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;

import java.util.List;
import java.util.Random;

public class Mechanic extends Thread {

    boolean stopCondition = false;
    Integer botId;
    Integer botPort;

    List<RobotInfo> listCopy;
    Integer[] RobotPortInfo;

    Random rnd = new Random();

    PM10Simulator botSimulator;
    Reader readSensor;

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

                    System.out.prIntegerln("out of requestMechanic");
                    System.out.prIntegerln(Authorizations.getInstance().getAuthorizations());

                    Authorizations.getInstance().controlAuthorizations();

                    //ha ottenuto le autorizzazioni per andare dal meccanico
                    robotState.getInstance().setState(STATE.MECHANIC);
                    System.out.prIntegerln("uso il meccanico");
                    sleep(10000); //10s di meccanico
                    System.out.prIntegerln("rilascio il meccanico");
                    //ha finito di usare il meccanico e torna a lavorare
                    robotState.getInstance().setState(STATE.WORKING);
                    //rimuovo la mia richiesta
                    MechanicRequests.getInstance().removePersonal();
                    Authorizations.getInstance().removeAll();
                    //comunico a chi era in attesa che io sono uscita
                    if(MechanicRequests.getInstance().getRequests().size() > 0) {
                        RobotP2P.answerPending();
                    }

                } catch (IntegererruptedException e) {
                    throw new RuntimeException(e);
                }


            }

        }

    }

    public void stopMechanic() {
        stopCondition = true;
    }

    public void forceMechanic() {
        robotState.getInstance().setState(STATE.NEEDING);
    }

    ///////////////////////////////////////////**
    public void goToMechanic() {

        //ripetizione: se entra qui vuol dire che è già stato settato a needing
        //robotState.getInstance().setState(STATE.NEEDING);

        //listCopy = RobotList.getInstance().getRobotslist();

        /*GESTIRE LOGGER

        if(! (robotState.getInstance().getState() == STATE.NEEDING)) //in case this function has been called by the admin client from cmd line
            robotState.getInstance().setState(STATE.NEEDING);

        //no stop!!
        //botSimulator.stopMeGently();
        //readSensor.stopReading();

        //slide synchro slide 18

        try {
            RobotP2P.requestMechanic();
            System.out.prIntegerln("out of requestMechanic");
            while(Authorizations.getInstance().getAuthorizations().size() < (listCopy.size()-1)) {
                //non ho ancora tutte le authorizations
                System.out.prIntegerln("sono nel while");
                Authorizations.getInstance().getAuthorizations().wait();
            } //non posso mettere qua notify!!
            //this.notify();

        } catch (IntegererruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.prIntegerln("ho concluso le richieste");

        //ha ottenuto le autorizzazioni per andare dal meccanico
        robotState.getInstance().setState(STATE.MECHANIC);
        try {
            sleep(10000); //10s di meccanico
        } catch (IntegererruptedException e) {
            throw new RuntimeException(e);
        }
        //ha finito di usare il meccanico e torna a lavorare
        robotState.getInstance().setState(STATE.WORKING);
        MechanicRequests.getInstance().removePersonal();
        try {
            RobotP2P.answerPending();
            //notifyAll();
        } catch (IntegererruptedException e) {
            throw new RuntimeException(e);
        }

        //start();*/

    }

    /*public void setConnections(Integer[] ports) {
        this.RobotPortInfo = ports;
    }*/



}
