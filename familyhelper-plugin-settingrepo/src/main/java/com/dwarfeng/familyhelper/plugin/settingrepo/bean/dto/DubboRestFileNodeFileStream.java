package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 文件节点文件流。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public class DubboRestFileNodeFileStream implements Dto {

    private static final long serialVersionUID = -5535319882188551923L;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestFileNodeFileStream() {
    }

    public DubboRestFileNodeFileStream(String originName, long length, InputStream content) {
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getOriginName() {
        return originName;
    }

    public DubboRestFileNodeFileStream setOriginName(String originName) {
        this.originName = originName;
        return this;
    }

    public long getLength() {
        return length;
    }

    public DubboRestFileNodeFileStream setLength(long length) {
        this.length = length;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public DubboRestFileNodeFileStream setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "DubboRestFileNodeFileStream{" +
                "originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
