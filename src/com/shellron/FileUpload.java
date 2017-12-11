package com.shellron;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传类，目录的遍历
 *
 * @author: Monster
 *
 * Date: 2017-12-6
 */
public class FileUpload {

    public static final okhttp3.MediaType MEDIA_TYPE_MARKDOWN = okhttp3.MediaType.parse("text/x-markdown; charset=utf-8");

    // Okhttp3 客户端实例
    private static OkHttpClient client;

    // 初始化 Okhttp3 客户端实例
    static {
        client = new OkHttpClient();
    }

    // 配置文件信息
    private Config config;

    public FileUpload() {
        super();
    }

    public FileUpload(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // 递归遍历目录与文件
    public void start(File dir) {

        File[] flist = dir.listFiles();

        if (flist == null || flist.length == 0) {
            return;
        }

        for (File file : flist) {

            if (file.isDirectory()) {

                // 这里将列出所有的文件夹
                System.out.println("Dir ==> " + file.getAbsolutePath());
                start(file);

            } else {

                for (String suffix : config.getSuffix()) {

                    // 获取当前文件的文件名
                    String fileName = file.getName();

                    // 若当前的文件类型为允许上传的类型，则上传该文件，否则跳过该文件
                    if(fileName.substring(fileName.lastIndexOf(".") + 1).equals(suffix)){

                        // 这里将列出所有需要上传文件
                        System.out.println("File ==> " + file.getAbsolutePath());
                        uploadFiles(file);

                    } else {
                        continue;
                    }

                }

            }

        }

    }

    // 获取文件的相对路径
    private String getRelativePath(File file) {

        String path;

        path = file.getAbsolutePath();
        path = path.replace(config.getBasePath(), "");
        path = path.replace('\\', '/');

        return path;
    }

    // 上传文件
    private void uploadFiles(File file) {

        Response response;
        String responseStr;
        String message;

        // 设置请求参数与需要上传的文件
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", config.getToken())
                .addFormDataPart("filePath", "/" + config.getAreaCode() + getRelativePath(file))
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Request request = new Request.Builder()
                .url(config.getUrl())
                .post(requestBody)
                .build();

        try {

            // 发送Http请求并获得响应
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                // 获得Http响应
                responseStr = response.body().string();

                JSONObject object = JSON.parseObject(responseStr);
                boolean status = object.getBooleanValue("status");

                if (status) {
                    message = "Message : Upload is completed!";
                } else {
                    message = "Error : " + object.getString("message");
                }

                System.out.println(message);

            } else {
                message = "Error : Upload is failed!";
                System.out.println(message);
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            message = "Error : Upload is failed!";
            System.out.println(message);
            e.printStackTrace();
        }

    }

}
