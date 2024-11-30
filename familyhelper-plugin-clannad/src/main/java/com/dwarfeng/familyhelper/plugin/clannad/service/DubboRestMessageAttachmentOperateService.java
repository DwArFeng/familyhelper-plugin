package com.dwarfeng.familyhelper.plugin.clannad.service;

import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUploadInfo;
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
 * Dubbo rest 留言附件操作服务。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
@Path("familyhelper-clannad/dubbo-rest-message-attachment-operate-service")
public interface DubboRestMessageAttachmentOperateService extends Service {

    /**
     * 下载留言附件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的留言附件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-message-attachment-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestMessageAttachmentStream downloadMessageAttachmentStream(
            @MultipartForm DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 请求下载留言附件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载留言附件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-message-attachment-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestMessageAttachmentStreamVoucher(
            @MultipartForm DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 通过凭证下载留言附件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 留言附件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-message-attachment-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestMessageAttachmentStream downloadMessageAttachmentStreamByVoucher(
            @MultipartForm VoucherIdWrapper voucherIdWrapper
    ) throws ServiceException;

    /**
     * 上传留言附件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-message-attachment-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadMessageAttachmentStream(@MultipartForm DubboRestMessageAttachmentStreamUploadInfo uploadInfo)
            throws ServiceException;

    /**
     * 更新留言附件流。
     *
     * @param updateInfo 更新信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("update-message-attachment-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void updateMessageAttachmentStream(@MultipartForm DubboRestMessageAttachmentStreamUpdateInfo updateInfo)
            throws ServiceException;
}
