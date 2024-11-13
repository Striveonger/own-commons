package com.striveonger.common.core;

import org.junit.jupiter.api.Test;

import java.util.List;

class FileKitTest {

    @Test
    public void test() {
        String path = "/Users/striveonger/tmp/image-script-exporter-1.0.0.tar";
        List<String> paths = FileKit.of().splitFile(path, 10);
        System.out.println(paths);

        FileKit.of().mergeFile(paths, path + ".merge");
    }
}