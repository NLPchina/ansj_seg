package org.ansj;

import lombok.SneakyThrows;
import org.ansj.crf.Model;
import org.ansj.crf.ModelSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.ansj.AnsjUtils.filesystemResource;

/**
 * by zhl on 15/7/20.
 */
public class TransCrfModel {

    @SneakyThrows
    public static void main(final String[] args) {
        final String file = "/tmp/crf.bz2";
        new File(file).delete();

        final Model model = Model.loadModel(filesystemResource("library/crf.model"));
        ModelSerializer.write(model, new FileOutputStream(file));

        ModelSerializer.read(new FileInputStream(file));
    }
}
