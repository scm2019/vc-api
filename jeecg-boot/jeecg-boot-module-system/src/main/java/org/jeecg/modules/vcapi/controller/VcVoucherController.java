package org.jeecg.modules.vcapi.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.vcapi.entity.VcVoucher;
import org.jeecg.modules.vcapi.service.IVcVoucherService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.vcapi.util.SignUtil;
import org.jeecg.modules.vcapi.vo.VcVoucherVo;
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
 * @Description: 虚拟卡券数据
 * @Author: jeecg-boot
 * @Date:   2020-04-24
 * @Version: V1.0
 */
@RestController
@RequestMapping("/vcapi/vcVoucher")
@Slf4j
public class VcVoucherController {

	private final IVcVoucherService vcVoucherService;

	@Autowired
	public VcVoucherController(IVcVoucherService vcVoucherService) {
		this.vcVoucherService = vcVoucherService;
	}
	
	/**
	  * 分页列表查询
	 * @param vcVoucher
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<VcVoucher>> queryPageList(VcVoucherVo vcVoucher,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		Result<IPage<VcVoucher>> result = new Result<IPage<VcVoucher>>();
		Page<VcVoucher> page = new Page<VcVoucher>(pageNo, pageSize);
		IPage<VcVoucher> pageList = vcVoucherService.queryPage(page, vcVoucher);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param vcVoucher
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<VcVoucher> add(@RequestBody VcVoucher vcVoucher) {
		Result<VcVoucher> result = new Result<VcVoucher>();
		try {
			vcVoucher.setBalance(vcVoucher.getAmount());
			vcVoucher.setPassword(StringUtils.isNotEmpty(vcVoucher.getPassword()) ? SignUtil.encode(vcVoucher.getPassword().getBytes()) : null);
			vcVoucherService.save(vcVoucher);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param vcVoucher
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<VcVoucher> edit(@RequestBody VcVoucher vcVoucher) {
		Result<VcVoucher> result = new Result<VcVoucher>();
		try {
			VcVoucher vcVoucherEntity = vcVoucherService.getById(vcVoucher.getId());
			if (vcVoucherEntity == null) {
				result.error500("未找到对应实体");
			} else {
				vcVoucher.setPassword(StringUtils.isNotEmpty(vcVoucher.getPassword()) ? SignUtil.encode(vcVoucher.getPassword().getBytes()) : null);
				boolean ok = vcVoucherService.updateById(vcVoucher);
				//TODO 返回false说明什么？
				if (ok) {
					result.success("修改成功!");
				}
			}
		}catch (Exception e){
			log.error(e.getMessage(),e);
			result.error500("操作失败");
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
		try {
			vcVoucherService.removeById(id);
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
	public Result<VcVoucher> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<VcVoucher> result = new Result<VcVoucher>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.vcVoucherService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<VcVoucher> queryById(@RequestParam(name="id",required=true) String id) {
		Result<VcVoucher> result = new Result<VcVoucher>();
		try{
			VcVoucher vcVoucher = vcVoucherService.getById(id);
			vcVoucher.setPassword(StringUtils.isNotEmpty(vcVoucher.getPassword()) ? SignUtil.decode(vcVoucher.getPassword().getBytes()) : null);
			if(vcVoucher==null) {
				result.error500("未找到对应实体");
			}else {
				result.setResult(vcVoucher);
				result.setSuccess(true);
			}
		}catch (Exception e){
			log.error("删除失败",e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, VcVoucher vcVoucher) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<VcVoucher> queryWrapper = QueryGenerator.initQueryWrapper(vcVoucher, request.getParameterMap());
      List<VcVoucher> pageList = vcVoucherService.list(queryWrapper);
      // Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      // 过滤选中数据
      String selections = request.getParameter("selections");
      if(oConvertUtils.isEmpty(selections)) {
    	  mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      }else {
    	  List<String> selectionList = Arrays.asList(selections.split(","));
    	  List<VcVoucher> exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
    	  mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "虚拟卡券数据列表");
      mv.addObject(NormalExcelConstants.CLASS, VcVoucher.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("虚拟卡券数据列表数据", "导出人:Jeecg", "导出信息"));
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<VcVoucher> listVcVouchers = ExcelImportUtil.importExcel(file.getInputStream(), VcVoucher.class, params);
              vcVoucherService.saveBatch(listVcVouchers);
              return Result.ok("文件导入成功！数据行数:" + listVcVouchers.size());
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
