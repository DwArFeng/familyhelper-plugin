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
public class DubboRestImageListNodeFileStreamUploadInfo implements Dto {

    private static final long serialVersionUID = -4078376188136183424L;

    @FormParam("category")
    @PartType("text/plain;charset=utf-8")
    private String category;

    @FormParam("args")
    @PartType("application/json;charset=utf-8")
    private String[] args;

    @FormParam("index")
    @PartType(MediaType.TEXT_PLAIN)
    private Integer index;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestImageListNodeFileStreamUploadInfo() {
    }

    public DubboRestImageListNodeFileStreamUploadInfo(
            String category, String[] args, Integer index, String originName, long length, InputStream content
    ) {
        this.category = category;
        this.args = args;
        this.index = index;
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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
        return "DubboRestImageListNodeFileStreamUploadInfo{" +
                "category='" + category + '\'' +
                ", args=" + Arrays.toString(args) +
                ", index=" + index +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
