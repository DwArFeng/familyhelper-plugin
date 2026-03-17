package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 文件列表节点操作服务。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
@Path("settingrepo/dubbo-rest-file-list-node-operate-service")
public interface DubboRestFileListNodeOperateService extends Service {

    /**
     * 下载文件列表节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的文件列表节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-file-list-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestFileListNodeFileStream downloadFileStream(
            @MultipartForm DubboRestFileListNodeFileStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 请求下载文件列表节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载文件列表节点文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-file-list-node-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestFileStreamVoucher(@MultipartForm DubboRestFileListNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载文件列表节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 文件列表节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-file-list-node-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestFileListNodeFileStream downloadFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传文件列表节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-file-list-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadFileStream(@MultipartForm DubboRestFileListNodeFileStreamUploadInfo uploadInfo) throws ServiceException;

    /**
     * 更新文件列表节点文件流。
     *
     * @param updateInfo 更新信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("update-file-list-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void updateFileStream(@MultipartForm DubboRestFileListNodeFileStreamUpdateInfo updateInfo) throws ServiceException;
}
