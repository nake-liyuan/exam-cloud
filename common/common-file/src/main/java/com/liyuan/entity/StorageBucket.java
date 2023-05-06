package com.liyuan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author liyuan
 * @date 2022/10/4
 * @project youlai-mall
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageBucket implements Serializable {

    private String name;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private ZonedDateTime creationDate;



}
