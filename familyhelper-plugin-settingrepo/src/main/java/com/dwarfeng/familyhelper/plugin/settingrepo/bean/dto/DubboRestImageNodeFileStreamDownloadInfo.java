package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.util.Arrays;

/**
 * Dubbo rest 图片节点文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
public class DubboRestImageNodeFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = 1692027193144123037L;

    @FormParam("category")
    @PartType("text/plain;charset=utf-8")
    @JSONField(name = "category", ordinal = 1)
    private String category;

    @FormParam("args")
    @PartType("application/json;charset=utf-8")
    @JSONField(name = "args", ordinal = 2)
    private String[] args;

    public DubboRestImageNodeFileStreamDownloadInfo() {
    }

    public DubboRestImageNodeFileStreamDownloadInfo(String category, String[] args) {
        this.category = category;
        this.args = args;
    }

    public String getCategory() {
        return category;
    }

    public DubboRestImageNodeFileStreamDownloadInfo setCategory(String category) {
        this.category = category;
        return this;
    }

    public String[] getArgs() {
        return args;
    }

    public DubboRestImageNodeFileStreamDownloadInfo setArgs(String[] args) {
        this.args = args;
        return this;
    }

    @Override
    public String toString() {
        return "DubboRestImageNodeFileStreamDownloadInfo{" +
                "category='" + category + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
