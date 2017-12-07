package com.shellron;

import java.io.File;

public class Main {

    private static String api;

    private static String basePath;

    private static String[] suffix;

    private static String token;

    static {



    }

    public static void main(String[] args) {

        String api = "http://127.0.0.1:4869/dfs/put";

        String basePath = "C:\\";

        String[] suffix = {"txt"};

        String token = "123456";

        FileUpload fileUpload = new FileUpload();
        fileUpload.setApi(api);
        fileUpload.setBasePath(basePath);
        fileUpload.setSuffix(suffix);
        fileUpload.setToken(token);

        File files = new File(basePath);
        fileUpload.getDirectory(files);

    }

}
