package com.jee.boot.admin.swagger.demo;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.core.web.BaseController;
import com.jee.boot.common.utils.text.JeeStringUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * API测试
 *
 * @author jeeLearner
 * @version V1.0
 */
@Api(tags = "用户信息管理")
@RestController
@RequestMapping("/test/user")
public class TestController extends BaseController {

    private final static Map<Long, UserEntity> users = new LinkedHashMap<Long, UserEntity>();{
        users.put(1L, new UserEntity(1L, "admin", "admin123", "15888888888"));
        users.put(2L, new UserEntity(2L, "jee", "admin123", "15666666666"));
    }

    /**
     * 获取用户列表
     * @return 用户列表
     */
    @ApiOperation("获取用户列表")
    @GetMapping("/list")    public R userList(){
        List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
        return R.ok().data("list", userList);
    }

    /**
     * 获取用户详情
     * @param userId 用户id
     * @return  用户信息
     */
    @ApiOperation("获取用户详情")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{userId}")
    public R getUser(@PathVariable Integer userId){
        if (!users.isEmpty() && users.containsKey(userId)) {
            return R.ok().data("info", users.get(userId));
        } else {
            return R.error().msg("用户不存在");
        }
    }

    /**
     * 新增用户
     * @param user  用户实体
     * @return  R
     */
    @ApiOperation("新增用户")
    @ApiImplicitParam(name = "userEntity", value = "新增用户信息", dataType = "UserEntity")
    @PostMapping("/save")
    public R save(UserEntity user){
        if (JeeStringUtils.isNull(user) || JeeStringUtils.isNull(user.getUserId())){
            return R.error().msg("用户ID不能为空");
        }
        users.put(user.getUserId(), user);
        return R.ok();
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @ApiOperation("更新用户")
    @ApiImplicitParam(name = "userEntity", value = "新增用户信息", dataType = "UserEntity")
    @PutMapping("/update")
    public R update(UserEntity user){
        if (JeeStringUtils.isNull(user) || JeeStringUtils.isNull(user.getUserId())){
            return R.error().msg("用户ID不能为空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId())){
            return R.error().msg("用户不存在");
        }
        users.remove(user.getUserId());
        users.put(user.getUserId(), user);
        return R.ok();
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @ApiOperation("删除用户信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{userId}")
    public R delete(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return R.ok();
        } else {
            return R.error().msg("用户不存在");
        }
    }
}


/**
 * 用户实体
 */
@ApiModel("用户实体")
class UserEntity {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;
    /**
     *用户名称
     */
    @ApiModelProperty("用户名称")
    private String username;
    /**
     *用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;
    /**
     *用户手机
     */
    @ApiModelProperty("用户手机")
    private String mobile;

    public UserEntity(){
    }

    public UserEntity(Long userId, String username, String password, String mobile) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
