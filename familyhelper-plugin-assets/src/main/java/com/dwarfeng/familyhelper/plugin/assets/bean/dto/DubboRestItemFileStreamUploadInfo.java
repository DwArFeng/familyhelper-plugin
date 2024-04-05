package com.dwarfeng.familyhelper.plugin.assets.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 项目文件流上传信息。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public class DubboRestItemFileStreamUploadInfo implements Dto {

    private static final long serialVersionUID = 8957463474487068715L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("item-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long itemLongId;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestItemFileStreamUploadInfo() {
    }

    public DubboRestItemFileStreamUploadInfo(
            String userStringId, Long itemLongId, String originName, long length, InputStream content
    ) {
        this.userStringId = userStringId;
        this.itemLongId = itemLongId;
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getUserStringId() {
        return userStringId;
    }

    public void setUserStringId(String userStringId) {
        this.userStringId = userStringId;
    }

    public Long getItemLongId() {
        return itemLongId;
    }

    public void setItemLongId(Long itemLongId) {
        this.itemLongId = itemLongId;
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
        return "DubboRestItemFileStreamUploadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", itemLongId=" + itemLongId +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
