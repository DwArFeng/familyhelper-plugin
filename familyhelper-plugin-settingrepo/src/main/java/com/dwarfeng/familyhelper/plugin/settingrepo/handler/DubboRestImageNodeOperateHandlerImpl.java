package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageNodeFileStream;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageNodeFileStreamDownloadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.service.ImageNodeOperateService;
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

import java.io.InputStream;

@Component
public class DubboRestImageNodeOperateHandlerImpl implements DubboRestImageNodeOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.image_node_file_stream_download}')}";

    private final ImageNodeOperateService imageNodeOperateService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestImageNodeOperateHandlerImpl(
            ImageNodeOperateService imageNodeOperateService,
            VoucherService voucherService
    ) {
        this.imageNodeOperateService = imageNodeOperateService;
        this.voucherService = voucherService;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestImageNodeFileStream downloadFileStream(
            DubboRestImageNodeFileStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();

            // 调用维护服务下载图片节点文件流。
            ImageNodeFileStream imageNodeFileStream = imageNodeOperateService.downloadFileStream(
                    new ImageNodeFileStreamDownloadInfo(category, args)
            );

            // 如果 imageNodeFileStream 为 null, 直接返回 null。
            if (imageNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestImageNodeFileStream 并返回。
            return new DubboRestImageNodeFileStream(
                    imageNodeFileStream.getOriginName(), imageNodeFileStream.getLength(),
                    imageNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestImageNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 生成凭证。
            String voucherContent = JSON.toJSONString(downloadInfo);
            LongIdKey voucher = voucherService.create(
                    new VoucherCreateInfo(voucherCategoryKey, null, voucherContent, "我是备注")
            );

            // 返回凭证 ID。
            return new VoucherIdWrapper(voucher.getLongId());
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestImageNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            // 查看凭证。
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            // 解析凭证内容，获取用户键和文件键。
            DubboRestImageNodeFileStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestImageNodeFileStreamDownloadInfo.class
            );

            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();

            // 调用维护服务下载图片节点文件流。
            ImageNodeFileStream imageNodeFileStream = imageNodeOperateService.downloadFileStream(
                    new ImageNodeFileStreamDownloadInfo(category, args)
            );

            // 如果 imageNodeFileStream 为 null, 直接返回 null。
            if (imageNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestImageNodeFileStream 并返回。
            return new DubboRestImageNodeFileStream(
                    imageNodeFileStream.getOriginName(), imageNodeFileStream.getLength(),
                    imageNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadFileStream(DubboRestImageNodeFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = uploadInfo.getCategory();
            String[] args = uploadInfo.getArgs();
            String originName = uploadInfo.getOriginName();
            long length = uploadInfo.getLength();
            InputStream content = uploadInfo.getContent();

            // 构造 ImageNodeFileStreamUploadInfo。
            ImageNodeFileStreamUploadInfo imageNodeFileStreamUploadInfo = new ImageNodeFileStreamUploadInfo(
                    category, args, originName, length, content
            );

            // 调用维护服务上传图片节点文件流。
            imageNodeOperateService.uploadFileStream(imageNodeFileStreamUploadInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }
}
