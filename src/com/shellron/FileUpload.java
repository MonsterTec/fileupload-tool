package  com.shellron;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class FileUpload {

    public static final okhttp3.MediaType MEDIA_TYPE_MARKDOWN = okhttp3.MediaType.parse("text/x-markdown; charset=utf-8");

    // 上传文件api
    private String api;

    // 需要扫描的基础目录
    private String basePath;

    // 需要上传的文件类型
    private String[] suffix;

    // api的口令验证
    private String token;

    // Okhttp3 客户端实例
    static private OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // 递归遍历目录与文件
    public void getDirectory(File file) {

        File[] flist = file.listFiles();

        if (flist == null || flist.length == 0) {
            return;
        }

        for (File f : flist) {
            if (f.isDirectory()) {
                //这里将列出所有的文件夹
                System.out.println("Dir==>" + f.getAbsolutePath());
                getDirectory(f);
            } else {
                //这里将列出所有的文件
                System.out.println("file==>" + f.getAbsolutePath());

                for(String suffix: this.suffix){

                    String fileName = f.getName();

                    if(fileName.substring(fileName.lastIndexOf(".") + 1).equals(suffix)){
                        uploadFiles(f);
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
        path = path.replace(this.basePath,"");
        path = path.replace('\\', '/');

        return path;
    }

    // 上传文件
    private void uploadFiles(File file) {

        Response response;
        String responseStr;

        // 设置请求参数与需要上传的文件
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("filePath", "/330105" + getRelativePath(file))
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Request request = new Request.Builder()
                .url(api)
                .post(requestBody)
                .build();

        try {

            // 发送Http请求并获得响应
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                // 获得Http响应
                responseStr = response.body().string();
                System.out.println(responseStr);

            } else {
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
