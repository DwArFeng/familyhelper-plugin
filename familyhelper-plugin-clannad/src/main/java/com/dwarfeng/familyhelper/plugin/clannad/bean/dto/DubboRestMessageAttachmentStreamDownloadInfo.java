package com.dwarfeng.familyhelper.plugin.clannad.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 留言附件流下载信息。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public class DubboRestMessageAttachmentStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = -2690171795542625119L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("message-attachment-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long messageAttachmentLongId;

    public DubboRestMessageAttachmentStreamDownloadInfo() {
    }

    public DubboRestMessageAttachmentStreamDownloadInfo(String userStringId, Long messageAttachmentLongId) {
        this.userStringId = userStringId;
        this.messageAttachmentLongId = messageAttachmentLongId;
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

    @Override
    public String toString() {
        return "DubboRestMessageAttachmentStreamDownloadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", messageAttachmentLongId=" + messageAttachmentLongId +
                '}';
    }
}
