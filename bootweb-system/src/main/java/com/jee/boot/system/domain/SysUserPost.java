package com.jee.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户与岗位关联 DO对象 sys_user_post
 * 
 * @author jeeLearner
 * @version V1.0
 */
public class SysUserPost {

	/** 用户ID */
	private Long userId;
	/** 岗位ID */
	private Long postId;


	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}
	public void setPostId(Long postId){
		this.postId = postId;
	}

	public Long getPostId(){
		return postId;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("postId", getPostId())
            .toString();
    }
}
