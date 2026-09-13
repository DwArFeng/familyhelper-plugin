package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 导出模板操作处理器。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public interface DubboRestExportTemplateOperateHandler extends Handler {

    /**
     * 下载导出模板流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导出模板流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestExportTemplateStream downloadStream(DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载导出模板流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导出模板流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestStreamVoucher(DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载导出模板流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导出模板流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestExportTemplateStream downloadStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传导出模板流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadStream(DubboRestExportTemplateStreamUploadInfo uploadInfo) throws HandlerException;
}
