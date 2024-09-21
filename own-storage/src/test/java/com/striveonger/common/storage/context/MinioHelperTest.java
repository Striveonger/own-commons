package com.striveonger.common.storage.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import com.striveonger.common.core.Jackson;
import com.striveonger.common.storage.config.StorageConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MinioHelperTest {

    private MinioHelper helper;

    @Before
    public void before() {
        StorageConfig.MinioStoreConfig config = new StorageConfig.MinioStoreConfig();
        config.setEndpoint("10.13.144.121");
        config.setPort(9000);
        config.setSecure(false);
        config.setAccessKey("BSIgjjn67zEEAoB5rCGG");
        config.setSecretKey("RuIvDSXRgsiMa9vtgDebTP2fq8bPjMCTUhIAcQxR");
        helper = new MinioHelper(config);
    }

    @Test
    public void test() {
        String bucket = "fairy-music-storage";
        // boolean exists = helper.isExistsBucket(bucket);
        // System.out.println(exists);
        // helper.createBucket(bucket);
        // helper.deleteBucket(bucket);

        // Map<String, String> tag = Map.of();
        // helper.setBucketTags(bucket, tag);

        // List<Pair<String, String>> tags = helper.getBucketTags(bucket);
        // System.out.println(Jackson.toJSONString(tags));

        // List<Bucket> list = helper.getBuckets();
        // System.out.println(Jackson.toJSONString(list));

        byte[] bytes = FileUtil.readBytes("/Users/striveonger/development/workspace/temp/fairy-music-storage/2024-09-13/1078966550188269593.mp3");
        boolean saved = helper.saveObject(bucket, "/music/20240920/001.mp3", bytes);
        System.out.println(saved);

        byte[] objects = helper.getObject(bucket, "/music/20240920/001.mp3");
        // System.out.println(Jackson.toJSONString(objects));
        System.out.println(bytes.length == objects.length);
        String x = "一样";
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i]!= objects[i]) {
                x = "不一样";
                break;
            }
        }
        System.out.println(x);
        boolean exist = helper.isExistObject(bucket, "/music/20240920/000.mp3");
        System.out.println(exist);
        if (exist) {
            helper.deleteObject(bucket, "/music/20240920/000.mp3");
        }
        helper.setObjectTags(bucket, "/music/20240920/001.mp3", Map.of("region", "Beijing"));

        List<Pair<String, String>> tags = helper.getObjectTags(bucket, "/music/20240920/001.mp3");
        System.out.println(Jackson.toJSONString(tags));


    }






}