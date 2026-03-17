package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.handler.DubboRestFileNodeOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

/**
 * Dubbo rest 文件节点操作服务实现。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
@Service
public class DubboRestFileNodeOperateServiceImpl implements DubboRestFileNodeOperateService {

    private final DubboRestFileNodeOperateHandler dubboRestFileNodeOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestFileNodeOperateServiceImpl(
            DubboRestFileNodeOperateHandler dubboRestFileNodeOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestFileNodeOperateHandler = dubboRestFileNodeOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestFileNodeFileStream downloadFileStream(DubboRestFileNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestFileNodeOperateHandler.downloadFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载文件节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestFileNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestFileNodeOperateHandler.requestFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载文件节点文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestFileNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestFileNodeOperateHandler.downloadFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载文件节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadFileStream(DubboRestFileNodeFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestFileNodeOperateHandler.uploadFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传文件节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
