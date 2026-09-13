package com.dwarfeng.familyhelper.plugin.fileio.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 导出文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public class DubboRestExportFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = 432901820196614721L;

    @FormParam("task-id")
    @PartType(MediaType.TEXT_PLAIN)
    @JSONField(name = "task_id", ordinal = 1)
    private Long taskId;

    @FormParam("identifier")
    @PartType("text/plain;charset=utf-8")
    @JSONField(name = "identifier", ordinal = 2)
    private String identifier;

    public DubboRestExportFileStreamDownloadInfo() {
    }

    public DubboRestExportFileStreamDownloadInfo(Long taskId, String identifier) {
        this.taskId = taskId;
        this.identifier = identifier;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "DubboRestExportFileStreamDownloadInfo{" +
                "taskId=" + taskId +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
