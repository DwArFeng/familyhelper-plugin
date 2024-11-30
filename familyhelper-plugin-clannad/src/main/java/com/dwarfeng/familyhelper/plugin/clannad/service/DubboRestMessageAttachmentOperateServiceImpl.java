package com.dwarfeng.familyhelper.plugin.clannad.service;

import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.handler.DubboRestMessageAttachmentOperateHandler;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestMessageAttachmentOperateServiceImpl implements DubboRestMessageAttachmentOperateService {

    private final DubboRestMessageAttachmentOperateHandler dubboRestMessageAttachmentOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestMessageAttachmentOperateServiceImpl(
            DubboRestMessageAttachmentOperateHandler dubboRestMessageAttachmentOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestMessageAttachmentOperateHandler = dubboRestMessageAttachmentOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestMessageAttachmentStream downloadMessageAttachmentStream(
            DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws ServiceException {
        try {
            return dubboRestMessageAttachmentOperateHandler.downloadMessageAttachmentStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载留言附件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestMessageAttachmentStreamVoucher(
            DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws ServiceException {
        try {
            return dubboRestMessageAttachmentOperateHandler.requestMessageAttachmentStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载留言附件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestMessageAttachmentStream downloadMessageAttachmentStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestMessageAttachmentOperateHandler.downloadMessageAttachmentStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载留言附件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadMessageAttachmentStream(DubboRestMessageAttachmentStreamUploadInfo uploadInfo)
            throws ServiceException {
        try {
            dubboRestMessageAttachmentOperateHandler.uploadMessageAttachmentStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传留言附件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void updateMessageAttachmentStream(DubboRestMessageAttachmentStreamUpdateInfo updateInfo)
            throws ServiceException {
        try {
            dubboRestMessageAttachmentOperateHandler.updateMessageAttachmentStream(updateInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新留言附件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
