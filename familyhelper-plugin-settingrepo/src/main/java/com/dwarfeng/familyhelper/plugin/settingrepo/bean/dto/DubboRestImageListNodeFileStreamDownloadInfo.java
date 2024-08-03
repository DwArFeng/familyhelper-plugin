package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;

/**
 * Dubbo rest 图片列表节点文件流下载信息。
 *
 * @author DwArFeng
 * @since 1.6.0
 */
public class DubboRestImageListNodeFileStreamDownloadInfo implements Dto {

    private static final long serialVersionUID = -3828265048292150681L;

    @FormParam("category")
    @PartType("text/plain;charset=utf-8")
    @JSONField(name = "category", ordinal = 1)
    private String category;

    @FormParam("args")
    @PartType("application/json;charset=utf-8")
    @JSONField(name = "args", ordinal = 2)
    private String[] args;

    @FormParam("index")
    @PartType(MediaType.TEXT_PLAIN)
    @JSONField(name = "index", ordinal = 3)
    private Integer index;

    public DubboRestImageListNodeFileStreamDownloadInfo() {
    }

    public DubboRestImageListNodeFileStreamDownloadInfo(String category, String[] args, Integer index) {
        this.category = category;
        this.args = args;
        this.index = index;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "DubboRestImageListNodeFileStreamDownloadInfo{" +
                "category='" + category + '\'' +
                ", args=" + Arrays.toString(args) +
                ", index=" + index +
                '}';
    }
}
