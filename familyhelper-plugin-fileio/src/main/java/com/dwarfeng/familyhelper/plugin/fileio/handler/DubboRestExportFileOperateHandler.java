package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStreamDownloadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 导出文件操作处理器。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public interface DubboRestExportFileOperateHandler extends Handler {

    /**
     * 下载导出文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导出文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestExportFileStream downloadFileStream(DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载导出文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导出文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestFileStreamVoucher(DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载导出文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导出文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestExportFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;
}
