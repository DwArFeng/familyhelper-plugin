package com.dwarfeng.familyhelper.plugin.fileio.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 导出模板流下载信息。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public class DubboRestExportTemplateStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = -57163261416945103L;

    @FormParam("task-setting-id")
    @PartType(MediaType.TEXT_PLAIN)
    @JSONField(name = "task_setting_id", ordinal = 1)
    private Long taskSettingId;

    @FormParam("identifier")
    @PartType("text/plain;charset=utf-8")
    @JSONField(name = "identifier", ordinal = 2)
    private String identifier;

    public DubboRestExportTemplateStreamDownloadInfo() {
    }

    public DubboRestExportTemplateStreamDownloadInfo(Long taskSettingId, String identifier) {
        this.taskSettingId = taskSettingId;
        this.identifier = identifier;
    }

    public Long getTaskSettingId() {
        return taskSettingId;
    }

    public void setTaskSettingId(Long taskSettingId) {
        this.taskSettingId = taskSettingId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "DubboRestExportTemplateStreamDownloadInfo{" +
                "taskSettingId=" + taskSettingId +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
