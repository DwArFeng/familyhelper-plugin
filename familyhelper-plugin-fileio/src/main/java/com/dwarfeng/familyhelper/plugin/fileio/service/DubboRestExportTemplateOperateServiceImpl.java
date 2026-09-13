package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.handler.DubboRestExportTemplateOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestExportTemplateOperateServiceImpl implements DubboRestExportTemplateOperateService {

    private final DubboRestExportTemplateOperateHandler dubboRestExportTemplateOperateHandler;
    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestExportTemplateOperateServiceImpl(
            DubboRestExportTemplateOperateHandler dubboRestExportTemplateOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestExportTemplateOperateHandler = dubboRestExportTemplateOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestExportTemplateStream downloadStream(DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestExportTemplateOperateHandler.downloadStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载导出模板流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestStreamVoucher(DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestExportTemplateOperateHandler.requestStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载导出模板流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestExportTemplateStream downloadStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestExportTemplateOperateHandler.downloadStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载导出模板流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadStream(DubboRestExportTemplateStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestExportTemplateOperateHandler.uploadStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传导出模板流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
