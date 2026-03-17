package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 文件列表节点操作处理器。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public interface DubboRestFileListNodeOperateHandler extends Handler {

    /**
     * 下载文件列表节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的文件列表节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestFileListNodeFileStream downloadFileStream(DubboRestFileListNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载文件列表节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载文件列表节点文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestFileStreamVoucher(DubboRestFileListNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载文件列表节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 文件列表节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestFileListNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传文件列表节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadFileStream(DubboRestFileListNodeFileStreamUploadInfo uploadInfo) throws HandlerException;

    /**
     * 更新文件列表节点文件流。
     *
     * @param updateInfo 更新信息。
     * @throws HandlerException 处理器异常。
     */
    void updateFileStream(DubboRestFileListNodeFileStreamUpdateInfo updateInfo) throws HandlerException;
}
