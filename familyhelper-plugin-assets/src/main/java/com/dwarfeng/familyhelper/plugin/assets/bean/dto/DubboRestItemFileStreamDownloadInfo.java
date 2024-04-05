package com.dwarfeng.familyhelper.plugin.assets.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 项目文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public class DubboRestItemFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = 3367594386374563453L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("item-file-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long itemFileLongId;

    public DubboRestItemFileStreamDownloadInfo() {
    }

    public DubboRestItemFileStreamDownloadInfo(String userStringId, Long itemFileLongId) {
        this.userStringId = userStringId;
        this.itemFileLongId = itemFileLongId;
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

    @Override
    public String toString() {
        return "DubboRestItemFileStreamDownloadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", itemFileLongId=" + itemFileLongId +
                '}';
    }
}
