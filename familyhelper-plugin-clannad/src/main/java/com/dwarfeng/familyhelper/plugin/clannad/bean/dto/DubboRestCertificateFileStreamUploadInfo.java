package com.dwarfeng.familyhelper.plugin.clannad.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Dubbo rest 凭证文件流上传信息。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public class DubboRestCertificateFileStreamUploadInfo implements Dto {

    private static final long serialVersionUID = -4879016673506339281L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("Certificate-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long CertificateLongId;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestCertificateFileStreamUploadInfo() {
    }

    public DubboRestCertificateFileStreamUploadInfo(
            String userStringId, Long CertificateLongId, String originName, long length, InputStream content
    ) {
        this.userStringId = userStringId;
        this.CertificateLongId = CertificateLongId;
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

    public Long getCertificateLongId() {
        return CertificateLongId;
    }

    public void setCertificateLongId(Long CertificateLongId) {
        this.CertificateLongId = CertificateLongId;
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
        return "DubboRestCertificateFileStreamUploadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", CertificateLongId=" + CertificateLongId +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
