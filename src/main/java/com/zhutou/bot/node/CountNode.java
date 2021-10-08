package com.zhutou.bot.node;

/**
 * 统计在线节点个数
 */
public class CountNode {
    public CheckNode cn = new CheckNode();

    public String countNode() throws Exception {
        return cn.getNodes(true);
    }

    public String usage() throws Exception {
        return cn.usage();
    }

}
