package org.org.apache.sketch;
import com.yahoo.sketches.Family;
import com.yahoo.sketches.theta.UpdateSketch;
import com.yahoo.sketches.theta.UpdateSketchBuilder;
import com.yahoo.sketches.tuple.UpdatableSketchBuilder;
import com.yahoo.sketches.tuple.*;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;

import java.io.*;
import java.util.Random;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
          Random rand = new Random();
//        ArrayOfDoublesUpdatableSketchBuilder arrayOfDoublesUpdatableSketchBuilder = new ArrayOfDoublesUpdatableSketchBuilder();
//        arrayOfDoublesUpdatableSketchBuilder.setSeed((long) Math.pow(2,5));
//        ArrayOfDoublesUpdatableSketch sketch1 = arrayOfDoublesUpdatableSketchBuilder
//                .build();
//        for (int key = 0; key < 100000; key++) sketch1.update(key, new double[] {rand.nextGaussian()});
//
//        double estimate = sketch1.getEstimate();
//        UpdateSketchBuilder updateSketchBuilder = new UpdeSketchBuilder().setSeed((long) Math.pow(2, 18)).setFamily(Family.TUPLE);
//        UpdateSketch sketch = updateSketchBuilder.build();


        UpdatableSketchBuilder updatableSketchBuilder = new UpdatableSketchBuilder(
                                                        new DoubleSummaryFactory(DoubleSummary.Mode.Max)).setNominalEntries((int)Math.pow(2,18));
        UpdatableSketch sketch = updatableSketchBuilder.build();
        UpdatableSketch sketch1 = updatableSketchBuilder.build();

        StringBuilder SB = new StringBuilder();
        for (int key = 0; key < 10000000; key++) {
            sketch.update(UUID.randomUUID().toString(),0.0);
            //SB.append(UUID.randomUUID().toString());
            //SB.append("\r\n");
        }


        for (int key = 0; key < 50000; key++) {
            sketch1.update(UUID.randomUUID().toString(),0.0);
            //SB.append(UUID.randomUUID().toString());
            //SB.append("\r\n");
        }
        //try {
            //FileOutputStream fileOutputStream = new FileOutputStream("");
            //FileWriter fw = new FileWriter("D:\\lht\\output\\uuid.txt");
            //BufferedWriter bw = new BufferedWriter(fw);
            //bw.write(SB.toString());
            //bw.close();
        //} catch (FileNotFoundException e) {
        //    throw new RuntimeException(e);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);

        //ItemsSketch<String> sketch = new ItemsSketch<String>();
//
        double estimate = sketch.getEstimate();
        Union union = new Union((int)Math.pow(2,18) ,new DoubleSummarySetOperations());
        union.update(sketch);
        union.update(sketch1);
        double estimate1 = union.getResult().getEstimate();
        System.out.println((long)estimate1);
        //System.out.println(Math.pow(2,18));

    }
}
