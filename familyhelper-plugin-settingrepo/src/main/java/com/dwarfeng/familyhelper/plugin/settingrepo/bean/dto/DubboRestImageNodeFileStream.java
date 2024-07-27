package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 图片节点文件流。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
public class DubboRestImageNodeFileStream implements Dto {

    private static final long serialVersionUID = -8532927249701711227L;
    
    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestImageNodeFileStream() {
    }

    public DubboRestImageNodeFileStream(String originName, long length, InputStream content) {
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getOriginName() {
        return originName;
    }

    public DubboRestImageNodeFileStream setOriginName(String originName) {
        this.originName = originName;
        return this;
    }

    public long getLength() {
        return length;
    }

    public DubboRestImageNodeFileStream setLength(long length) {
        this.length = length;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public DubboRestImageNodeFileStream setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "DubboRestImageNodeFileStream{" +
                "originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
