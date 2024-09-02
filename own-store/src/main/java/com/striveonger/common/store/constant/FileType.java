package com.striveonger.common.store.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-26 00:27
 */
public enum FileType {
    BMP      ("bmp",       "image",      true),
    JPG      ("jpg",       "image",      true),
    JPGE     ("jpge",      "image",      true),
    PNG      ("png",       "image",      true),
    GIF      ("gif",       "image",      true),
    MP3      ("mp3",       "audio",      true),
    MP4      ("mp4",       "video",      true),
    DOC      ("doc",       "doc",      false),
    DOCX     ("docx",      "docx" ,    false),
    XLS      ("xls",       "xls",      false),
    XLSX     ("xlsx",      "xlsx",     false),
    PPT      ("ppt",       "ppt",      false),
    PPTX     ("pptx",      "pptx",     false),
    TXT      ("txt",       "txt",      false),
    ;
    /**
     * 文件类型
     */
    private final String type;

    /**
     * 文件标签
     */
    private final String tag;

    /**
     * 是否为流媒体(Streaming media)
     */
    private final boolean supportPreview;

    FileType(String type, String tag, boolean supportPreview) {
        this.type = type;
        this.tag = tag;
        this.supportPreview = supportPreview;
    }

    public String getType() { return type; }

    public String getTag() { return tag; }

    public boolean supportPreview() { return supportPreview; }

    public static FileType of(String type) {
        for (FileType fileType : values()) {
            if (StrUtil.equals(fileType.getType(), type, true)) {
                return fileType;
            }
        }
        return null;
    }
}
