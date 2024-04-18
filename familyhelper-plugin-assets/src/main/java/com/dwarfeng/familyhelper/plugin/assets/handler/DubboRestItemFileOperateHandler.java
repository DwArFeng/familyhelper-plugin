package com.dwarfeng.familyhelper.plugin.assets.handler;

import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStream;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 项目文件操作处理器。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public interface DubboRestItemFileOperateHandler extends Handler {

    /**
     * 下载项目文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的项目文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestItemFileStream downloadItemFileStream(DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载项目文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载项目文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestItemFileStreamVoucher(DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载项目文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 项目文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestItemFileStream downloadItemFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传项目文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadItemFileStream(DubboRestItemFileStreamUploadInfo uploadInfo) throws HandlerException;

    /**
     * 更新项目文件流。
     *
     * @param updateInfo 更新信息。
     * @throws HandlerException 处理器异常。
     */
    void updateItemFileStream(DubboRestItemFileStreamUpdateInfo updateInfo) throws HandlerException;
}
