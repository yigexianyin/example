package com.jipf.example.hadoop;

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

public class HdfsClient {

    public static FileSystem fileSystem;

    private static void kerberosConfig(Configuration config) {
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
     * 单节点连接hdfs
     *
     * @param hdfsProperties
     */
    public static void initHadoop1Connect(HdfsProperties hdfsProperties) {

        try {

            Configuration configuration = new Configuration();
            configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");

            kerberosConfig(configuration);

            URI uri = new URI(hdfsProperties.getUrl());

            fileSystem = FileSystem.get(uri, configuration, hdfsProperties.getUser());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * HA 集群连接
     *
     * @param hdfsProperties
     */
    public static void initHadoop2Connect(HdfsProperties hdfsProperties) {

        try {

            Configuration configuration = new Configuration();

            String nn1 = hdfsProperties.getNns()[0];
            String nn2 = hdfsProperties.getNns()[1];

            configuration.set("fs.defaultFS", hdfsProperties.getUrl());
            configuration.set("dfs.nameservices", hdfsProperties.getNameServices());
            configuration.set("dfs.ha.namenodes." + hdfsProperties.getNameServices(), nn1 + "," + nn2);
            configuration.set("dfs.namenode.rpc-address." + hdfsProperties.getNameServices() + "." + nn1, hdfsProperties.getNameNode1Url());
            configuration.set("dfs.namenode.rpc-address." + hdfsProperties.getNameServices() + "." + nn2, hdfsProperties.getNameNode2Url());
            configuration.set("dfs.client.failover.proxy.provider." + hdfsProperties.getNameServices(),
                    "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

            kerberosConfig(configuration);//kerberos 验证

            URI uri = new URI(hdfsProperties.getUrl());

            fileSystem = FileSystem.get(uri, configuration);
        } catch (Exception e) {

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
    public static void uploadFileToHdfs(String data, String path) {
        try {
            data = new String(data.getBytes(), "utf-8");
            writerString(data, path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个字符串写入某个路径
     *
     * @param text 要保存的字符串
     * @param path 要保存的路径
     */
    public static void writerString(String text, String path) {

        try {
            Path f = new Path(path);
            FSDataOutputStream os = fileSystem.create(f, true);
            // 以UTF-8格式写入文件，不乱码
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(text);
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {

        HdfsProperties hdfsProperties = new HdfsProperties();

        /**
         * hadoop1 连接方式
         */
//        hdfsProperties.setUrl("hdfs://10.111.32.184:8020");
//        hdfsProperties.setUser("gai");
//        initHadoop1Connect(hdfsProperties);

        /**
         * hadoop2 ha 连接方式
         */
        //geocluster 为 nameservices名
        hdfsProperties.setUrl("hdfs://dpcluster");
        hdfsProperties.setUser("gai");
        hdfsProperties.setNameServices("dpcluster");
        hdfsProperties.setNns(new String[]{"nn1", "nn2"});
        hdfsProperties.setNameNode1Url("hdfs://10.111.32.184:8020");
        hdfsProperties.setNameNode2Url("hdfs://10.111.32.186:8020");
        initHadoop2Connect(hdfsProperties);


//        hdfsProperties.setUrl("hdfs://geonamenodeha");
//        hdfsProperties.setUser("gai");
//        hdfsProperties.setNameServices("geonamenodeha");
//        hdfsProperties.setNns(new String[]{"nn1", "nn2"});
//        hdfsProperties.setNameNode1Url("hdfs://172.16.9.16:8020");
//        hdfsProperties.setNameNode2Url("hdfs://172.16.9.17:8020");
//        initHadoop2Connect(hdfsProperties);

//        uploadFileToHdfs("222222","/user/gai/file/data/temp/11111.csv");


        try {

            FileStatus[] statuses = fileSystem.listStatus(new Path("/"));

            for (FileStatus status : statuses) {

                System.out.println(status.getPath().toUri().getPath());
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
