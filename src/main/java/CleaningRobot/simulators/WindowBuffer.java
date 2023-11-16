package CleaningRobot.simulators;

import AdminServer.AdminServer;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/*At the end of a read operation, readAllAndClean makes room for new measurements in
the buffer. Specifically, you must process sensor data through the sliding
window technique that was introduced in the theory lessons. You must
consider a buffer of 8 measurements, with an overlap factor of 50%. When
the dimension of the buffer is equal to 8 measurements, you must compute
the average of these 8 measurements. A cleaning robot will periodically send
these averages to the Administrator Server*/

public class WindowBuffer implements Buffer {

    private int size;
    private List<Measurement> buffer;

    private static final Logger logger = Logger.getLogger(AdminServer.class.getSimpleName());

    public WindowBuffer(int size) {
        this.size = size;

        this.buffer = new ArrayList<>(this.size);
    }


    @Override
    public synchronized void addMeasurement(Measurement m) {

        try {
            while (buffer.size() == this.size) {
                this.wait();
            }

            this.buffer.add(m);

            if (buffer.size() == this.size) {
                this.notify();
            }
        }
        catch (Exception e) {
            logger.warning("Exception inside the buffer");
        }

    }

    @Override
    public synchronized List<Measurement> readAllAndClean() {

        try {
            while (buffer.size() < this.size) {
                this.wait();
            }

            List<Measurement> windowMeasurements = new ArrayList<>(this.buffer);

            //50%
            int toDelete = 4;

            for (int i = 0; i < toDelete; i++)
                this.buffer.remove(i);

            this.notify();

            return windowMeasurements;

        } catch (InterruptedException e) {
            //in case of interrupt i send what i have even if under the buffer size
            return new ArrayList<>(this.buffer);
        }

    }
}
