package com.dwarfeng.familyhelper.plugin.assets.service;

import com.dwarfeng.familyhelper.plugin.assets.bean.dto.*;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 项目文件操作服务。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
@Path("familyhelper-assets/dubbo-rest-item-file-operate-service")
public interface DubboRestItemFileOperateService extends Service {

    /**
     * 下载项目文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的项目文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-item-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestItemFileStream downloadItemFileStream(@MultipartForm DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 请求下载项目文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载项目文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-item-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestItemFileStreamVoucher(@MultipartForm DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载项目文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 项目文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-item-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestItemFileStream downloadItemFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传项目文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-item-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadItemFileStream(@MultipartForm DubboRestItemFileStreamUploadInfo uploadInfo) throws ServiceException;

    /**
     * 更新项目文件流。
     *
     * @param updateInfo 更新信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("update-item-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void updateItemFileStream(@MultipartForm DubboRestItemFileStreamUpdateInfo updateInfo) throws ServiceException;
}
