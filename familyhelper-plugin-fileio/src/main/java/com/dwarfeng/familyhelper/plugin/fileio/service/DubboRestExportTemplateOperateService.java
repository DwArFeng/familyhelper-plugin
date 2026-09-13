package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 导出模板操作服务。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
@Path("fileio/dubbo-rest-export-template-operate-service")
public interface DubboRestExportTemplateOperateService extends Service {

    /**
     * 下载导出模板流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导出模板流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-export-template-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestExportTemplateStream downloadStream(@MultipartForm DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 请求下载导出模板流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导出模板流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-export-template-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestStreamVoucher(@MultipartForm DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载导出模板流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导出模板流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-export-template-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestExportTemplateStream downloadStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传导出模板流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-export-template-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadStream(@MultipartForm DubboRestExportTemplateStreamUploadInfo uploadInfo) throws ServiceException;
}
