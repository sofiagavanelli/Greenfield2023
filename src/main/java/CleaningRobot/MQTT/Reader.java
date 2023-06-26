package CleaningRobot.MQTT;

import CleaningRobot.simulators.Measurement;
import CleaningRobot.simulators.WindowBuffer;

import java.util.*;

public class Reader extends Thread {

    private boolean stopCondition = false;
    private WindowBuffer measures;
    private List<Double> statistics = new ArrayList<>();

    public Reader(WindowBuffer buf) {
        this.measures = buf;
    }

    @Override
    public void run() {

        while(!stopCondition) {
            createAverage(measures.readAllAndClean());
        }

    }

    public void stopReading() {
        stopCondition = true;
    }

    public void createAverage(List<Measurement> data) {

        /*general code:
            data.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0)
         */

        OptionalDouble average = data.stream()
                .mapToDouble(Measurement::getValue)
                .average();

        if(average.isPresent()) this.statistics.add(average.getAsDouble());

    }

    public List<Double> getAverages() {

        List<Double> copy = new ArrayList<>(this.statistics);

        this.statistics.clear();

        return copy;
    }


}
