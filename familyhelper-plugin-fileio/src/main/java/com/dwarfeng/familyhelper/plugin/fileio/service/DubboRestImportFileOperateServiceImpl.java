package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.handler.DubboRestImportFileOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestImportFileOperateServiceImpl implements DubboRestImportFileOperateService {

    private final DubboRestImportFileOperateHandler dubboRestImportFileOperateHandler;
    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestImportFileOperateServiceImpl(
            DubboRestImportFileOperateHandler dubboRestImportFileOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestImportFileOperateHandler = dubboRestImportFileOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestImportFileStream downloadFileStream(DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImportFileOperateHandler.downloadFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载导入文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImportFileOperateHandler.requestFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载导入文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestImportFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestImportFileOperateHandler.downloadFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载导入文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadFileStream(DubboRestImportFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestImportFileOperateHandler.uploadFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传导入文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
