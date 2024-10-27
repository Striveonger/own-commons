package com.striveonger.common.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHelperTest {

    @Test
    public void test() {
        String path = "/Users/striveonger/tmp/image-script-exporter-1.0.0.tar";
        List<String> paths = FileHelper.of().splitFile(path, 10);
        System.out.println(paths);

        FileHelper.of().mergeFile(paths, path + ".merge");
    }
}