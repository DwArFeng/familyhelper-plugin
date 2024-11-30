package com.dwarfeng.familyhelper.plugin.clannad.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 留言附件流更新信息。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public class DubboRestMessageAttachmentStreamUpdateInfo implements Dto {

    private static final long serialVersionUID = 8383308482042465253L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("message-attachment-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long messageAttachmentLongId;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestMessageAttachmentStreamUpdateInfo() {
    }

    public DubboRestMessageAttachmentStreamUpdateInfo(
            String userStringId, Long messageAttachmentLongId, String originName, long length, InputStream content
    ) {
        this.userStringId = userStringId;
        this.messageAttachmentLongId = messageAttachmentLongId;
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

    public Long getMessageAttachmentLongId() {
        return messageAttachmentLongId;
    }

    public void setMessageAttachmentLongId(Long messageAttachmentLongId) {
        this.messageAttachmentLongId = messageAttachmentLongId;
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
        return "DubboRestMessageAttachmentStreamUpdateInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", messageAttachmentLongId=" + messageAttachmentLongId +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
