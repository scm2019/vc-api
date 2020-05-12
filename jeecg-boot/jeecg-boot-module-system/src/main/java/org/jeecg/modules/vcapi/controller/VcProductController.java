package org.jeecg.modules.vcapi.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.impl.SysDictServiceImpl;
import org.jeecg.modules.vcapi.entity.VcProduct;
import org.jeecg.modules.vcapi.enums.RoleCodeEnum;
import org.jeecg.modules.vcapi.service.IVcProductService;
import org.jeecg.modules.vcapi.service.VcRechargeService;
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
import com.alibaba.fastjson.JSON;

 /**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2020-04-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/vcapi/vcProduct")
@Slf4j
public class VcProductController {

	private final IVcProductService vcProductService;
	 private final ISysUserRoleService userRoleService;

	@Autowired
	public VcProductController(IVcProductService vcProductService,ISysUserRoleService userRoleService) {
		this.vcProductService = vcProductService;
		this.userRoleService = userRoleService;
	}
	
	/**
	  * 分页列表查询
	 * @param vcProduct
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<VcProduct>> queryPageList(VcProduct vcProduct,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		Result<IPage<VcProduct>> result = new Result<IPage<VcProduct>>();
		QueryWrapper<VcProduct> queryWrapper = QueryGenerator.initQueryWrapper(vcProduct, req.getParameterMap());
		Page<VcProduct> page = new Page<VcProduct>(pageNo, pageSize);
		IPage<VcProduct> pageList = vcProductService.page(page, queryWrapper);
		if(CollectionUtils.isNotEmpty(pageList.getRecords())){
			pageList.getRecords().stream().forEach(product -> {
				if(null != product.getSalePrice()){
					product.setSalePrice(product.getSalePrice().divide(new BigDecimal(100)));
				}
				if(null != product.getPrice()){
					product.setPrice(product.getPrice().divide(new BigDecimal(100)));
				}
			});
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param vcProduct
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<VcProduct> add(@RequestBody VcProduct vcProduct) {
		Result<VcProduct> result = new Result<VcProduct>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(userRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()<=0){
			result.error500("您暂时没有该操作的权限！！！");
		}else{
			try {
				if(null != vcProduct.getSalePrice()){
					vcProduct.setSalePrice(vcProduct.getSalePrice().multiply(new BigDecimal(100)));
				}
				if(null != vcProduct.getPrice()){
					vcProduct.setPrice(vcProduct.getPrice().multiply(new BigDecimal(100)));
				}
				vcProductService.save(vcProduct);
				result.success("添加成功！");
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				result.error500("操作失败");
			}
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param vcProduct
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<VcProduct> edit(@RequestBody VcProduct vcProduct) {
		Result<VcProduct> result = new Result<VcProduct>();
		VcProduct vcProductEntity = vcProductService.getById(vcProduct.getId());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(userRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()<=0){
			result.error500("您暂时没有该操作的权限！！！");
		}else{
			if(vcProductEntity==null) {
				result.error500("未找到对应实体");
			}else {
				if(null != vcProduct.getSalePrice()){
					vcProduct.setSalePrice(vcProduct.getSalePrice().multiply(new BigDecimal(100)));
				}
				if(null != vcProduct.getPrice()){
					vcProduct.setPrice(vcProduct.getPrice().multiply(new BigDecimal(100)));
				}
				boolean ok = vcProductService.updateById(vcProduct);
				//TODO 返回false说明什么？
				if(ok) {
					result.success("修改成功!");
				}
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
		if(userRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()<=0){
			return Result.error("您暂时没有该操作的权限！！！");
		}else{
			try {
				vcProductService.removeById(id);
			} catch (Exception e) {
				log.error("删除失败",e.getMessage());
				return Result.error("删除失败!");
			}
			return Result.ok("删除成功!");
		}

	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<VcProduct> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VcProduct> result = new Result<VcProduct>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(userRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()<=0){
			result.error500("您暂时没有该操作的权限！！！");
		}else {
			if(ids==null || "".equals(ids.trim())) {
				result.error500("参数不识别！");
			}else {
				this.vcProductService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<VcProduct> queryById(@RequestParam(name="id",required=true) String id) {
		Result<VcProduct> result = new Result<VcProduct>();
		VcProduct vcProduct = vcProductService.getById(id);
		if(vcProduct==null) {
			result.error500("未找到对应实体");
		}else {
			if(null != vcProduct.getSalePrice()){
				vcProduct.setSalePrice(vcProduct.getSalePrice().divide(new BigDecimal(100)));
			}
			if(null != vcProduct.getPrice()){
				vcProduct.setPrice(vcProduct.getPrice().divide(new BigDecimal(100)));
			}
			result.setResult(vcProduct);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, VcProduct vcProduct) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<VcProduct> queryWrapper = QueryGenerator.initQueryWrapper(vcProduct, request.getParameterMap());
      List<VcProduct> pageList = vcProductService.list(queryWrapper);
	  if(CollectionUtils.isNotEmpty(pageList)){
		  pageList.stream().forEach(product -> {
			  if(null != product.getSalePrice()){
				  product.setSalePrice(product.getSalePrice().divide(new BigDecimal(100)));
			  }
			  if(null != product.getPrice()){
				  product.setPrice(product.getPrice().divide(new BigDecimal(100)));
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
    	  List<VcProduct> exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
    	  mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "产品表列表");
      mv.addObject(NormalExcelConstants.CLASS, VcProduct.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("产品表列表数据", "导出人:Jeecg", "导出信息"));
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
	  if(userRoleService.getUserRole(sysUser.getUsername()).stream().filter(role->(RoleCodeEnum.ADMIN.getRoleCode().equals(role))).collect(Collectors.toList()).size()<=0){
		  return Result.error("您暂时没有该操作的权限！！！");
	  }else{
		  MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		  Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		  for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			  MultipartFile file = entity.getValue();// 获取上传文件对象
			  ImportParams params = new ImportParams();
			  params.setTitleRows(2);
			  params.setHeadRows(1);
			  params.setNeedSave(true);
			  try {
				  List<VcProduct> listVcProducts = ExcelImportUtil.importExcel(file.getInputStream(), VcProduct.class, params);
				  if(CollectionUtils.isNotEmpty(listVcProducts)){
					  listVcProducts.stream().forEach(product -> {
						  if(null != product.getSalePrice()){
							  product.setSalePrice(product.getSalePrice().multiply(new BigDecimal(100)));
						  }
						  if(null != product.getPrice()){
							  product.setPrice(product.getPrice().multiply(new BigDecimal(100)));
						  }
					  });
				  }
				  vcProductService.saveBatch(listVcProducts);
				  return Result.ok("文件导入成功！数据行数:" + listVcProducts.size());
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

}
