package org.jeecg.modules.vcapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.enums.RoleCodeEnum;
import org.jeecg.modules.vcapi.service.IVcOrderRechargeService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: vc_order_recharge
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
@RestController
@RequestMapping("/vcapi/vcOrderRecharge")
@Slf4j
public class VcOrderRechargeController {

	private final IVcOrderRechargeService vcOrderRechargeService;

	private final ISysUserRoleService sysUserRoleService;


	@Autowired
	public VcOrderRechargeController(IVcOrderRechargeService vcOrderRechargeService,ISysUserRoleService sysUserRoleService){
		this.vcOrderRechargeService = vcOrderRechargeService;
		this.sysUserRoleService = sysUserRoleService;
	}
	
	/**
	  * 分页列表查询
	 * @param vcOrderRecharge
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
//	@GetMapping(value = "/list")
//	public Result<?> queryPageList(VcOrderRecharge vcOrderRecharge,
//														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//														HttpServletRequest req) {
//		Result<IPage<VcOrderRecharge>> result = new Result<IPage<VcOrderRecharge>>();
//		QueryWrapper<VcOrderRecharge> queryWrapper = QueryGenerator.initQueryWrapper(vcOrderRecharge, req.getParameterMap());
//		Page<VcOrderRecharge> page = new Page<VcOrderRecharge>(pageNo, pageSize);
//		IPage<VcOrderRecharge> pageList = vcOrderRechargeService.page(page, queryWrapper);
//		result.setSuccess(true);
//		result.setResult(pageList);
//		return result;
//	}

	 /**
	  * 分页列表查询
	  * @param vcOrderRecharge
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/pageList")
	 public Result<?> queryList(VcOrderRecharge vcOrderRecharge,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Result<IPage<VcOrderRecharge>> result = new Result<IPage<VcOrderRecharge>>();
		 Page<VcOrderRecharge> page = new Page<VcOrderRecharge>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 Boolean isAdmin = false;
			 if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			 //管理员的查看全部人员的
			 isAdmin = true;
		 }else if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.CUSTOMER.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
			 isAdmin = false;
		 }else{
			 return Result.error("您暂时没有权限查看该页面");
		 }
		 IPage<VcOrderRecharge> pageList = vcOrderRechargeService.queryPage(page, vcOrderRecharge,isAdmin,sysUser.getUsername());
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	/**
	  *   添加
	 * @param vcOrderRecharge
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<VcOrderRecharge> add(@RequestBody VcOrderRecharge vcOrderRecharge) {
		Result<VcOrderRecharge> result = new Result<VcOrderRecharge>();
		try {
			vcOrderRechargeService.save(vcOrderRecharge);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param vcOrderRecharge
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<VcOrderRecharge> edit(@RequestBody VcOrderRecharge vcOrderRecharge) {
		Result<VcOrderRecharge> result = new Result<VcOrderRecharge>();
		VcOrderRecharge vcOrderRechargeEntity = vcOrderRechargeService.getById(vcOrderRecharge.getId());
		if(vcOrderRechargeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = vcOrderRechargeService.updateById(vcOrderRecharge);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
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
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size() <=0) {
			return Result.error("您暂时无该操作的权限!!!");
		}
		try {
			vcOrderRechargeService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<VcOrderRecharge> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VcOrderRecharge> result = new Result<VcOrderRecharge>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size() <=0) {
			result.error500("您暂时无该操作的权限！！！");
		}else{
			if(ids==null || "".equals(ids.trim())) {
				result.error500("参数不识别！");
			}else {
				this.vcOrderRechargeService.removeByIds(Arrays.asList(ids.split(",")));
				result.success("删除成功!");
			}
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<VcOrderRecharge> queryById(@RequestParam(name="id",required=true) String id) {
		Result<VcOrderRecharge> result = new Result<VcOrderRecharge>();
		VcOrderRecharge vcOrderRecharge = vcOrderRechargeService.getById(id);
		if(vcOrderRecharge==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(vcOrderRecharge);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, VcOrderRecharge vcOrderRecharge) {
	  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	  Boolean isAdmin = false;
	  if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
		  //管理员的查看全部人员的
		  isAdmin = true;
	  }else if(sysUserRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.CUSTOMER.getRoleCode().equals(role))).collect(Collectors.toList()).size()>0){
		  isAdmin = false;
	  }
	  List<VcOrderRecharge> list = vcOrderRechargeService.exportExcelData( vcOrderRecharge,isAdmin,sysUser.getUsername());
      // Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      // 过滤选中数据
      String selections = request.getParameter("selections");
      if(oConvertUtils.isEmpty(selections)) {
    	  mv.addObject(NormalExcelConstants.DATA_LIST, list);
      }else {
    	  List<String> selectionList = Arrays.asList(selections.split(","));
    	  List<VcOrderRecharge> exportList = list.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
    	  mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "vc_order_recharge列表");
      mv.addObject(NormalExcelConstants.CLASS, VcOrderRecharge.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("vc_order_recharge列表数据", "导出人:"+sysUser.getUsername(), "导出信息"));
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
              List<VcOrderRecharge> listVcOrderRecharges = ExcelImportUtil.importExcel(file.getInputStream(), VcOrderRecharge.class, params);
              vcOrderRechargeService.saveBatch(listVcOrderRecharges);
              return Result.ok("文件导入成功！数据行数:" + listVcOrderRecharges.size());
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
