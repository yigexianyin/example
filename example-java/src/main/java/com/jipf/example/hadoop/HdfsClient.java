package com.jipf.example.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class HdfsClient {

    public static FileSystem fileSystem;

    /**
     * 单节点连接hdfs
     *
     * @param hdfsProperties
     */
    public static void initHadoop1Connect(HdfsProperties hdfsProperties) {

        try {

            Configuration configuration = new Configuration();

            URI uri = new URI(hdfsProperties.getUrl());

            fileSystem = FileSystem.get(uri, configuration, hdfsProperties.getUser());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * HA 集群连接
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

            URI uri = new URI(hdfsProperties.getUrl());

            fileSystem = FileSystem.get(uri, configuration, hdfsProperties.getUser());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        HdfsProperties hdfsProperties = new HdfsProperties();

        /**
         * hadoop1 连接方式
         */
//        hdfsProperties.setUrl("hdfs://10.111.32.12:8020");
//        hdfsProperties.setUser("dp");
//        initHadoop1Connect(hdfsProperties);

        /**
         * hadoop2 ha 连接方式
         */
        //geocluster 为 nameservices名
        hdfsProperties.setUrl("hdfs://geocluster");
        hdfsProperties.setUser("hdfs");
        hdfsProperties.setNameServices("geocluster");
        hdfsProperties.setNns(new String[]{"nn1", "nn2"});
        hdfsProperties.setNameNode1Url("hdfs://10.111.32.25:8020");
        hdfsProperties.setNameNode2Url("hdfs://10.111.32.26:8020");
        initHadoop2Connect(hdfsProperties);


//        hdfsProperties.setUrl("hdfs://geonamenodeha");
//        hdfsProperties.setUser("gai");
//        hdfsProperties.setNameServices("geonamenodeha");
//        hdfsProperties.setNns(new String[]{"nn1", "nn2"});
//        hdfsProperties.setNameNode1Url("hdfs://172.16.9.16:8020");
//        hdfsProperties.setNameNode2Url("hdfs://172.16.9.17:8020");
//        initHadoop2Connect(hdfsProperties);

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
