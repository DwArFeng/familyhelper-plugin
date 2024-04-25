package com.dwarfeng.familyhelper.plugin.clannad.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 凭证文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public class DubboRestCertificateFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = 1154714793938938067L;

    @FormParam("user-string-id")
    @PartType("text/plain;charset=utf-8")
    private String userStringId;

    @FormParam("Certificate-file-long-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long CertificateFileLongId;

    public DubboRestCertificateFileStreamDownloadInfo() {
    }

    public DubboRestCertificateFileStreamDownloadInfo(String userStringId, Long CertificateFileLongId) {
        this.userStringId = userStringId;
        this.CertificateFileLongId = CertificateFileLongId;
    }

    public String getUserStringId() {
        return userStringId;
    }

    public void setUserStringId(String userStringId) {
        this.userStringId = userStringId;
    }

    public Long getCertificateFileLongId() {
        return CertificateFileLongId;
    }

    public void setCertificateFileLongId(Long CertificateFileLongId) {
        this.CertificateFileLongId = CertificateFileLongId;
    }

    @Override
    public String toString() {
        return "DubboRestCertificateFileStreamDownloadInfo{" +
                "userStringId='" + userStringId + '\'' +
                ", CertificateFileLongId=" + CertificateFileLongId +
                '}';
    }
}
