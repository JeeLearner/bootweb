package com.jee.boot.system.vo;

import com.jee.boot.common.core.domain.BaseEntity;
import java.io.Serializable;

/**
 * 岗位 VO
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class  SysPostVO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 岗位ID */
    private Long postId;
    /** 岗位编码 */
    private String postCode;
    /** 岗位名称 */
    private String postName;
    /** 显示顺序 */
    private Integer postSort;
    /** 状态（0正常 1停用） */
    private String status;


    public void setPostId(Long postId){
        this.postId = postId;
    }

    public Long getPostId(){
        return postId;
    }
    public void setPostCode(String postCode){
        this.postCode = postCode;
    }

    public String getPostCode(){
        return postCode;
    }
    public void setPostName(String postName){
        this.postName = postName;
    }

    public String getPostName(){
        return postName;
    }
    public void setPostSort(Integer postSort){
        this.postSort = postSort;
    }

    public Integer getPostSort(){
        return postSort;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
