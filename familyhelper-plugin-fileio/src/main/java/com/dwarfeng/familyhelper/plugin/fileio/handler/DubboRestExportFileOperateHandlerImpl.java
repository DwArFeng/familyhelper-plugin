package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportFileStreamDownloadInfo;
import com.dwarfeng.fileio.stack.bean.dto.FileStream;
import com.dwarfeng.fileio.stack.bean.dto.FileStreamDownloadInfo;
import com.dwarfeng.fileio.stack.bean.key.TaskItemKey;
import com.dwarfeng.fileio.stack.service.ExportService;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.voucher.stack.bean.dto.VoucherCreateInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectResult;
import com.dwarfeng.voucher.stack.service.VoucherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DubboRestExportFileOperateHandlerImpl implements DubboRestExportFileOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.export_file_stream_download}')}";

    private final ExportService exportService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    public DubboRestExportFileOperateHandlerImpl(
            ExportService exportService,
            VoucherService voucherService
    ) {
        this.exportService = exportService;
        this.voucherService = voucherService;
    }

    @Override
    public DubboRestExportFileStream downloadFileStream(DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            FileStream fileStream = exportService.downloadFileStream(
                    new FileStreamDownloadInfo(resolveTaskItemKey(downloadInfo))
            );

            if (fileStream == null) {
                return null;
            }

            return new DubboRestExportFileStream(
                    fileStream.getOriginName(), fileStream.getLength(), fileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestExportFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            String voucherContent = JSON.toJSONString(downloadInfo);
            LongIdKey voucher = voucherService.create(
                    new VoucherCreateInfo(voucherCategoryKey, null, voucherContent, "fileio 导出文件流下载凭证")
            );
            return new VoucherIdWrapper(voucher.getLongId());
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public DubboRestExportFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            DubboRestExportFileStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestExportFileStreamDownloadInfo.class
            );

            FileStream fileStream = exportService.downloadFileStream(
                    new FileStreamDownloadInfo(resolveTaskItemKey(downloadInfo))
            );

            if (fileStream == null) {
                return null;
            }

            return new DubboRestExportFileStream(
                    fileStream.getOriginName(), fileStream.getLength(), fileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private TaskItemKey resolveTaskItemKey(DubboRestExportFileStreamDownloadInfo downloadInfo) {
        return new TaskItemKey(downloadInfo.getTaskId(), downloadInfo.getIdentifier());
    }
}
