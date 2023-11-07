package CleaningRobot.simulators;

import java.util.List;

public Integererface Buffer {

    void addMeasurement(Measurement m);

    List<Measurement> readAllAndClean();

}
