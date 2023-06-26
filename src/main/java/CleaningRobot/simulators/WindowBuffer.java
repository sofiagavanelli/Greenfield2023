package CleaningRobot.simulators;

import java.util.List;
import java.util.ArrayList;

/*At the end of a read operation, readAllAndClean makes room for new measurements in
the buffer. Specifically, you must process sensor data through the sliding
window technique that was introduced in the theory lessons. You must
consider a buffer of 8 measurements, with an overlap factor of 50%. When
the dimension of the buffer is equal to 8 measurements, you must compute
the average of these 8 measurements. A cleaning robot will periodically send
these averages to the Administrator Server*/

public class WindowBuffer implements Buffer {

    private int size;
    //private float overlap = 1/2;
    private double overlap = 0.5;
    private List<Measurement> buffer;

    private Measurement average;

    public WindowBuffer(int size) {
        this.size = size;

        //option: Array deques have no capacity restrictions; they grow as necessary to support usage.
        // They are not thread-safe; in the absence of external synchronization, they do not support
        // concurrent access by multiple threads. Null elements are prohibited.
        this.buffer = new ArrayList<>(this.size);
    }


    @Override
    public synchronized void addMeasurement(Measurement m) {

        try {
            while (buffer.size() == this.size) {
                //System.out.println("add in wait");
                this.wait();
            }

            //System.out.println(m.getValue());
            this.buffer.add(m);

            if (buffer.size() == this.size) {
                //System.out.println("add in notify");
                this.notify();
            }
        }
        catch (Exception e) {

        }

    }

    @Override
    public synchronized List<Measurement> readAllAndClean() {

        //List<Measurement> windowMeasurements = new ArrayList<>(this.buffer);
        //return windowMeasurements;

        try {
            while (buffer.size() < this.size) {
                //System.out.println("read in wait");
                this.wait();
            }

            List<Measurement> windowMeasurements = new ArrayList<>(this.buffer);

            int toDelete = 4; // Math.round(this.size * this.overlap);

            for (int i = 0; i < toDelete; i++)
                this.buffer.remove(i);

            this.notify();
            //System.out.println("read in notify");

            return windowMeasurements;

        } catch (InterruptedException e) {
            return new ArrayList<>(this.buffer);
        }

    }
}
