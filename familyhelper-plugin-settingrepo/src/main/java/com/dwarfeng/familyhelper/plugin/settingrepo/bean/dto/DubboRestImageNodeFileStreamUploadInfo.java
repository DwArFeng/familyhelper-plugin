package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Dubbo rest 图片节点文件流上传信息。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
public class DubboRestImageNodeFileStreamUploadInfo implements Dto {

    private static final long serialVersionUID = 2621033287407948351L;
    
    @FormParam("category")
    @PartType("text/plain;charset=utf-8")
    private String category;

    @FormParam("args")
    @PartType("application/json;charset=utf-8")
    private String[] args;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestImageNodeFileStreamUploadInfo() {
    }

    public DubboRestImageNodeFileStreamUploadInfo(
            String category, String[] args, String originName, long length, InputStream content
    ) {
        this.category = category;
        this.args = args;
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public DubboRestImageNodeFileStreamUploadInfo setCategory(String category) {
        this.category = category;
        return this;
    }

    public String[] getArgs() {
        return args;
    }

    public DubboRestImageNodeFileStreamUploadInfo setArgs(String[] args) {
        this.args = args;
        return this;
    }

    public String getOriginName() {
        return originName;
    }

    public DubboRestImageNodeFileStreamUploadInfo setOriginName(String originName) {
        this.originName = originName;
        return this;
    }

    public long getLength() {
        return length;
    }

    public DubboRestImageNodeFileStreamUploadInfo setLength(long length) {
        this.length = length;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public DubboRestImageNodeFileStreamUploadInfo setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "DubboRestImageNodeFileStreamUploadInfo{" +
                "category='" + category + '\'' +
                ", args=" + Arrays.toString(args) +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
