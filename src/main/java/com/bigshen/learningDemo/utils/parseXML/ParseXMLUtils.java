package com.bigshen.learningDemo.utils.parseXML;


import com.bigshen.learningDemo.utils.bo.MetaBO;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName ParseXML
 * @Description:TODO 解析xml文件
 * @Author: byj
 * @Date: 2020/5/8
 */
public class ParseXMLUtils {
    public static void main(String[] args) throws Exception {
        File file=new File("E:\\IdeaProjects\\ds-encryption-service\\src\\main\\resources\\ds-timeline-server-publish-configuration.xml");
        InputStream in=new FileInputStream(file);
        byte[] fileContent = input2byte(in);
        MetaBO metaBO = parseXML(fileContent);
        System.out.println(metaBO);

    }
    private static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
    /**
     * 解析二进制流
     *
     * @param file_byte
     *            二进制数据源
     * @return 元数据对象
     * @throws UnsupportedEncodingException
     */
    private static MetaBO parseXML(byte[] file_byte) throws Exception {
        MetaBO meta = new MetaBO();

        String xml = new String(file_byte, "UTF-8");
        Document document = DocumentHelper.parseText(xml);
        Element project = document.getRootElement();
        Element info = project.element("info");
        if (info != null) {
            meta.setName(info.elementText("name"));
            meta.setCode(info.elementText("code"));
            meta.setVersion(info.elementText("version"));
            meta.setAppName(info.elementText("appid"));
        }
        Element release = project.element("install-info");
        if (release != null) {
            meta.setLinuxPath(release.elementText("linuxpath"));
            meta.setWindowsPath(release.elementText("windowspath"));
        }
        Element files = project.element("cfg-files");
        Iterator iterator = files.elementIterator("file");
        Map<String, Map<String, String>> file = new HashMap<String, Map<String, String>>();
        Map<String, String> xmlFiles = new HashMap<String, String>();
        while (iterator.hasNext()) {
            Element ele = (Element) iterator.next();
            if (ele != null) {
                String fileName = ele.attribute("name").getText();
                if (ele.attribute("type").getText().equals("xml")) {
                    Iterator it = ele.elementIterator();
                    if (it != null) {
                        while (it.hasNext()) {
                            Element child = (Element) it.next();
                            xmlFiles.put(fileName, child.asXML());

                        }
                    }

                    continue;
                }
                Iterator pros = ele.elementIterator();
                Map<String, String> props = new HashMap<String, String>();
                while (pros.hasNext()) {
                    Element pro = (Element) pros.next();
                    String key = pro.getName();
                    String value = pro.getStringValue();
                    if (pro.attribute("content") != null) {
                        String content = pro.attribute("content").getValue();

                        value = value + "@@" + content;
                    }
                    props.put(key, value);
                }
                file.put(fileName, props);

            }
        }
        meta.setXmlFiles(xmlFiles);
        meta.setFiles(file);

        return meta;
    }
}
