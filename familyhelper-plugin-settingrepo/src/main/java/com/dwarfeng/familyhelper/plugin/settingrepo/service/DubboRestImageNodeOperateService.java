package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 图片节点操作服务。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
@Path("settingrepo/dubbo-rest-image-node-operate-service")
public interface DubboRestImageNodeOperateService extends Service {

    /**
     * 下载图片节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的图片节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-image-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImageNodeFileStream downloadFileStream(
            @MultipartForm DubboRestImageNodeFileStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 请求下载图片节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载图片节点文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-image-node-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestFileStreamVoucher(@MultipartForm DubboRestImageNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载图片节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 图片节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-image-node-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestImageNodeFileStream downloadFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传图片节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-image-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadFileStream(@MultipartForm DubboRestImageNodeFileStreamUploadInfo uploadInfo) throws ServiceException;
}
