package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStreamUploadInfo;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 导入模板操作处理器。
 *
 * @author DwArFeng
 * @since 1.8.0
 */
public interface DubboRestImportTemplateOperateHandler extends Handler {

    /**
     * 下载导入模板流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的导入模板流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImportTemplateStream downloadStream(DubboRestImportTemplateStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 请求下载导入模板流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载导入模板流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestStreamVoucher(DubboRestImportTemplateStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载导入模板流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 导入模板流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestImportTemplateStream downloadStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传导入模板流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadStream(DubboRestImportTemplateStreamUploadInfo uploadInfo) throws HandlerException;
}
