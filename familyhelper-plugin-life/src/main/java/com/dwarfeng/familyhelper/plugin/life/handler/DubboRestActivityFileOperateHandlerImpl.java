package com.dwarfeng.familyhelper.plugin.life.handler;

import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.familyhelper.life.impl.handler.HandlerValidator;
import com.dwarfeng.familyhelper.life.impl.util.FtpConstants;
import com.dwarfeng.familyhelper.life.sdk.util.Constants;
import com.dwarfeng.familyhelper.life.stack.bean.entity.ActivityFileInfo;
import com.dwarfeng.familyhelper.life.stack.service.ActivityFileInfoMaintainService;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStream;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.life.bean.dto.DubboRestActivityFileStreamUploadInfo;
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
public class DubboRestActivityFileOperateHandlerImpl implements DubboRestActivityFileOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.activity_file_stream_download}')}";

    private final ActivityFileInfoMaintainService activityFileInfoMaintainService;
    private final VoucherService voucherService;

    private final FtpHandler ftpHandler;

    private final KeyGenerator<LongIdKey> keyGenerator;

    private final HandlerValidator handlerValidator;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestActivityFileOperateHandlerImpl(
            ActivityFileInfoMaintainService activityFileInfoMaintainService,
            VoucherService voucherService,
            FtpHandler ftpHandler,
            KeyGenerator<LongIdKey> keyGenerator,
            HandlerValidator handlerValidator
    ) {
        this.activityFileInfoMaintainService = activityFileInfoMaintainService;
        this.voucherService = voucherService;
        this.ftpHandler = ftpHandler;
        this.keyGenerator = keyGenerator;
        this.handlerValidator = handlerValidator;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestActivityFileStream downloadActivityFileStream(DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey activityFileKey = new LongIdKey(downloadInfo.getActivityFileLongId());

            // 确认用户存在。
            handlerValidator.makeSureUserExists(userKey);

            // 确认活动文件存在。
            handlerValidator.makeSureActivityFileExists(activityFileKey);

            // 获取活动文件对应的活动，并确认用户有权限操作活动。
            ActivityFileInfo activityFileInfo = activityFileInfoMaintainService.get(activityFileKey);
            handlerValidator.makeSureUserInspectPermittedForActivity(userKey, activityFileInfo.getActivityKey());

            // 获取活动文件的内容。
            InputStream content = ftpHandler.openInputStream(
                    FtpConstants.FILE_PATHS_ACTIVITY_FILE, getFileName(activityFileKey)
            );

            // 更新文件的查看时间。
            activityFileInfo.setInspectedDate(new Date());
            activityFileInfoMaintainService.update(activityFileInfo);

            // 拼接 DubboRestActivityFileStream 并返回。
            return new DubboRestActivityFileStream(
                    activityFileInfo.getOriginName(), activityFileInfo.getLength(), content
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public VoucherIdWrapper requestActivityFileStreamVoucher(DubboRestActivityFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey activityFileKey = new LongIdKey(downloadInfo.getActivityFileLongId());

            // 确认用户存在。
            handlerValidator.makeSureUserExists(userKey);

            // 确认活动文件存在。
            handlerValidator.makeSureActivityFileExists(activityFileKey);

            // 获取活动文件对应的活动，并确认用户有权限操作活动。
            ActivityFileInfo activityFileInfo = activityFileInfoMaintainService.get(activityFileKey);
            handlerValidator.makeSureUserInspectPermittedForActivity(userKey, activityFileInfo.getActivityKey());

            // 生成凭证。
            String voucherContent = userKey.getStringId() + "@" + activityFileKey.getLongId();
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
    public DubboRestActivityFileStream downloadActivityFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
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
            LongIdKey activityFileKey = new LongIdKey(Long.parseLong(split[1]));

            // 确认用户存在。
            handlerValidator.makeSureUserExists(userKey);

            // 确认活动文件存在。
            handlerValidator.makeSureActivityFileExists(activityFileKey);

            // 获取活动文件对应的活动，并确认用户有权限操作活动。
            ActivityFileInfo activityFileInfo = activityFileInfoMaintainService.get(activityFileKey);
            handlerValidator.makeSureUserInspectPermittedForActivity(userKey, activityFileInfo.getActivityKey());

            // 获取活动文件的内容。
            InputStream content = ftpHandler.openInputStream(
                    FtpConstants.FILE_PATHS_ACTIVITY_FILE, getFileName(activityFileKey)
            );

            // 更新文件的查看时间。
            activityFileInfo.setInspectedDate(new Date());
            activityFileInfoMaintainService.update(activityFileInfo);

            // 拼接 DubboRestActivityFileStream 并返回。
            return new DubboRestActivityFileStream(
                    activityFileInfo.getOriginName(), activityFileInfo.getLength(), content
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadActivityFileStream(DubboRestActivityFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(uploadInfo.getUserStringId());
            LongIdKey activityKey = new LongIdKey(uploadInfo.getActivityLongId());

            // 确认用户存在。
            handlerValidator.makeSureUserExists(userKey);

            // 确认活动文件所属的活动存在。
            handlerValidator.makeSureActivityExists(activityKey);

            // 确认用户有权限操作活动。
            handlerValidator.makeSureUserModifyPermittedForActivity(userKey, activityKey);

            // 分配主键。
            LongIdKey activityFileKey = keyGenerator.generate();

            // 活动文件内容并存储（覆盖）。
            InputStream cin = uploadInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.FILE_PATHS_ACTIVITY_FILE, getFileName(activityFileKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 根据 activityFileStreamUploadInfo 构造 ActivityFileInfo，插入或更新。
            Date currentDate = new Date();
            // 映射属性。
            ActivityFileInfo activityFileInfo = new ActivityFileInfo();
            activityFileInfo.setKey(activityFileKey);
            activityFileInfo.setActivityKey(activityKey);
            activityFileInfo.setOriginName(uploadInfo.getOriginName());
            activityFileInfo.setLength(uploadInfo.getLength());
            activityFileInfo.setCreatedDate(currentDate);
            activityFileInfo.setModifiedDate(currentDate);
            activityFileInfo.setInspectedDate(currentDate);
            activityFileInfo.setRemark("通过 familyhelper-life 服务上传/更新活动文件");
            activityFileInfoMaintainService.insertOrUpdate(activityFileInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void updateActivityFileStream(DubboRestActivityFileStreamUpdateInfo updateInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(updateInfo.getUserStringId());
            LongIdKey activityFileKey = new LongIdKey(updateInfo.getActivityFileLongId());

            // 确认用户存在。
            handlerValidator.makeSureUserExists(userKey);

            // 确认活动文件信息存在。
            handlerValidator.makeSureActivityFileExists(activityFileKey);

            // 确认用户有权限操作活动文件信息。
            handlerValidator.makeSureUserModifyPermittedForActivityFileInfo(userKey, activityFileKey);

            // 活动文件内容并存储（覆盖）。
            InputStream cin = updateInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.FILE_PATHS_ACTIVITY_FILE, getFileName(activityFileKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 根据 activityFileStreamUpdateInfo 更新字段。
            ActivityFileInfo activityFileInfo = activityFileInfoMaintainService.get(activityFileKey);
            activityFileInfo.setOriginName(updateInfo.getOriginName());
            activityFileInfo.setLength(updateInfo.getLength());
            activityFileInfo.setModifiedDate(new Date());
            activityFileInfoMaintainService.update(activityFileInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private String getFileName(LongIdKey activityFileKey) {
        return Long.toString(activityFileKey.getLongId());
    }
}
