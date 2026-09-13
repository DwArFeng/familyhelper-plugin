package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 导入文件操作服务。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
@Path("fileio/dubbo-rest-import-file-operate-service")
public interface DubboRestImportFileOperateService extends Service {

    /**
     * 下载导入文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导入文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-import-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImportFileStream downloadFileStream(@MultipartForm DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 请求下载导入文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导入文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-import-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestFileStreamVoucher(@MultipartForm DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载导入文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导入文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-import-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImportFileStream downloadFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传导入文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-import-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadFileStream(@MultipartForm DubboRestImportFileStreamUploadInfo uploadInfo) throws ServiceException;
}
