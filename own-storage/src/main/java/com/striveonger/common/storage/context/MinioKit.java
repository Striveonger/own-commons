package com.striveonger.common.storage.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import com.striveonger.common.core.FileKit;
import com.striveonger.common.core.MarkGenerate;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.storage.config.StorageConfig;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @since 2024-09-21 09:31
 */
public class MinioKit {
    private final Logger log = LoggerFactory.getLogger(MinioKit.class);

    private final ExtMinioClient client;

    public MinioKit(StorageConfig.MinioStoreConfig config) {
        this(config.getEndpoint(), config.getPort(), config.getSecure(), config.getAccessKey(), config.getSecretKey());
    }

    public MinioKit(String endpoint, int port, boolean secure, String accessKey, String secretKey) {
        this.client = ExtMinioClient.of(MinioClient.builder().endpoint(endpoint, port, secure).credentials(accessKey, secretKey).build());
    }

    /**
     * 获取MinioClient
     * 可以自行扩展功能使用
     */
    public MinioClient getClient() {
        return client;
    }

    /**
     * 判断存储桶是否存在
     *
     * @param name
     * @return
     */
    public boolean isExistBucket(String name) {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        } catch (Exception e) {
            log.error("MinioHelper.bucketExists error", e);
        }
        return false;
    }

    /**
     * 判断存储桶是否不存在
     *
     * @param name
     * @return
     */
    public boolean isNotExistBucket(String name) {
        return !isExistBucket(name);
    }

    /**
     * 创建存储桶
     *
     * @param name
     */
    public boolean createBucket(String name) {
        if (isNotExistBucket(name)) {
            try {
                client.makeBucket(MakeBucketArgs.builder().bucket(name).build());
                return true;
            } catch (Exception e) {
                log.error("MinioHelper.createBucket error", e);
            }
            return false;
        }
        return true;
    }

    /**
     * 删除存储桶
     *
     * @param name
     */
    public boolean deleteBucket(String name) {
        if (isExistBucket(name)) {
            try {
                client.removeBucket(RemoveBucketArgs.builder().bucket(name).build());
                return true;
            } catch (Exception e) {
                log.error("MinioHelper.deleteBucket error", e);
            }
        }
        return false;
    }

    /**
     * 获取所有存储桶
     */
    public List<Bucket> getBuckets() {
        try {
            return client.listBuckets();
        } catch (Exception e) {
            log.error("MinioHelper.listBuckets error", e);
        }
        return List.of();
    }

    /**
     * 获取存储桶标签
     *
     * @param name
     */
    public List<Pair<String, String>> getBucketTags(String name) {
        if (isExistBucket(name)) {
            try {
                Tags tags = client.getBucketTags(GetBucketTagsArgs.builder().bucket(name).build());
                List<Pair<String, String>> list = new ArrayList<>();
                tags.get().forEach((k, v) -> list.add(Pair.of(k, v)));
                return list;
            } catch (Exception e) {
                log.error("MinioHelper.getBucketTags error", e);
            }
        }
        return List.of();
    }

    /**
     * 设置存储桶标签(全量覆盖)
     *
     * @param name
     * @param tags
     */
    public boolean setBucketTags(String name, Map<String, String> tags) {
        if (isExistBucket(name)) {
            synchronized (name.intern()) {
                try {
                    client.setBucketTags(SetBucketTagsArgs.builder().bucket(name).tags(tags).build());
                    return true;
                } catch (Exception e) {
                    log.error("MinioHelper.setBucketTags error", e);
                }
            }
        }
        return false;
    }

    /**
     * 保存文件对象
     *
     * @param bucket
     */
    public boolean saveObject(String bucket, String filepath, byte[] bytes) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            PutObjectArgs args = PutObjectArgs.builder().bucket(bucket).object(filepath).stream(stream, bytes.length, -1).build();
            client.putObject(args);
            return true;
        } catch (Exception e) {
            log.error("MinioHelper.saveObject error", e);
        }
        return false;
    }


    /**
     * 删除文件对象
     *
     * @param bucket
     * @param filepath
     */
    public boolean deleteObject(String bucket, String filepath) {
        if (isExistObject(bucket, filepath)) {
            try {
                RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(bucket).object(filepath).build();
                client.removeObject(args);
                return true;
            } catch (Exception e) {
                log.error("MinioHelper.deleteObject error", e);
            }
        }
        return false;
    }

    /**
     * 获取文件对象
     *
     * @param bucket
     * @param filepath
     * @return
     */
    public byte[] getObject(String bucket, String filepath) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(filepath).build();
        try (InputStream stream = client.getObject(args)) {
            return stream.readAllBytes();
        } catch (Exception e) {
            log.error("MinioHelper.getObject error", e);
        }
        return new byte[]{};
    }

    /**
     * 判断文件对象是否存在
     *
     * @param bucket
     * @param filepath
     * @return
     */
    public boolean isExistObject(String bucket, String filepath) {
        try {
            StatObjectArgs args = StatObjectArgs.builder().bucket(bucket).object(filepath).build();
            StatObjectResponse object = client.statObject(args);
            return object != null;
        } catch (Exception e) {
            // log.error("MinioHelper.isExistObject error", e);
        }
        return false;
    }

    /**
     * 判断文件对象是否不存在
     *
     * @param bucket
     * @param filepath
     * @return
     */
    public boolean isNotExistObject(String bucket, String filepath) {
        return !isExistObject(bucket, filepath);
    }

    /**
     * 获取文件对象标签
     *
     * @param bucket
     * @param filepath
     * @return
     */
    public List<Pair<String, String>> getObjectTags(String bucket, String filepath) {
        if (isExistObject(bucket, filepath)) {
            try {
                GetObjectTagsArgs args = GetObjectTagsArgs.builder().bucket(bucket).object(filepath).build();
                Tags objectTags = client.getObjectTags(args);
                List<Pair<String, String>> list = new ArrayList<>();
                objectTags.get().forEach((k, v) -> list.add(Pair.of(k, v)));
                return list;
            } catch (Exception e) {
                log.error("MinioHelper.getObjectTags error", e);
            }
        }
        return List.of();
    }

    /**
     * 设置文件对象标签(全量覆盖)
     *
     * @param bucket
     * @param filepath
     * @param tags
     * @return
     */
    public boolean setObjectTags(String bucket, String filepath, Map<String, String> tags) {
        if (isExistObject(bucket, filepath)) {
            try {
                SetObjectTagsArgs args = SetObjectTagsArgs.builder().bucket(bucket).object(filepath).tags(tags).build();
                client.setObjectTags(args);
                return true;
            } catch (Exception e) {
                log.error("MinioHelper.setObjectTags error", e);
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------

    public MultipartUpload createMultipartUpload(String fileName, long fileSize, int chunkCount) {
        return client.createMultipartUpload(fileName, fileSize, chunkCount);
    }

    public boolean uploadChunk(String fileId, byte[] bytes, int index) {
        return client.uploadChunk(fileId, bytes, index);
    }

    public File mergeChunks(String fileId) {
        return client.mergeChunks(fileId);
    }

    public boolean uploadMergedFile(String bucket, String filepath, String fileId) {
        File file = client.mergeChunks(fileId);
        byte[] bytes = FileUtil.readBytes(file);
        return saveObject(bucket, filepath, bytes);
    }

    public boolean clear(String fileId) {
        return client.clear(fileId);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 扩展MinioClient
     * super.createMultipartUpload 被弃用了, 自己实现一个呗
     */
    private static class ExtMinioClient extends MinioClient {

        /**
         * 分片上传的文件信息
         */
        private final Map<String, MultipartUpload> uploads = new ConcurrentHashMap<>();

        private final String tempath;

        private ExtMinioClient(MinioClient client, String tempath) {
            super(client);
            this.tempath = tempath;
        }

        public static ExtMinioClient of(MinioClient client, String tempath) {
            return new ExtMinioClient(client, tempath);
        }

        public static ExtMinioClient of(MinioClient client) {
            return of(client, "/var/tmp");
        }

        public MultipartUpload createMultipartUpload(String fileName, long fileSize, int chunkCount) {
            MultipartUpload upload = new MultipartUpload(fileName, fileSize, chunkCount);
            uploads.put(upload.getFileId(), upload);
            // 初始化临时文件夹
            String path = this.tempath + File.separator + upload.getFileId();
            upload.setTempath(path);
            FileKit.of().mkdir(path);
            return upload;
        }

        public boolean uploadChunk(String fileId, byte[] bytes, int index) {
            // 多线程上传时, 避免乱序问题, 加入 chunk index
            MultipartUpload upload = uploads.get(fileId);
            boolean result = false;
            if (Objects.nonNull(upload)) {
                int len = String.valueOf(upload.getChunkCount()).length();
                String filename = fileId + ".%0" + len + "d" + ".part";
                File file = new File(upload.getTempath(), String.format(filename, index));
                result = FileKit.of().write(file, bytes);
                FileChunk chunk = new FileChunk(file, index);
                upload.uploadChunk(chunk);
            }
            return result;
        }

        /**
         * 合并分片
         * @param fileId
         * @return
         */
        public File mergeChunks(String fileId) {
            MultipartUpload upload = uploads.get(fileId);
            if (Objects.nonNull(upload) && upload.isComplete()) {
                if (Objects.nonNull(upload.getMergedFile())) {
                    // 已经合并过了, 直接返回
                    return upload.getMergedFile();
                }
                File target = new File(upload.getTempath(), upload.getFileName());
                // 根据 index 排序
                Comparator<FileChunk> comparator = Comparator.comparingInt(FileChunk::index);
                List<File> chunks = upload.getChunks().stream().sorted(comparator).map(FileChunk::file).toList();
                FileKit.of().mergeFile(chunks, target);
                upload.setMergedFile(target);
                return target;
            } else {
                String s = Objects.nonNull(upload) ? "文件上传不完整" : "文件ID不正确";
                throw new CustomException(s + ", 导致文件合并失败").show();
            }
        }

        public boolean clear(String fileId) {
            MultipartUpload upload = uploads.remove(fileId);
            if (Objects.nonNull(upload)) {
                return FileKit.of().delete(upload.getTempath());
            }
            return true;
        }
    }

    public static class MultipartUpload {
        private final String fileId;
        private final String fileName;
        private final long fileTotalSize;
        private final int chunkCount;
        private final List<FileChunk> chunks = new ArrayList<>();

        private String tempath;
        private File mergedFile;

        public MultipartUpload(String fileName, long fileTotalSize, int chunkCount) {
            this.fileId = MarkGenerate.build();
            this.fileName = fileName;
            this.fileTotalSize = fileTotalSize;
            this.chunkCount = chunkCount;
        }

        public String getFileId() {
            return fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public long getFileTotalSize() {
            return fileTotalSize;
        }

        public int getChunkCount() {
            return chunkCount;
        }

        public List<FileChunk> getChunks() {
            return chunks;
        }

        public String getTempath() {
            return tempath;
        }

        public void setTempath(String tempath) {
            this.tempath = tempath;
        }

        public File getMergedFile() {
            return mergedFile;
        }

        public void setMergedFile(File mergedFile) {
            this.mergedFile = mergedFile;
        }

        public synchronized void uploadChunk(FileChunk chunk) {
            chunks.add(chunk);
        }

        /**
         * 是否完整
         */
        public boolean isComplete() {
            long total = chunks.stream().map(FileChunk::file).mapToLong(File::length).sum();
            return chunks.size() == chunkCount && total == fileTotalSize;
        }
    }

    public record FileChunk(File file, int index) { }
}
