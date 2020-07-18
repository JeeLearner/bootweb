package com.jee.boot.system;

import com.jee.boot.common.enums.UserStatus;
import com.jee.boot.common.core.page.PageUtil;
import com.jee.boot.common.core.result.R;
import com.jee.boot.system.dto.SysUserDTO;
import com.jee.boot.system.service.ISysUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author jeeLearner
 * @version V1.0
 */
@SpringBootTest(classes = SysApplication.class)
@RunWith(SpringRunner.class)
public class SysUserTest {

    @Autowired
    ISysUserService userService;

    @Test
    public void selectUserById(){
        long id = 1;
        SysUserDTO dto = userService.getUserById(id);
        Assert.assertEquals("admin", dto.getLoginName());
    }

    @Test
    public void selectUserList(){
        List<SysUserDTO> list = userService.listUser(null);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void selectUserListByPage(){
//        R r = PageUtil.page("1","1", "dept_id asc",() -> {
//            return userService.selectUserList(null);
//        });
//
//        r.getData().entrySet().forEach((e) -> {
//            System.out.println(e.getKey());
//            System.out.println(e.getValue());
//        });
//        System.out.println(r.getData().size());
    }

    @Test
    public void testt(){
        System.out.println(UserStatus.OK);
        System.out.println(UserStatus.OK.ordinal());
    }
}

