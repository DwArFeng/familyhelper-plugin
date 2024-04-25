package com.dwarfeng.familyhelper.plugin.life.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 活动文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public class DubboRestActivityFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = -8145163016077395338L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("activity-file-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long activityFileLongId;

    public DubboRestActivityFileStreamDownloadInfo() {
    }

    public DubboRestActivityFileStreamDownloadInfo(String userStringId, Long activityFileLongId) {
        this.userStringId = userStringId;
        this.activityFileLongId = activityFileLongId;
    }

    public String getUserStringId() {
        return userStringId;
    }

    public void setUserStringId(String userStringId) {
        this.userStringId = userStringId;
    }

    public Long getActivityFileLongId() {
        return activityFileLongId;
    }

    public void setActivityFileLongId(Long activityFileLongId) {
        this.activityFileLongId = activityFileLongId;
    }

    @Override
    public String toString() {
        return "DubboRestActivityFileStreamDownloadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", activityFileLongId=" + activityFileLongId +
                '}';
    }
}
