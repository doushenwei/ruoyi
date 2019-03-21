package com.ruoyi.web.controller.worktask.controller;

import java.util.Date;
import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.worktask.domain.WorkTask;
import com.ruoyi.worktask.service.IWorkTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 工作任务 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-03-20
 */
@Controller
@RequestMapping("/worktask/workTask")
public class WorkTaskController extends BaseController
{
    private String prefix = "worktask/workTask";
	
	@Autowired
	private IWorkTaskService workTaskService;
	
	@RequiresPermissions("worktask:workTask:view")
	@GetMapping()
	public String workTask()
	{
	    return prefix + "/workTask";
	}
	
	/**
	 * 查询工作任务列表
	 */
	@RequiresPermissions("worktask:workTask:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(WorkTask workTask)
	{
		startPage();
        List<WorkTask> list = workTaskService.selectWorkTaskList(workTask);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出工作任务列表
	 */
	@RequiresPermissions("worktask:workTask:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WorkTask workTask)
    {
    	List<WorkTask> list = workTaskService.selectWorkTaskList(workTask);
        ExcelUtil<WorkTask> util = new ExcelUtil<WorkTask>(WorkTask.class);
        return util.exportExcel(list, "workTask");
    }
	
	/**
	 * 新增工作任务
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存工作任务
	 */
	@RequiresPermissions("worktask:workTask:add")
	@Log(title = "工作任务", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(WorkTask workTask)
	{
		workTask.setCreateBy(ShiroUtils.getLoginName());
		workTask.setCreateTime(new Date());
		return toAjax(workTaskService.insertWorkTask(workTask));
	}

	/**
	 * 修改工作任务
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		WorkTask workTask = workTaskService.selectWorkTaskById(id);
		mmap.put("workTask", workTask);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存工作任务
	 */
	@RequiresPermissions("worktask:workTask:edit")
	@Log(title = "工作任务", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(WorkTask workTask)
	{
		workTask.setUpdateTime(new Date());
		workTask.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(workTaskService.updateWorkTask(workTask));
	}
	
	/**
	 * 删除工作任务
	 */
	@RequiresPermissions("worktask:workTask:remove")
	@Log(title = "工作任务", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(workTaskService.deleteWorkTaskByIds(ids));
	}
	
}