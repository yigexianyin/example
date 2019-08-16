package com.jipf.example.hadoop;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class KerberosHdfsClient {

    private FileSystem hdfs;

    public KerberosHdfsClient(String host, String port) {
        this.hdfs = getFileSystem(host, port);
    }

    /**
     * kerbers认证调用方式一
     *
     * @param config
     */
    private static void kerberosConfig1(Configuration config) {
        /**设置java安全krb5配置,其中krb5.conf文件可以从成功开启kerberos的集群任意一台节点/etc/krb5.conf拿到，放置本地*/
        String krb5File = "E:\\data1\\krb5.conf";
        /** kerberos principal*/
        String kerUser = "gai/10.111.32.189@GAI.COM";
        /** 对应kerberos principal的keytab文件,从服务器获取放置本地*/
        String keyPath = "E:\\data1\\gai.10.111.32.189.keytab";
        /** 设置krb5.conf到环境变量*/
        System.setProperty("java.security.krb5.conf", krb5File);
        /** 设置安全认证方式为kerberos */
        config.set("hadoop.security.authentication", "kerberos");

        UserGroupInformation.setConfiguration(config);
        try {
            UserGroupInformation.loginUserFromKeytab(kerUser, keyPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * kerbers认证调用方式二
     *
     * @param config
     */
    private static void kerberosConfig2(Configuration config) {

        /**设置java安全krb5配置,其中krb5.conf文件可以从成功开启kerberos的集群任意一台节点/etc/krb5.conf拿到，放置本地*/
        String krb5File = "E:\\data1\\krb5.conf";
        /** kerberos principal*/
        String kerUser = "gai/10.111.32.189@GAI.COM";
        /** 对应kerberos principal的keytab文件,从服务器获取放置本地*/
        String keyPath = "E:\\data1\\gai.10.111.32.189.keytab";
        /** 设置krb5.conf到环境变量*/
        System.setProperty("java.security.krb5.conf", krb5File);
        /** hadoop集群任一节点拿配置文件到本地*/
        String coreSite = "D:\\data1\\core-site.xml";
        /** hadoop集群任一节点拿配置文件到本地*/
        String hdfsSite = "D:\\data1\\hdfs-site.xml";
        System.setProperty("java.security.krb5.conf", krb5File);
        config.addResource(new Path(coreSite));
        config.addResource(new Path(hdfsSite));
        UserGroupInformation.setConfiguration(config);
        try {
            UserGroupInformation.loginUserFromKeytab(kerUser, keyPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 得到hdfs的连接 FileSystem类
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static FileSystem getFileSystem(String host, String port) {
        // 创建HDFS属性对象

        // 获取FileSystem类的方法有很多种，这里只写一种
        Configuration config = new Configuration();
        config.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");

        /** 推荐方式一*/
        kerberosConfig1(config);
//        kerberosConfig2(config);


        try {
            // hdfs连接地址
            URI uri = new URI("hdfs://" + host + ":" + port);
            // 第一位为uri，第二位为config，第三位是登录的用户
            //方式一
            return FileSystem.get(uri, config);
            //方式二
//            return FileSystem.get(config);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 删除文件或者文件夹
     *
     * @param src
     * @throws Exception
     */
    public void delete(String src) throws Exception {
        Path p1 = new Path(src);
        if (hdfs.isDirectory(p1)) {
            hdfs.delete(p1, true);
            System.out.println("删除文件夹成功: " + src);
        } else if (hdfs.isFile(p1)) {
            hdfs.delete(p1, false);
            System.out.println("删除文件成功: " + src);
        }
    }

    /**
     * 将一个字符串写入某个路径
     *
     * @param text 要保存的字符串
     * @param path 要保存的路径
     */
    public void writerString(String text, String path) {

        try {
            Path f = new Path(path);
            FSDataOutputStream os = hdfs.create(f, true);
            // 以UTF-8格式写入文件，不乱码
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(text);
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * 读取文件目录列表
     *
     * @param dir
     * @return
     */
    public List<String> readDirList(String dir) {
        if (StringUtils.isBlank(dir)) {
            return null;
        }
        if (!dir.startsWith("/")) {
//            dir = dir.substring(1);
            dir = "/" + dir;
        }
        List<String> result = new ArrayList<String>();
        FileStatus[] stats = null;
        try {
            stats = hdfs.listStatus(new Path(dir));
            for (FileStatus file : stats) {
//                if (file.isFile() && file.getLen() != 0) {
//                    result.add(file.getPath().toUri().getPath());
//                }
                result.add(file.getPath().toUri().getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param data
     * @param path
     */
    public void append(String data, String path) {
        try {
            Path f = new Path(path);
            if (!hdfs.exists(f)) {
                hdfs.createNewFile(f);
            }
            FSDataOutputStream os = hdfs.append(new Path(path));
            // 以UTF-8格式写入文件，不乱码
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(data);
            writer.write("\r\n");
            writer.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换文件编码为UTF-8，上传至hdfs服务器
     *
     * @param data
     * @param path
     * @author jipengfei
     */
    public void uploadFileToHdfs(String data, String path) {
        try {
            data = new String(data.getBytes(), "utf-8");
            this.writerString(data, path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            KerberosHdfsClient hdfsUtil = new KerberosHdfsClient("10.111.32.184", "8020");
//            hdfsUtil.uploadFileToHdfs("111111","/user/gai/file/data/temp/11111.csv");
//            hdfsUtil.delete("/user/dp/file/data/temp/11111.csv");
            List<String> list = hdfsUtil.readDirList("/user/gai/file/data");
            System.out.println("==============================================");
            list.stream().forEach(System.out::println);
            System.out.println("==============================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
