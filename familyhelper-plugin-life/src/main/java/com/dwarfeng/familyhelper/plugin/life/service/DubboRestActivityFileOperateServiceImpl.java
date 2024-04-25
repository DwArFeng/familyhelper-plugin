package com.dwarfeng.familyhelper.plugin.life.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStream;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.life.handler.DubboRestActivityFileOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestActivityFileOperateServiceImpl implements DubboRestActivityFileOperateService {

    private final DubboRestActivityFileOperateHandler dubboRestActivityFileOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestActivityFileOperateServiceImpl(
            DubboRestActivityFileOperateHandler dubboRestActivityFileOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestActivityFileOperateHandler = dubboRestActivityFileOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestActivityFileStream downloadActivityFileStream(DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestActivityFileOperateHandler.downloadActivityFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载活动文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestActivityFileStreamVoucher(DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestActivityFileOperateHandler.requestActivityFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载活动文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestActivityFileStream downloadActivityFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestActivityFileOperateHandler.downloadActivityFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载活动文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadActivityFileStream(DubboRestActivityFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestActivityFileOperateHandler.uploadActivityFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传活动文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void updateActivityFileStream(DubboRestActivityFileStreamUpdateInfo updateInfo) throws ServiceException {
        try {
            dubboRestActivityFileOperateHandler.updateActivityFileStream(updateInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新活动文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
