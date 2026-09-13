package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 导入模板操作服务。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
@Path("fileio/dubbo-rest-import-template-operate-service")
public interface DubboRestImportTemplateOperateService extends Service {

    /**
     * 下载导入模板流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导入模板流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-import-template-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImportTemplateStream downloadStream(@MultipartForm DubboRestImportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 请求下载导入模板流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导入模板流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-import-template-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestStreamVoucher(@MultipartForm DubboRestImportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载导入模板流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导入模板流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-import-template-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImportTemplateStream downloadStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传导入模板流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-import-template-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadStream(@MultipartForm DubboRestImportTemplateStreamUploadInfo uploadInfo) throws ServiceException;
}
