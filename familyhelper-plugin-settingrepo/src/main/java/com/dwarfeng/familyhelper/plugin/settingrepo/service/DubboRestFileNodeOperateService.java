package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dubbo rest 文件节点操作服务。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
@Path("settingrepo/dubbo-rest-file-node-operate-service")
public interface DubboRestFileNodeOperateService extends Service {

    /**
     * 下载文件节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的文件节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-file-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestFileNodeFileStream downloadFileStream(
            @MultipartForm DubboRestFileNodeFileStreamDownloadInfo downloadInfo
    ) throws ServiceException;

    /**
     * 请求下载文件节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载文件节点文件流凭证 ID 包装器。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("request-file-node-file-stream-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    VoucherIdWrapper requestFileStreamVoucher(@MultipartForm DubboRestFileNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException;

    /**
     * 通过凭证下载文件节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 文件节点文件流。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("download-file-node-file-stream-by-voucher")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    DubboRestFileNodeFileStream downloadFileStreamByVoucher(@MultipartForm VoucherIdWrapper voucherIdWrapper)
            throws ServiceException;

    /**
     * 上传文件节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws ServiceException 服务异常。
     */
    @POST
    @Path("upload-file-node-file-stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void uploadFileStream(@MultipartForm DubboRestFileNodeFileStreamUploadInfo uploadInfo) throws ServiceException;
}
