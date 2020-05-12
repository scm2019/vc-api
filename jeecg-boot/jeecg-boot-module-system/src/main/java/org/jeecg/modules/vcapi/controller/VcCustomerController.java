package org.jeecg.modules.vcapi.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.vcapi.entity.VcCustomer;
import org.jeecg.modules.vcapi.enums.RoleCodeEnum;
import org.jeecg.modules.vcapi.service.IVcCustomerService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
 /**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@RestController
@RequestMapping("/vcapi/vcCustomer")
@Slf4j
public class VcCustomerController {

	private final IVcCustomerService vcCustomerService;
	private final ISysUserRoleService sysUserRoleService;
	private final ISysUserService sysUserService;

	@Autowired
	public VcCustomerController(IVcCustomerService vcCustomerService,ISysUserRoleService sysUserRoleService,ISysUserService sysUserService){
		this.vcCustomerService = vcCustomerService;
		this.sysUserRoleService = sysUserRoleService;
		this.sysUserService = sysUserService;
	}
	/**
	  * 分页列表查询
	 * @param vcCustomer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<VcCustomer>> queryPageList(VcCustomer vcCustomer,
												   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												   HttpServletRequest req) {
		Result<IPage<VcCustomer>> result = new Result<IPage<VcCustomer>>();
		//QueryWrapper<VcCustomer> queryWrapper = QueryGenerator.initQueryWrapper(vcCustomer, req.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		boolean isAdmin = false;
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			//管理员的查看全部人员的
			isAdmin = true;
		}else if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.CUSTOMER.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			isAdmin = false;
		}
		Page<VcCustomer> page = new Page<VcCustomer>(pageNo, pageSize);
		IPage<VcCustomer> pageList = vcCustomerService.queryPage(page, vcCustomer,isAdmin,sysUser.getUsername());
		if(CollectionUtils.isNotEmpty(pageList.getRecords())){
			pageList.getRecords().stream().forEach(customer -> {
				if(null != customer.getQuota()){
					customer.setQuota(customer.getQuota().divide(new BigDecimal(100)));
				}
				if(null != customer.getMoney()){
					customer.setMoney(customer.getMoney().divide(new BigDecimal(100)));
				}
			});
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param vcCustomer
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<VcCustomer> add(@RequestBody VcCustomer vcCustomer) {
		Result<VcCustomer> result = new Result<VcCustomer>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			try {
				if(null != vcCustomer.getMoney()){
					vcCustomer.setMoney(vcCustomer.getMoney().multiply(new BigDecimal(100)));
				}
				if(null != vcCustomer.getQuota()){
					vcCustomer.setQuota(vcCustomer.getQuota().multiply(new BigDecimal(100)));
				}
				vcCustomerService.save(vcCustomer);
				result.success("添加成功！");
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				result.error500("操作失败");
			}
		}else{
			result.error500("您暂时没有该操作权限!!!");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param vcCustomer
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<VcCustomer> edit(@RequestBody VcCustomer vcCustomer) {
		Result<VcCustomer> result = new Result<VcCustomer>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			//管理员的查看全部人员的
			VcCustomer vcCustomerEntity = vcCustomerService.getById(vcCustomer.getId());
			if(vcCustomerEntity==null) {
				result.error500("未找到对应实体");
			}else {
				if(null != vcCustomer.getQuota()){
					vcCustomer.setQuota(vcCustomer.getQuota().multiply(new BigDecimal(100)));
				}
				if(null != vcCustomer.getMoney()){
					vcCustomer.setMoney(vcCustomer.getMoney().multiply(new BigDecimal(100)));
				}
				boolean ok = vcCustomerService.updateById(vcCustomer);
				//TODO 返回false说明什么？
				if(ok) {
					result.success("修改成功!");
				}
			}
		}else {
			result.error500("您暂时没有该操作权限!!");
		}
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			//管理员的查看全部人员的
			try {
				vcCustomerService.removeById(id);
			} catch (Exception e) {
				log.error("删除失败",e.getMessage());
				return Result.error("删除失败!");
			}
			return Result.ok("删除成功!");
		}else {
			return Result.error("您暂时没有该操作权限!");
		}
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<VcCustomer> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VcCustomer> result = new Result<VcCustomer>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			if(ids==null || "".equals(ids.trim())) {
				result.error500("参数不识别！");
			}else {
				this.vcCustomerService.removeByIds(Arrays.asList(ids.split(",")));
				result.success("删除成功!");
			}
		}else{
			result.error500("您暂时没有该操作权限!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<VcCustomer> queryById(@RequestParam(name="id",required=true) String id) {
		Result<VcCustomer> result = new Result<VcCustomer>();
		VcCustomer vcCustomer = vcCustomerService.getById(id);
		if(vcCustomer==null) {
			result.error500("未找到对应实体");
		}else {
			if(null != vcCustomer.getQuota()){
				vcCustomer.setQuota(vcCustomer.getQuota().divide(new BigDecimal(100)));
			}
			if(null != vcCustomer.getMoney()){
				vcCustomer.setMoney(vcCustomer.getMoney().divide(new BigDecimal(100)));
			}
			result.setResult(vcCustomer);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param vcCustomer
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, VcCustomer vcCustomer) {
	  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	  boolean isAdmin = false;
	  if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
		  //管理员的查看全部人员的
		  isAdmin = true;
	  }else if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.CUSTOMER.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
		  isAdmin = false;
	  }
      // Step.1 组装查询条件查询数据
      //QueryWrapper<VcCustomer> queryWrapper = QueryGenerator.initQueryWrapper(vcCustomer, request.getParameterMap());
      List<VcCustomer> pageList = vcCustomerService.exportListData(vcCustomer,isAdmin,sysUser.getUsername());
	  if(CollectionUtils.isNotEmpty(pageList)){
	  	pageList.stream().forEach(customer -> {
			if(null != customer.getQuota()){
				customer.setQuota(customer.getQuota().divide(new BigDecimal(100)));
			}
			if(null != customer.getMoney()){
				customer.setMoney(customer.getMoney().divide(new BigDecimal(100)));
			}
		});
	  }
      // Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      // 过滤选中数据
      String selections = request.getParameter("selections");
      if(oConvertUtils.isEmpty(selections)) {
    	  mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      }else {
    	  List<String> selectionList = Arrays.asList(selections.split(","));
    	  List<VcCustomer> exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
    	  mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "客户信息列表");
      mv.addObject(NormalExcelConstants.CLASS, VcCustomer.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客户信息列表数据", "导出人:Jeecg", "导出信息"));
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
	  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	  if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()<=0){
	       	return Result.error("您暂时没有该操作权限!!!");
	  }
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<VcCustomer> listVcCustomers = ExcelImportUtil.importExcel(file.getInputStream(), VcCustomer.class, params);
              if(CollectionUtils.isNotEmpty(listVcCustomers)){
				  SysUser user = null;
				  for(int i = 0; i < listVcCustomers.size();i++){
				  	   //先查询这个用户是否存在
					  user = sysUserService.getUserByName(listVcCustomers.get(i).getUserName());
					  if(user != null){
						  listVcCustomers.get(i).setUserId(user.getId());
					  }else{
					  	  //不存在该用户的话删除该用户数据，不进行导入
					  	  listVcCustomers.remove(i);
					  }
					  //判断这个用户是否已存在表里  是的话删除原先的，保存现在的数据
					  VcCustomer customer = vcCustomerService.getCustomerByUserId(user.getId());
					  if(null != customer){
					  	vcCustomerService.removeById(customer.getId());
					  }
					  if(null != listVcCustomers.get(i).getQuota()){
						  listVcCustomers.get(i).setQuota(customer.getQuota().multiply(new BigDecimal(100)));
					  }
					  if(null != listVcCustomers.get(i).getMoney()){
						  listVcCustomers.get(i).setMoney(customer.getMoney().multiply(new BigDecimal(100)));
					  }
				  }
			  }
              vcCustomerService.saveBatch(listVcCustomers);
              return Result.ok("文件导入成功！数据行数:" + listVcCustomers.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
