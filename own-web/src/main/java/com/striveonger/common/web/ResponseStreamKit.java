package com.striveonger.common.web;

import cn.hutool.core.io.IoUtil;
import com.striveonger.common.core.constant.FileType;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author Mr.Lee
 * @since 2024-07-26 00:32
 */
public class ResponseStreamKit {
    private final static Logger log = LoggerFactory.getLogger(ResponseStreamKit.class);

    /**
     * 导出文件, 由浏览器下载
     *
     * @param filename 文件名
     * @param request  请求体
     * @param response 响应体
     * @param bytes    文件流
     */
    public static void export(String filename, HttpServletRequest request, HttpServletResponse response, byte[] bytes) {
        Consumer<OutputStream> write = o -> IoUtil.write(o, true, bytes);
        String prefix = filename.substring(filename.lastIndexOf(".") + 1);
        response.reset();
        response.setContentType("application/" + prefix + ";charset=UTF-8");
        String filenameDisplay = filenameDisplay(request, filename);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filenameDisplay + "\"");
        // response.setHeader("Content-Length", fileSize);
        process(response, write);
    }


    /**
     * 预览文件, 由浏览器展示
     *
     * @param filename 文件名
     * @param request  请求体
     * @param response 响应体
     * @param bytes    文件流
     */
    public static void preview(String filename, HttpServletRequest request, HttpServletResponse response, byte[] bytes) {
        String prefix = filename.substring(filename.lastIndexOf(".") + 1);
        FileType fileType = FileType.of(prefix);
        if (fileType != null && fileType.supportPreview()) {
            Consumer<OutputStream> write = o -> IoUtil.write(o, true, bytes);
            response.reset();
            String filenameDisplay = filenameDisplay(request, filename);
            response.setContentType(fileType.contentType());
            response.setHeader("Content-Disposition", "inline;filename=\"" + filenameDisplay + "\"");
            // response.setHeader("Content-Length", String.valueOf(bytes.length / 8));
            response.setHeader("Content-Range", "bytes 0-");
            response.setHeader("Accept-Ranges", "bytes");
            process(response, write);
        } else {
            throw new CustomException(ResultStatus.ACCIDENT, "不支持预览的文件类型");
        }
    }


    /**
     * 导出文件, 由浏览器下载
     *
     * @param response 响应体
     * @param write    写数据的外部调用
     */
    private static void process(HttpServletResponse response, Consumer<OutputStream> write) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            write.accept(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("write out file error...", e);
            throw new CustomException("write out file error");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("close output stream error...", e);
                }
            }
        }
    }

    private static String filenameDisplay(HttpServletRequest request, String fileName) {
        String filenameDisplay = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        if (request.getHeader("User-Agent").toUpperCase().indexOf("FIREFOX") > 0) {
            // firefox浏览器
            filenameDisplay = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            // IE浏览器
            filenameDisplay = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("SAFARI") > 0) {
            // safari 浏览器
            filenameDisplay = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
            // Chrome浏览器
            filenameDisplay = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        return filenameDisplay;
    }
}
