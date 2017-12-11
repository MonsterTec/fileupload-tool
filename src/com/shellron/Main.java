package com.shellron;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 *
 * 主类，初始化配置信息，主程序入口
 *
 * @author: Monster
 *
 * Date: 2017-12-6
 *
 */
public class Main {

    // 配置文件名
    private static final String conf_fileName = "config.xml";

    // 配置文件所在目录
    private static final String conf_folder = "config";

    // 配置文件对象
    private static Config config;

    // 初始化程序配置
    static {

        // 配置文件的绝对目录
        String filePath = System.getProperty("user.dir") + File.separator + conf_folder + File.separator + conf_fileName;

        File file = new File(filePath);

        // 文件判断是否存在
        if (!file.exists()) {
            System.out.println("Error : Config file doesn't exist!");
            System.exit(1);
        }

        // 创建配置文件对象
        config = new Config();

        try {

            SAXReader reader = new SAXReader();
            Document doc;
            doc = reader.read(file);

            // 获取根节点
            Element root = doc.getRootElement();
            Element data;

            // 迭代Config节点
            Iterator<?> iterator = root.elementIterator("Config");
            data = (Element) iterator.next();

            // 获取suffix节点的所有子节点
            List<Element> suffixList = data.element("suffix").elements();
            String[] suffix = new String[suffixList.size()];

            // 将suffix子节点的内容存入数组
            for(int i = 0; i < suffixList.size(); i++){
                suffix[i] = suffixList.get(i).getTextTrim();
            }

            // 初始化config对象
            config.setUrl(data.elementText("url").trim());
            config.setBasePath(data.elementText("basePath").trim());
            config.setSuffix(suffix);
            config.setToken(data.elementText("token").trim());
            config.setAreaCode(data.elementText("areaCode").trim());

        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("Error : Read Config file exception!");
            System.exit(1);
        }

    }

    // main方法，主程序入口
    public static void main(String[] args) {

        File dir = new File(config.getBasePath());

        if(!dir.exists() || !dir.isDirectory()){
            System.out.println("Error : Directory is not found!");
            System.exit(1);
        } else {
            // 实例化文件上传对象，执行文件上传方法
            FileUpload fileUpload = new FileUpload();
            fileUpload.setConfig(config);
            fileUpload.start(dir);
        }

    }

}
