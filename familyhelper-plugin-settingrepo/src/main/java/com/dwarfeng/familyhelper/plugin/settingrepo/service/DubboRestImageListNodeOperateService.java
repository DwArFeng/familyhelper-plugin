package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 图片列表节点操作服务。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
@Path("settingrepo/dubbo-rest-image-list-node-operate-service")
public interface DubboRestImageListNodeOperateService extends Service {

    /**
     * 下载图片列表节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的图片列表节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-image-list-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImageListNodeFileStream downloadFileStream(
            @MultipartForm DubboRestImageListNodeFileStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 请求下载图片列表节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载图片列表节点文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-image-list-node-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestFileStreamVoucher(@MultipartForm DubboRestImageListNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载图片列表节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 图片列表节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-image-list-node-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImageListNodeFileStream downloadFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传图片列表节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-image-list-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadFileStream(@MultipartForm DubboRestImageListNodeFileStreamUploadInfo uploadInfo) throws ServiceException;

    /**
     * 更新图片列表节点文件流。
     *
     * @param updateInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("update-image-list-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void updateFileStream(@MultipartForm DubboRestImageListNodeFileStreamUpdateInfo updateInfo) throws ServiceException;
}
