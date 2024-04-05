package com.dwarfeng.familyhelper.plugin.assets.handler;

import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.familyhelper.assets.impl.handler.OperateHandlerValidator;
import com.dwarfeng.familyhelper.assets.impl.util.FtpConstants;
import com.dwarfeng.familyhelper.assets.sdk.util.Constants;
import com.dwarfeng.familyhelper.assets.stack.bean.entity.ItemFileInfo;
import com.dwarfeng.familyhelper.assets.stack.service.ItemFileInfoMaintainService;
import com.dwarfeng.familyhelper.plugin.assets.bean.dto.*;
import com.dwarfeng.ftp.handler.FtpHandler;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.voucher.stack.bean.dto.VoucherCreateInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectResult;
import com.dwarfeng.voucher.stack.service.VoucherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

@Component
public class DubboRestItemFileOperateHandlerImpl implements DubboRestItemFileOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.item_file_stream_download}')}";

    private final ItemFileInfoMaintainService itemFileInfoMaintainService;
    private final VoucherService voucherService;

    private final FtpHandler ftpHandler;

    private final KeyGenerator<LongIdKey> keyGenerator;

    private final OperateHandlerValidator operateHandlerValidator;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    public DubboRestItemFileOperateHandlerImpl(
            ItemFileInfoMaintainService itemFileInfoMaintainService,
            VoucherService voucherService,
            FtpHandler ftpHandler,
            KeyGenerator<LongIdKey> keyGenerator,
            OperateHandlerValidator operateHandlerValidator
    ) {
        this.itemFileInfoMaintainService = itemFileInfoMaintainService;
        this.voucherService = voucherService;
        this.ftpHandler = ftpHandler;
        this.keyGenerator = keyGenerator;
        this.operateHandlerValidator = operateHandlerValidator;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestItemFileStream downloadItemFileStream(DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey itemFileKey = new LongIdKey(downloadInfo.getItemFileLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认项目文件存在。
            operateHandlerValidator.makeSureItemFileExists(itemFileKey);

            // 获取项目文件对应的项目，并确认用户有权限操作项目。
            ItemFileInfo itemFileInfo = itemFileInfoMaintainService.get(itemFileKey);
            operateHandlerValidator.makeSureUserInspectPermittedForItem(userKey, itemFileInfo.getItemKey());

            // 获取项目文件的内容。
            InputStream content = ftpHandler.openInputStream(FtpConstants.PATH_ITEM_FILE, getFileName(itemFileKey));

            // 更新文件的查看时间。
            itemFileInfo.setInspectedDate(new Date());
            itemFileInfoMaintainService.update(itemFileInfo);

            // 拼接 DubboRestItemFileStream 并返回。
            return new DubboRestItemFileStream(itemFileInfo.getOriginName(), itemFileInfo.getLength(), content);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public VoucherIdWrapper requestItemFileStreamVoucher(DubboRestItemFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey itemFileKey = new LongIdKey(downloadInfo.getItemFileLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认项目文件存在。
            operateHandlerValidator.makeSureItemFileExists(itemFileKey);

            // 获取项目文件对应的项目，并确认用户有权限操作项目。
            ItemFileInfo itemFileInfo = itemFileInfoMaintainService.get(itemFileKey);
            operateHandlerValidator.makeSureUserInspectPermittedForItem(userKey, itemFileInfo.getItemKey());

            // 生成凭证。
            String voucherContent = userKey.getStringId() + "@" + itemFileKey.getLongId();
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
    public DubboRestItemFileStream downloadItemFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            // 查看凭证。
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            // 解析凭证内容，获取用户键和文件键。
            String[] split = voucherContent.split("@");
            StringIdKey userKey = new StringIdKey(split[0]);
            LongIdKey itemFileKey = new LongIdKey(Long.parseLong(split[1]));

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认项目文件存在。
            operateHandlerValidator.makeSureItemFileExists(itemFileKey);

            // 获取项目文件对应的项目，并确认用户有权限操作项目。
            ItemFileInfo itemFileInfo = itemFileInfoMaintainService.get(itemFileKey);
            operateHandlerValidator.makeSureUserInspectPermittedForItem(userKey, itemFileInfo.getItemKey());

            // 获取项目文件的内容。
            InputStream content = ftpHandler.openInputStream(FtpConstants.PATH_ITEM_FILE, getFileName(itemFileKey));

            // 更新文件的查看时间。
            itemFileInfo.setInspectedDate(new Date());
            itemFileInfoMaintainService.update(itemFileInfo);

            // 拼接 DubboRestItemFileStream 并返回。
            return new DubboRestItemFileStream(itemFileInfo.getOriginName(), itemFileInfo.getLength(), content);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadItemFileStream(DubboRestItemFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(uploadInfo.getUserStringId());
            LongIdKey itemKey = new LongIdKey(uploadInfo.getItemLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认项目文件所属的项目存在。
            operateHandlerValidator.makeSureItemExists(itemKey);

            // 确认用户有权限操作项目。
            operateHandlerValidator.makeSureUserModifyPermittedForItem(userKey, itemKey);

            // 分配主键。
            LongIdKey itemFileKey = keyGenerator.generate();

            // 项目文件内容并存储（覆盖）。
            InputStream cin = uploadInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.PATH_ITEM_FILE, getFileName(itemFileKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 根据 itemFileStreamUploadInfo 构造 ItemFileInfo，插入或更新。
            Date currentDate = new Date();
            // 映射属性。
            ItemFileInfo itemFileInfo = new ItemFileInfo();
            itemFileInfo.setKey(itemFileKey);
            itemFileInfo.setItemKey(itemKey);
            itemFileInfo.setOriginName(uploadInfo.getOriginName());
            itemFileInfo.setLength(uploadInfo.getLength());
            itemFileInfo.setCreatedDate(currentDate);
            itemFileInfo.setModifiedDate(currentDate);
            itemFileInfo.setInspectedDate(currentDate);
            itemFileInfo.setRemark("通过 familyhelper-assets 服务上传/更新项目文件");
            itemFileInfoMaintainService.insertOrUpdate(itemFileInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void updateItemFileStream(DubboRestItemFileStreamUpdateInfo updateInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(updateInfo.getUserStringId());
            LongIdKey itemFileKey = new LongIdKey(updateInfo.getItemFileLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认项目文件信息存在。
            operateHandlerValidator.makeSureItemFileExists(itemFileKey);

            // 确认用户有权限操作项目文件信息。
            operateHandlerValidator.makeSureUserModifyPermittedForItemFileInfo(userKey, itemFileKey);

            // 项目文件内容并存储（覆盖）。
            InputStream cin = updateInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.PATH_ITEM_FILE, getFileName(itemFileKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 根据 itemFileStreamUpdateInfo 更新字段。
            ItemFileInfo itemFileInfo = itemFileInfoMaintainService.get(itemFileKey);
            itemFileInfo.setOriginName(updateInfo.getOriginName());
            itemFileInfo.setLength(updateInfo.getLength());
            itemFileInfo.setModifiedDate(new Date());
            itemFileInfoMaintainService.update(itemFileInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private String getFileName(LongIdKey itemFileKey) {
        return Long.toString(itemFileKey.getLongId());
    }
}
