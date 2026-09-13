package com.dwarfeng.familyhelper.plugin.fileio.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 导入文件流。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public class DubboRestImportFileStream implements Dto {

    private static final long serialVersionUID = -5129754084346474842L;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestImportFileStream() {
    }

    public DubboRestImportFileStream(String originName, long length, InputStream content) {
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DubboRestImportFileStream{" +
                "originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
