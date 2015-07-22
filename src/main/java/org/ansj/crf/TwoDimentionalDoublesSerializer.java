package org.ansj.crf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * by zhl on 15/7/21.
 */
public class TwoDimentionalDoublesSerializer extends Serializer<double[][]> {

    @Override
    public void write(final Kryo kryo, final Output output, final double[][] object) {
        output.writeInt(object.length);
        for (final double[] doubles : object) {
            output.writeInt(doubles.length);
            output.writeDoubles(doubles);
        }
    }

    @Override
    public double[][] read(final Kryo kryo, final Input input, final Class<double[][]> type) {
        final double[][] object = new double[input.readInt()][];
        for (int i = 0; i < object.length; i++) {
            object[i] = input.readDoubles(input.readInt());
        }
        return object;
    }
}
