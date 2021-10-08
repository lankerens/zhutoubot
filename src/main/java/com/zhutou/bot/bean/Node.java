package com.zhutou.bot.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
public class Node {
    public Integer id;
    public List<String> group_id;
    public Integer parent_id;
    public Integer port;
    public Integer server_port;
    public Integer tls;
    public List<String> tags;
    public String rate;
    public String network;
    public String settings;
    public String rules;
    public String networkSettings;
    public String tlsSettings;
    public String ruleSettings;
    public String dnsSettings;
    public String created_at;
    public String updated_at;
    public String type;
    public String last_check_at;
    public String last_push_at;
    public Integer alter_id;
    public Integer sort;



    // 名称
    public String name;
    // 判断是否有效节点
    public String host;
    // 是否展示
    public Integer show;
    //在线人数
    public String online;
    // 0 掉线 1 在线未上报 2 在线
    public Integer available_status;

    public Node(){ }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", parent_id=" + parent_id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", available_status=" + available_status +
                '}';
    }
}
