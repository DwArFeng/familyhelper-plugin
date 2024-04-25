package com.dwarfeng.familyhelper.plugin.life.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 活动文件流上传信息。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public class DubboRestActivityFileStreamUploadInfo implements Dto {

    private static final long serialVersionUID = 5650872319608157178L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("activity-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long activityLongId;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestActivityFileStreamUploadInfo() {
    }

    public DubboRestActivityFileStreamUploadInfo(
            String userStringId, Long activityLongId, String originName, long length, InputStream content
    ) {
        this.userStringId = userStringId;
        this.activityLongId = activityLongId;
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

    public Long getActivityLongId() {
        return activityLongId;
    }

    public void setActivityLongId(Long activityLongId) {
        this.activityLongId = activityLongId;
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
        return "DubboRestActivityFileStreamUploadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", activityLongId=" + activityLongId +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
