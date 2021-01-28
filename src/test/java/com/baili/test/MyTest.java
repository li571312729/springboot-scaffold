package com.baili.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.baili.common.SpringBootTestCase;
import com.baili.zookeeper.service.ZookeeperService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MyTest extends SpringBootTestCase {

    @Autowired
    DruidDataSource druidDataSource;

    @Autowired
    private ZookeeperService zookeeperService ;

    @Test
    public void test(){
        if(!zookeeperService.isExistNode("/bailizoo")){
            System.out.println("节点不存在");
        }else{
            System.out.println("节点存在");
        }
    }
}
