package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 文件节点文件操作处理器。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public interface DubboRestFileNodeOperateHandler extends Handler {

    /**
     * 下载文件节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的文件节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestFileNodeFileStream downloadFileStream(DubboRestFileNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载文件节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载文件节点文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestFileStreamVoucher(DubboRestFileNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载文件节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 文件节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestFileNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper) throws HandlerException;

    /**
     * 上传文件节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadFileStream(DubboRestFileNodeFileStreamUploadInfo uploadInfo) throws HandlerException;
}
