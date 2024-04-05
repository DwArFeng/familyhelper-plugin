package com.dwarfeng.familyhelper.plugin.assets.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 项目文件流更新信息。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public class DubboRestItemFileStreamUpdateInfo implements Dto {

    private static final long serialVersionUID = -897227403866172435L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("item-file-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long itemFileLongId;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestItemFileStreamUpdateInfo() {
    }

    public DubboRestItemFileStreamUpdateInfo(
            String userStringId, Long itemFileLongId, String originName, long length, InputStream content
    ) {
        this.userStringId = userStringId;
        this.itemFileLongId = itemFileLongId;
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

    public Long getItemFileLongId() {
        return itemFileLongId;
    }

    public void setItemFileLongId(Long itemFileLongId) {
        this.itemFileLongId = itemFileLongId;
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
        return "DubboRestItemFileStreamUpdateInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", itemFileLongId=" + itemFileLongId +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
