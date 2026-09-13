package com.dwarfeng.familyhelper.plugin.fileio.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 导入文件流上传信息。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public class DubboRestImportFileStreamUploadInfo implements Dto {

    private static final long serialVersionUID = -8798156661886746112L;

    @FormParam("task-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long taskId;

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

    public DubboRestImportFileStreamUploadInfo() {
    }

    public DubboRestImportFileStreamUploadInfo(
            Long taskId, String identifier, String originName, long length, InputStream content
    ) {
        this.taskId = taskId;
        this.identifier = identifier;
        this.originName = originName;
        this.length = length;
        this.content = content;
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
        return "DubboRestImportFileStreamUploadInfo{" +
                "taskId=" + taskId +
                ", identifier='" + identifier + '\'' +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
