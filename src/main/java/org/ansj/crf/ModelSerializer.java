package org.ansj.crf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import lombok.SneakyThrows;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * by zhl on 15/7/21.
 */
public class ModelSerializer extends Serializer<Model> {

    @Override
    public void write(final Kryo kryo, final Output output, final Model object) {
        new TwoDimentionalIntsSerializer().write(kryo, output, object.ft);
        output.writeInt(object.left);
        output.writeInt(object.right);
        output.writeInt(object.tagNum);
        kryo.writeObject(output, object.statusMap, statusMapSerializer(kryo));

        new TwoDimentionalDoublesSerializer().write(kryo, output, object.getStatus());
        kryo.writeObject(output, object.getGrad(), gradMapSerializer(kryo));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Model read(final Kryo kryo, final Input input, final Class<Model> type) {
        final int[][] ft = new TwoDimentionalIntsSerializer().read(kryo, input, int[][].class);
        final int left = input.readInt();
        final int right = input.readInt();
        final int tagNum = input.readInt();
        final Map<String, Integer> statusMap = kryo.readObject(input, cast(HashMap.class), statusMapSerializer(kryo));

        final double[][] status = new TwoDimentionalDoublesSerializer().read(kryo, input, double[][].class);
        final Map<String, double[][]> grad = kryo.readObject(input, cast(HashMap.class), gradMapSerializer(kryo));
        return new Model(ft, left, right, tagNum, statusMap, status, grad, null);
    }

    public static MapSerializer statusMapSerializer(final Kryo kryo) {
        final MapSerializer statusMapSerializer = new MapSerializer();
        statusMapSerializer.setKeyClass(String.class, new DefaultSerializers.StringSerializer());
        statusMapSerializer.setValueClass(Integer.class, new DefaultSerializers.IntSerializer());
        //statusMapSerializer.setGenerics(kryo, new Class[]{String.class, Integer.class});
        return statusMapSerializer;
    }

    public static MapSerializer gradMapSerializer(final Kryo kryo) {
        final MapSerializer gradMapSerializer = new MapSerializer();
        gradMapSerializer.setKeyClass(String.class, new DefaultSerializers.StringSerializer());
        gradMapSerializer.setValueClass(double[][].class, new TwoDimentionalDoublesSerializer());
        //gradMapSerializer.setGenerics(kryo, new Class[]{String.class, double[][].class});
        return gradMapSerializer;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> cast(final Class<?> clazz) {
        return (Class<T>) clazz;
    }

    @SneakyThrows
    public static void write(final Model model, final OutputStream outputStream) {
        final Kryo kryo = new Kryo();
        try (final Output output = new Output(new BufferedOutputStream(new BZip2CompressorOutputStream(outputStream)))) {
            kryo.writeObject(output, model, new ModelSerializer());
        }
    }

    @SneakyThrows
    public static Model read(final InputStream inputStream) {
        final Kryo kryo = new Kryo();
        try (final Input input = new Input(new BufferedInputStream(new BZip2CompressorInputStream(inputStream)))) {
            return kryo.readObject(input, Model.class, new ModelSerializer());
        }
    }
}
