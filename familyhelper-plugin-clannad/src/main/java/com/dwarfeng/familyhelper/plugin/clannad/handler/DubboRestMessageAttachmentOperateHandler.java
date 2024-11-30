package com.dwarfeng.familyhelper.plugin.clannad.handler;

import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.Handler;

/**
 * Dubbo rest 证件文件操作处理器
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface DubboRestMessageAttachmentOperateHandler extends Handler {

    /**
     * 下载留言附件流。
     *
     * @param downloadInfo 下载信息。
     * @return 下载的留言附件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestMessageAttachmentStream downloadMessageAttachmentStream(
            DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws HandlerException;

    /**
     * 请求下载留言附件流凭证。
     *
     * @param downloadInfo 下载信息。
     * @return 下载留言附件流凭证 ID 包装器。
     * @throws HandlerException 处理器异常。
     */
    VoucherIdWrapper requestMessageAttachmentStreamVoucher(DubboRestMessageAttachmentStreamDownloadInfo downloadInfo)
            throws HandlerException;

    /**
     * 通过凭证下载留言附件流。
     *
     * @param voucherIdWrapper 凭证 ID 包装器。
     * @return 留言附件流。
     * @throws HandlerException 处理器异常。
     */
    DubboRestMessageAttachmentStream downloadMessageAttachmentStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException;

    /**
     * 上传留言附件流。
     *
     * @param uploadInfo 上传信息。
     * @throws HandlerException 处理器异常。
     */
    void uploadMessageAttachmentStream(DubboRestMessageAttachmentStreamUploadInfo uploadInfo) throws HandlerException;

    /**
     * 更新留言附件流。
     *
     * @param updateInfo 更新信息。
     * @throws HandlerException 处理器异常。
     */
    void updateMessageAttachmentStream(DubboRestMessageAttachmentStreamUpdateInfo updateInfo) throws HandlerException;
}
