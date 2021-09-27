package com.zhutou.bot.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Usage {

    public Integer server_id;
    public String server_type;
    public String u;
    public String d;
    public Double total;
    public String server_name;

    public Usage(){ };
}
