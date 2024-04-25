package com.dwarfeng.familyhelper.plugin.clannad.handler;

import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 证件文件操作处理器
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public interface DubboRestCertificateFileOperateHandler extends Handler {

    /**
     * 下载凭证文件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的凭证文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestCertificateFileStream downloadCertificateFileStream(
            DubboRestCertificateFileStreamDownloadInfo downloadInfo
    ) throws HandlerException;

    /**
     * 请求下载凭证文件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载凭证文件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestCertificateFileStreamVoucher(DubboRestCertificateFileStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载凭证文件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 凭证文件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestCertificateFileStream downloadCertificateFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传凭证文件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadCertificateFileStream(DubboRestCertificateFileStreamUploadInfo uploadInfo) throws HandlerException;
}
