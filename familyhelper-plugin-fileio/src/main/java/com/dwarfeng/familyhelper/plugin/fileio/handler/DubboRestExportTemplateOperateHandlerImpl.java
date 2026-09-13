package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestExportTemplateStreamUploadInfo;
import com.dwarfeng.fileio.stack.bean.dto.TemplateStream;
import com.dwarfeng.fileio.stack.bean.dto.TemplateStreamDownloadInfo;
import com.dwarfeng.fileio.stack.bean.dto.TemplateStreamUploadInfo;
import com.dwarfeng.fileio.stack.bean.key.TaskSettingItemKey;
import com.dwarfeng.fileio.stack.service.ExportTemplateOperateService;
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
public class DubboRestExportTemplateOperateHandlerImpl implements DubboRestExportTemplateOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.export_template_stream_download}')}";

    private final ExportTemplateOperateService exportTemplateOperateService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    public DubboRestExportTemplateOperateHandlerImpl(
            ExportTemplateOperateService exportTemplateOperateService,
            VoucherService voucherService
    ) {
        this.exportTemplateOperateService = exportTemplateOperateService;
        this.voucherService = voucherService;
    }

    @Override
    public DubboRestExportTemplateStream downloadStream(DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            TemplateStream templateStream = exportTemplateOperateService.downloadStream(
                    new TemplateStreamDownloadInfo(resolveTaskSettingItemKey(downloadInfo))
            );

            if (templateStream == null) {
                return null;
            }

            return new DubboRestExportTemplateStream(
                    templateStream.getOriginName(), templateStream.getLength(), templateStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestStreamVoucher(DubboRestExportTemplateStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            String voucherContent = JSON.toJSONString(downloadInfo);
            LongIdKey voucher = voucherService.create(
                    new VoucherCreateInfo(voucherCategoryKey, null, voucherContent, "fileio 导出模板流下载凭证")
            );
            return new VoucherIdWrapper(voucher.getLongId());
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public DubboRestExportTemplateStream downloadStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            DubboRestExportTemplateStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestExportTemplateStreamDownloadInfo.class
            );

            TemplateStream templateStream = exportTemplateOperateService.downloadStream(
                    new TemplateStreamDownloadInfo(resolveTaskSettingItemKey(downloadInfo))
            );

            if (templateStream == null) {
                return null;
            }

            return new DubboRestExportTemplateStream(
                    templateStream.getOriginName(), templateStream.getLength(), templateStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadStream(DubboRestExportTemplateStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            exportTemplateOperateService.uploadStream(
                    new TemplateStreamUploadInfo(
                            new TaskSettingItemKey(uploadInfo.getTaskSettingId(), uploadInfo.getIdentifier()),
                            uploadInfo.getOriginName(),
                            uploadInfo.getLength(),
                            uploadInfo.getContent()
                    )
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private TaskSettingItemKey resolveTaskSettingItemKey(DubboRestExportTemplateStreamDownloadInfo downloadInfo) {
        return new TaskSettingItemKey(downloadInfo.getTaskSettingId(), downloadInfo.getIdentifier());
    }
}
