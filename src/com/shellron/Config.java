package com.shellron;

/**
 *
 * 配置文件类，用于读取配置文件
 *
 * @author: Monster
 *
 * Date: 2017-12-7
 *
 */
public class Config {

    // 上传文件接口
    private String url;

    // 需要扫描的基础目录
    private String basePath;

    // 需要上传的文件类型，文件扩展名
    private String[] suffix;

    // 上传文件的口令验证
    private String token;

    // 上传区域代码
    private String areaCode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String[] getSuffix() {
        return suffix;
    }

    public void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

}
