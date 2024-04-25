package com.dwarfeng.familyhelper.plugin.clannad.service;

import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 凭证文件操作服务。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
@Path("familyhelper-clannad/dubbo-rest-certificate-file-operate-service")
public interface DubboRestCertificateFileOperateService extends Service {

    /**
     * 下载凭证文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的凭证文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-certificate-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestCertificateFileStream downloadCertificateFileStream(
            @MultipartForm DubboRestCertificateFileStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 请求下载凭证文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载凭证文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-certificate-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestCertificateFileStreamVoucher(
            @MultipartForm DubboRestCertificateFileStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 通过凭证下载凭证文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 凭证文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-certificate-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestCertificateFileStream downloadCertificateFileStreamByVoucher(
            @MultipartForm VoucherIdWrapper voucherIdWrapper
    ) throws ServiceException;

    /**
     * 上传凭证文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-certificate-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadCertificateFileStream(@MultipartForm DubboRestCertificateFileStreamUploadInfo uploadInfo)
            throws ServiceException;
}
