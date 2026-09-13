package com.dwarfeng.familyhelper.plugin.fileio.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 导入模板流上传信息。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public class DubboRestImportTemplateStreamUploadInfo implements Dto {

    private static final long serialVersionUID = -3893188497072032662L;

    @FormParam("task-setting-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long taskSettingId;

    @FormParam("identifier")
    @PartType("text/plain;charset=utf-8")
    private String identifier;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestImportTemplateStreamUploadInfo() {
    }

    public DubboRestImportTemplateStreamUploadInfo(
            Long taskSettingId, String identifier, String originName, long length, InputStream content
    ) {
        this.taskSettingId = taskSettingId;
        this.identifier = identifier;
        this.originName = originName;
        this.length = length;
        this.content = content;
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
        return "DubboRestImportTemplateStreamUploadInfo{" +
                "taskSettingId=" + taskSettingId +
                ", identifier='" + identifier + '\'' +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
