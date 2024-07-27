package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 图片节点文件操作处理器。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public interface DubboRestImageNodeOperateHandler extends Handler {

    /**
     * 下载图片节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的图片节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImageNodeFileStream downloadFileStream(DubboRestImageNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载图片节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载图片节点文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestFileStreamVoucher(DubboRestImageNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载图片节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 图片节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImageNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper) throws HandlerException;

    /**
     * 上传图片节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadFileStream(DubboRestImageNodeFileStreamUploadInfo uploadInfo) throws HandlerException;
}
