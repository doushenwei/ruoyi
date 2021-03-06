package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    private String prefix = "system/notice";
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysNoticeService noticeService;

    @RequiresPermissions("system:notice:view")
    @GetMapping()
    public String notice(ModelMap mmap)
    {
        mmap.put("user", ShiroUtils.getSysUser());
        return prefix + "/notice";
    }


    @RequiresPermissions("system:notice:view")
    @GetMapping("/view/{id}")
    public String toView(@PathVariable("id") Long id,ModelMap mmap)
    {
        SysNotice sysNotice = noticeService.selectNoticeById(id);
        sysNotice.setReadStatus("1");
        noticeService.updateNotice(sysNotice);
        mmap.put("notice", sysNotice);
        return prefix + "/noticePreview";
    }
    /**
     * 查询公告列表
     */
    @RequiresPermissions("system:notice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId()))
        {
            notice.setDisplayUser(ShiroUtils.getUserId()+"");
        }
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 新增公告
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        SysUser sysUser=new SysUser();
        List<SysUser> sysUsers = userService.selectUserList(sysUser);
        mmap.put("users",sysUsers);

        return prefix + "/add";
    }

    /**
     * 新增保存公告
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNotice notice)
    {
        notice.setCreateBy(ShiroUtils.getUserId()+"");
        notice.setRemark(ShiroUtils.getSysUser().getUserName());
        String displayUser = notice.getDisplayUser();
        String[] displayUsers = displayUser.split(",");
        int i1=0;
        for (int i = 0; i < displayUsers.length; i++) {
             notice.setDisplayUser(displayUsers[i]);
             i1 = noticeService.insertNotice(notice);
        }
        return toAjax(i1);
    }

    /**
     * 修改公告
     */
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap)
    {
        SysUser sysUser=new SysUser();
        mmap.put("users",userService.selectUserList(sysUser));
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存公告
     */
    @RequiresPermissions("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotice notice)
    {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(noticeService.deleteNoticeByIds(ids));
    }
}
