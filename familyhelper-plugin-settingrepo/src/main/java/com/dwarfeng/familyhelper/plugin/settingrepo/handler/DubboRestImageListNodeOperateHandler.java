package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 图片列表节点操作处理器。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
public interface DubboRestImageListNodeOperateHandler extends Handler {

    /**
     * 下载图片列表节点文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的图片列表节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImageListNodeFileStream downloadFileStream(DubboRestImageListNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载图片列表节点文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载图片列表节点文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestFileStreamVoucher(DubboRestImageListNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载图片列表节点文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 图片列表节点文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImageListNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传图片列表节点文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadFileStream(DubboRestImageListNodeFileStreamUploadInfo uploadInfo) throws HandlerException;

    /**
     * 更新图片列表节点文件流。
     *
     * @param updateInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void updateFileStream(DubboRestImageListNodeFileStreamUpdateInfo updateInfo) throws HandlerException;
}
