package com.dwarfeng.familyhelper.plugin.life.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStream;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 活动文件操作处理器。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public interface DubboRestActivityFileOperateHandler extends Handler {

    /**
     * 下载活动文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的活动文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestActivityFileStream downloadActivityFileStream(DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载活动文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载活动文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestActivityFileStreamVoucher(DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载活动文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 活动文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestActivityFileStream downloadActivityFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传活动文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadActivityFileStream(DubboRestActivityFileStreamUploadInfo uploadInfo) throws HandlerException;

    /**
     * 更新活动文件流。
     *
     * @param updateInfo 更新信息。
     * @throws HandlerException 处理器异常。
     */
    void updateActivityFileStream(DubboRestActivityFileStreamUpdateInfo updateInfo) throws HandlerException;
}
