package CleaningRobot.breakHandler;

import AdminServer.beans.RobotInfo;
import AdminServer.beans.RobotList;
import CleaningRobot.MQTT.Reader;
import CleaningRobot.gRPC.Authorizations;
import CleaningRobot.gRPC.MechanicRequests;
import CleaningRobot.gRPC.RobotP2P;
import CleaningRobot.simulators.PM10Simulator;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Mechanic extends Thread {

    boolean stopCondition = false;
    int botId;
    int botPort;

    List<RobotInfo> listCopy;
    int[] RobotPortInfo;

    Random rnd = new Random();

    PM10Simulator botSimulator;
    Reader readSensor;

    public Mechanic(PM10Simulator botSimulator, Reader readSensor, int botId, int botPort) {
        this.botSimulator = botSimulator;
        this.readSensor = readSensor;

        this.botId = botId;
        this.botPort = botPort;
    }

    @Override
    public void run() {

        while(!stopCondition) {

            //try {
            //wait for requests
            if(robotState.getInstance().getState() == STATE.NEEDING) {

                try {
                    RobotP2P.requestMechanic(/*listCopy, botPort, botId*/);
                    System.out.println("out of requestMechanic");
                    System.out.println(Authorizations.getInstance().getAuthorizations());
                    Authorizations.getInstance().controlAuthorizations();

                    //ha ottenuto le autorizzazioni per andare dal meccanico
                    robotState.getInstance().setState(STATE.MECHANIC);
                    System.out.println("uso il meccanico");
                    sleep(10000); //10s di meccanico
                    System.out.println("rilascio il meccanico");
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
                    throw new RuntimeException(e);
                }


            }

        }

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
            System.out.println("out of requestMechanic");
            while(Authorizations.getInstance().getAuthorizations().size() < (listCopy.size()-1)) {
                //non ho ancora tutte le authorizations
                System.out.println("sono nel while");
                Authorizations.getInstance().getAuthorizations().wait();
            } //non posso mettere qua notify!!
            //this.notify();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ho concluso le richieste");

        //ha ottenuto le autorizzazioni per andare dal meccanico
        robotState.getInstance().setState(STATE.MECHANIC);
        try {
            sleep(10000); //10s di meccanico
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //ha finito di usare il meccanico e torna a lavorare
        robotState.getInstance().setState(STATE.WORKING);
        MechanicRequests.getInstance().removePersonal();
        try {
            RobotP2P.answerPending();
            //notifyAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //start();*/

    }

    public void setConnections(int[] ports) {
        this.RobotPortInfo = ports;
    }

    public void forceMechanic() {
        robotState.getInstance().setState(STATE.NEEDING);
    }

}
