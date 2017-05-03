package com.leeo.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PROPAGATION_REQUIRED -- 支持当前事务，如果当前没有事务，就新建一个事务，spring默认。
 *
 * PROPAGATION_SUPPORTS -- 支持当前事务，如果当前没有事务，就以非事务方式执行。
 *
 * PROPAGATION_MANDATORY -- 支持当前事务，如果当前没有事务，就抛出异常。
 *
 * PROPAGATION_REQUIRES_NEW -- 新建事务，如果当前存在事务，把当前事务挂起。
 *
 * PROPAGATION_NOT_SUPPORTED -- 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
 *
 * PROPAGATION_NEVER -- 以非事务方式执行，如果当前存在事务，则抛出异常。
 *
 * PROPAGATION_NESTED --
 * 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。
 *
 */
@Service
@Transactional
public class TransServices {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TransServices2 transServices2;

	/**
	 * Exception异常回滚（rollbackFor = Exception.class）
	 *
	 * Spring事务管理默认是针对unchecked exception回滚，也就是默认对RuntimeException
	 * ()异常或是其子类进行事务回滚；checked异常,即Exception可try{}捕获的不会回滚
	 *
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void save() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		throw new Exception("save出错");
	}

	/**
	 * Exception异常不回滚（noRollbackFor = Exception.class）
	 *
	 * @throws Exception
	 */
	@Transactional(noRollbackFor = Exception.class)
	public void save2() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		throw new Exception("save出错");
	}

	/**
	 * 嵌套事物（内部事物配置成Propagation.REQUIRES_NEW）
	 *
	 * 1、如果外部事务（没有配置noRollbackFor）抛了异常，内部事务没有异常，此时外部事务rollback，内部事务commit。
	 * 2、如果外部事务（配置了noRollbackFor）抛了异常，内部事务没有异常，此时外部事务commit，内部事务commit。
	 *
	 * @throws Exception
	 */
	// @Transactional(noRollbackFor = RuntimeException.class)
	public void save3() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		this.transServices2.save();

		throw new RuntimeException("save出错");
	}

	/**
	 * 嵌套事物（内部事物配置成Propagation.REQUIRED）
	 *
	 * 1、如果外部事务（没有配置noRollbackFor）抛了异常，内部事务没有异常，此时整个事务rollback。
	 * 2、如果外部事务（配置了noRollbackFor）抛了异常，内部事务没有异常，此时整个事务commit。
	 *
	 * @throws Exception
	 */
	// @Transactional(noRollbackFor = RuntimeException.class)
	public void save4() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		this.transServices2.save2();

		throw new RuntimeException("save出错");
	}

	/**
	 * 嵌套事物（内部事物配置成Propagation.REQUIRES_NEW）
	 *
	 * 1、如果外部事务（没有配置noRollbackFor）没有异常，内部事务抛了异常，此时整个事务rollback（捕获内部事物异常时，
	 * 外部事务commit，内部事务rollback）。
	 * 2、如果外部事务（配置了noRollbackFor）没有异常，内部事务抛了异常，此时外部事务commit，内部事务rollback。
	 *
	 * @throws Exception
	 */
	// @Transactional(noRollbackFor = RuntimeException.class)
	public void save5() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		try {
			this.transServices2.save3();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 嵌套事物（内部事物配置成Propagation.REQUIRED）
	 *
	 * 1、如果外部事务（没有配置noRollbackFor）没有异常，内部事务抛了异常，此时整个事务rollback。
	 * 2、如果外部事务（配置了noRollbackFor）没有异常，内部事务抛了异常，此时整个事务commit。
	 *
	 * @throws Exception
	 */
	// @Transactional(noRollbackFor = RuntimeException.class)
	public void save6() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		this.transServices2.save4();
	}

	/**
	 * 嵌套事物（内部事物配置成Propagation.NESTED）
	 * transactionManager 的 nestedTransactionAllowed 属性为 true, 此属性默认为 false.
	 *
	 * 1、如果外部事务（没有配置noRollbackFor）没有异常，内部事务抛了异常，此时整个事务rollback（捕获内部事物异常时，
	 * 外部事务commit，内部事务rollback）。
	 * 2、如果外部事务（配置了noRollbackFor）没有异常，内部事务抛了异常，此时外部事务commit,内部事务rollback。
	 *
	 * @throws Exception
	 */
	// @Transactional(noRollbackFor = RuntimeException.class)
	public void save7() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");
		// try {
		this.transServices2.save5();
		// } catch (final Exception e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * 嵌套事物（内部事物配置成Propagation.NESTED）
	 * transactionManager 的 nestedTransactionAllowed 属性为 true, 此属性默认为 false.
	 *
	 * 1、如果外部事务（没有配置noRollbackFor）抛了异常，内部事务没有异常，此时整个事务rollback。
	 * 2、如果外部事务（配置了noRollbackFor）抛了异常，内部事务没有异常，此时外部事务commit,内部事务rollback。
	 *
	 * @throws Exception
	 */
	// @Transactional(noRollbackFor = RuntimeException.class)
	public void save8() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('6','test','test123')");

		this.transServices2.save6();

//		throw new RuntimeException("save出错");
	}
	
	/**
	 * 只读事物测试
	 */
	@Transactional(readOnly=true)
	public void select(){
	    Long count = this.jdbcTemplate.queryForLong("select count(1) from test111");
	    System.out.println("----count:"+count);
	}
}
