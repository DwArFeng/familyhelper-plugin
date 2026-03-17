package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.util.Arrays;

/**
 * Dubbo rest 文件节点文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public class DubboRestFileNodeFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = -7866747914777536659L;

    @FormParam("category")
    @PartType("text/plain;charset=utf-8")
    @JSONField(name = "category", ordinal = 1)
    private String category;

    @FormParam("args")
    @PartType("application/json;charset=utf-8")
    @JSONField(name = "args", ordinal = 2)
    private String[] args;

    public DubboRestFileNodeFileStreamDownloadInfo() {
    }

    public DubboRestFileNodeFileStreamDownloadInfo(String category, String[] args) {
        this.category = category;
        this.args = args;
    }

    public String getCategory() {
        return category;
    }

    public DubboRestFileNodeFileStreamDownloadInfo setCategory(String category) {
        this.category = category;
        return this;
    }

    public String[] getArgs() {
        return args;
    }

    public DubboRestFileNodeFileStreamDownloadInfo setArgs(String[] args) {
        this.args = args;
        return this;
    }

    @Override
    public String toString() {
        return "DubboRestFileNodeFileStreamDownloadInfo{" +
                "category='" + category + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
