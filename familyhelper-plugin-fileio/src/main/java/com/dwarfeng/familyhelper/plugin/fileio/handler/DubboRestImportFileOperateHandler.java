package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 导入文件操作处理器。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public interface DubboRestImportFileOperateHandler extends Handler {

    /**
     * 下载导入文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导入文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImportFileStream downloadFileStream(DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载导入文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导入文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestFileStreamVoucher(DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载导入文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导入文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImportFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传导入文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadFileStream(DubboRestImportFileStreamUploadInfo uploadInfo) throws HandlerException;
}
