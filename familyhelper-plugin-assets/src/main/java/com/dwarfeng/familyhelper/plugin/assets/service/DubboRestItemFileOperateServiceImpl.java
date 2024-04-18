package com.dwarfeng.familyhelper.plugin.assets.service;

import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStream;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.DubboRestItemFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.assets.handler.DubboRestItemFileOperateHandler;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestItemFileOperateServiceImpl implements DubboRestItemFileOperateService {

    private final DubboRestItemFileOperateHandler dubboRestItemFileOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestItemFileOperateServiceImpl(
            DubboRestItemFileOperateHandler dubboRestItemFileOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestItemFileOperateHandler = dubboRestItemFileOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestItemFileStream downloadItemFileStream(DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestItemFileOperateHandler.downloadItemFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载项目文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestItemFileStreamVoucher(DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestItemFileOperateHandler.requestItemFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载项目文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestItemFileStream downloadItemFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestItemFileOperateHandler.downloadItemFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载项目文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadItemFileStream(DubboRestItemFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestItemFileOperateHandler.uploadItemFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传项目文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void updateItemFileStream(DubboRestItemFileStreamUpdateInfo updateInfo) throws ServiceException {
        try {
            dubboRestItemFileOperateHandler.updateItemFileStream(updateInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新项目文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
