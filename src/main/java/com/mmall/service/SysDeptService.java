package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.Date;
import java.util.List;

/**
 * @author Alan Brown
 * @date 2018/5/24 19:45
 */
@Service
public class SysDeptService {

	@Resource
	private SysDeptMapper sysDeptMapper;
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysLogService sysLogService;

	// 保存部门
	public void save(DeptParam param){
		BeanValidator.check(param); // 检查参数是否合要求
		// 如果部门已经存在
		if(checkExist(param.getParentId(), param.getName(), param.getId())){
			throw new ParamException("同一层级下存在相同名称的部门");
		}
		// 创建这个部门
		SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
				.seq(param.getSeq()).remark(param.getRemark()).build();
		String level = LevelUtil.calculateLevel( getLevel(param.getParentId()), param.getParentId() );
		dept.setLevel( level );
		dept.setOperator(RequestHolder.getCurrentUser().getUsername());
		dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		dept.setOperateTime(new Date());
		// 保存这个部门到数据库
		// 注意，有两个函数，insert和insertSelective
		// 前者要求具有所有属性且满足要求，后者只插入具有值的属性
		sysDeptMapper.insertSelective(dept);
		sysLogService.saveDeptLog(null, dept);
	}

	// 更新部门
	public void update(DeptParam param){
		BeanValidator.check(param); // 检查参数是否合要求
		// 检查待更新的部门是否存在
		SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before, "待更新的部门不存在");
		// 如果同一层级下有同名的部门
		if(checkExist(param.getParentId(), param.getName(), param.getId())){
			throw new ParamException("同一层级下存在相同名称的部门");
		}

		// 创建这个部门
		SysDept after = SysDept.builder().id(param.getId()).name(param.getName())
				.parentId(param.getParentId())
				.seq(param.getSeq()).remark(param.getRemark())
				.build();
		String level = LevelUtil.calculateLevel( getLevel(param.getParentId()), param.getParentId() );
		after.setLevel( level );
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		after.setOperateTime(new Date());
		updateWithChild(before, after);
		sysLogService.saveDeptLog(before, after);
	}

	// 更新该部门以及该部门的子部门
	// 事务，要么更新成功，要么更新失败回滚
	@Transactional
	public void updateWithChild(SysDept before, SysDept after){
		String newLevelPrefix = after.getLevel();
		String oldLevelPrefix = before.getLevel();
		// 如果修改前后的部门级别不同，就需要更改它的子部门的level
		if (!after.getLevel().equals(before.getLevel())) {
			// 获取修改前该部门的子部门，以及子部门的子部门，以此类推
			List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix, before.getParentId());
			if (CollectionUtils.isNotEmpty(deptList)) {
				for (SysDept dept : deptList) {
					String level = dept.getLevel(); // 该部门的级别
					// 如果该级别是以，修改前的待修改部门的级别开头的
					if (level.indexOf(oldLevelPrefix) == 0) {
						// 那么就用待更新部门的新级别替换到修改前的级别
						level = newLevelPrefix + level.substring(oldLevelPrefix.length());
						dept.setLevel(level);
					}
				}
				// 批量更新这些部门的level
				sysDeptMapper.batchUpdateLevel(deptList);
			}
		}
		// 更新这个部门的信息
		sysDeptMapper.updateByPrimaryKey(after);
	}

	// 删除部门
	public void delete(int deptId) {
		SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
		Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
		if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
			throw new ParamException("当前部门下面有子部门，无法删除");
		}
		if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
			throw new ParamException("当前部门下面有用户，无法删除");
		}
		sysDeptMapper.deleteByPrimaryKey(deptId);
	}

	/**
	 * 检查该部门是否已经存在
	 * @param parentId 上级部门的id
	 * @param deptName 部门名称
	 * @param deptId 部门id
	 * @return true存在,false不存在
	 */
	private boolean checkExist(Integer parentId, String deptName, Integer deptId){
		return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
	}

	// 根据部门id获取该部门的级别，部门不存在的话就返回null
	private String getLevel(Integer deptId){
		SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
		if(dept == null)
			return null;
		return dept.getLevel();
	}
}
