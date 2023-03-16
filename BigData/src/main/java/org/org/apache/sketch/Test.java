package org.org.apache.sketch;
import com.yahoo.sketches.theta.UpdateSketch;
import com.yahoo.sketches.theta.UpdateSketchBuilder;
import com.yahoo.sketches.tuple.*;

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
        UpdateSketchBuilder updateSketchBuilder = new UpdateSketchBuilder().setSeed((long) Math.pow(2, 18));
        UpdateSketch sketch = updateSketchBuilder.build();
        for (int key = 0; key < 8000; key++) sketch.update(UUID.randomUUID().toString());

        //ItemsSketch<String> sketch = new ItemsSketch<String>();
        double estimate = sketch.getEstimate();
        System.out.println(estimate);

    }
}
