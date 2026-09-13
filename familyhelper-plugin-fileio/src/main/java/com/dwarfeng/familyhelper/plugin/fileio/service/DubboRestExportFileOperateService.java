package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStreamDownloadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 导出文件操作服务。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
@Path("fileio/dubbo-rest-export-file-operate-service")
public interface DubboRestExportFileOperateService extends Service {

    /**
     * 下载导出文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导出文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-export-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestExportFileStream downloadFileStream(@MultipartForm DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 请求下载导出文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导出文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-export-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestFileStreamVoucher(@MultipartForm DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载导出文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导出文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-export-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestExportFileStream downloadFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;
}
