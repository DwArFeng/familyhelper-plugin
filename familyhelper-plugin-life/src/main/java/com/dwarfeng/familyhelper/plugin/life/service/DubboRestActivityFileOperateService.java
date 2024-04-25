package com.dwarfeng.familyhelper.plugin.life.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStream;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 活动文件操作服务。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
@Path("familyhelper-life/dubbo-rest-activity-file-operate-service")
public interface DubboRestActivityFileOperateService extends Service {

    /**
     * 下载活动文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的活动文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-activity-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestActivityFileStream downloadActivityFileStream(@MultipartForm DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 请求下载活动文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载活动文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-activity-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestActivityFileStreamVoucher(@MultipartForm DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载活动文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 活动文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-activity-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestActivityFileStream downloadActivityFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传活动文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-activity-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadActivityFileStream(@MultipartForm DubboRestActivityFileStreamUploadInfo uploadInfo) throws ServiceException;

    /**
     * 更新活动文件流。
     *
     * @param updateInfo 更新信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("update-activity-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void updateActivityFileStream(@MultipartForm DubboRestActivityFileStreamUpdateInfo updateInfo) throws ServiceException;
}
