package com.dwarfeng.familyhelper.plugin.fileio.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.handler.DubboRestExportFileOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestExportFileOperateServiceImpl implements DubboRestExportFileOperateService {

    private final DubboRestExportFileOperateHandler dubboRestExportFileOperateHandler;
    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestExportFileOperateServiceImpl(
            DubboRestExportFileOperateHandler dubboRestExportFileOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestExportFileOperateHandler = dubboRestExportFileOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestExportFileStream downloadFileStream(DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestExportFileOperateHandler.downloadFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载导出文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestExportFileOperateHandler.requestFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载导出文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestExportFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestExportFileOperateHandler.downloadFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载导出文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
