package com.dwarfeng.familyhelper.plugin.clannad.service;

import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.handler.DubboRestCertificateFileOperateHandler;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestCertificateFileOperateServiceImpl implements DubboRestCertificateFileOperateService {

    private final DubboRestCertificateFileOperateHandler dubboRestCertificateFileOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestCertificateFileOperateServiceImpl(
            DubboRestCertificateFileOperateHandler dubboRestCertificateFileOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestCertificateFileOperateHandler = dubboRestCertificateFileOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestCertificateFileStream downloadCertificateFileStream(
            DubboRestCertificateFileStreamDownloadInfo downloadInfo
    ) throws ServiceException {
        try {
            return dubboRestCertificateFileOperateHandler.downloadCertificateFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载凭证文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestCertificateFileStreamVoucher(
            DubboRestCertificateFileStreamDownloadInfo downloadInfo
    ) throws ServiceException {
        try {
            return dubboRestCertificateFileOperateHandler.requestCertificateFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载凭证文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestCertificateFileStream downloadCertificateFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestCertificateFileOperateHandler.downloadCertificateFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载凭证文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadCertificateFileStream(DubboRestCertificateFileStreamUploadInfo uploadInfo)
            throws ServiceException {
        try {
            dubboRestCertificateFileOperateHandler.uploadCertificateFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传凭证文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
