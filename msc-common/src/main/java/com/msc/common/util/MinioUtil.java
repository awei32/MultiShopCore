package com.msc.common.util;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * MinIO 工具类
 *
 * @author yourname
 */
@Component
public class MinioUtil {

    private static final Logger logger = LoggerFactory.getLogger(MinioUtil.class);

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;

    /**
     * 初始化 MinIO 客户端
     */
    public void init() {
        if (minioClient == null) {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            logger.info("MinIO 客户端初始化成功");
        }
    }

    /**
     * 上传文件
     *
     * @param objectName 对象名称
     * @param inputStream 输入流
     * @return 上传成功返回 true
     */
    public boolean uploadFile(String objectName, InputStream inputStream) {
        init();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType("application/octet-stream")
                            .build()
            );
            logger.info("文件上传成功: {}", objectName);
            return true;
        } catch (Exception e) {
            logger.error("文件上传失败: {}", objectName, e);
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     * @return 删除成功返回 true
     */
    public boolean deleteFile(String objectName) {
        init();
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            logger.info("文件删除成功: {}", objectName);
            return true;
        } catch (Exception e) {
            logger.error("文件删除失败: {}", objectName, e);
            return false;
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param objectName 对象名称
     * @return 存在返回 true
     */
    public boolean fileExists(String objectName) {
        init();
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        } catch (Exception e) {
            logger.debug("文件不存在: {}", objectName, e);
            return false;
        }
    }

    // Getter 方法
    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public MinioClient getMinioClient() {
        init();
        return minioClient;
    }
}