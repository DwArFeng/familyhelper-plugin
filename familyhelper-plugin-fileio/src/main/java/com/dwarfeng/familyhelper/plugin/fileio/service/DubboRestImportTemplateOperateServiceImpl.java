package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportTemplateStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.handler.DubboRestImportTemplateOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestImportTemplateOperateServiceImpl implements DubboRestImportTemplateOperateService {

    private final DubboRestImportTemplateOperateHandler dubboRestImportTemplateOperateHandler;
    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestImportTemplateOperateServiceImpl(
            DubboRestImportTemplateOperateHandler dubboRestImportTemplateOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestImportTemplateOperateHandler = dubboRestImportTemplateOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestImportTemplateStream downloadStream(DubboRestImportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImportTemplateOperateHandler.downloadStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载导入模板流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestStreamVoucher(DubboRestImportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImportTemplateOperateHandler.requestStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载导入模板流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestImportTemplateStream downloadStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestImportTemplateOperateHandler.downloadStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载导入模板流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadStream(DubboRestImportTemplateStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestImportTemplateOperateHandler.uploadStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传导入模板流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
