package com.msc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 文件上传配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {

    /**
     * 文件上传路径
     */
    private String path = "/tmp/uploads";

    /**
     * 头像配置
     */
    private Avatar avatar = new Avatar();

    @Data
    public static class Avatar {
        /**
         * 最大文件大小
         */
        private String maxSize = "5MB";

        /**
         * 允许的文件类型
         */
        private List<String> allowedTypes = List.of("jpg", "jpeg", "png", "gif");
    }
}