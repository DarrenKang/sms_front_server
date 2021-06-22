package ph.sinonet.vg.live.dao;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author Win
 * 
 * @version 2011-4-15 下午04:14:52
 * 
 */
//@Repository
public class UniversalDao extends HibernateDaoSupport {

	public void setMySessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public UniversalDao() {
	}

	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(get(clazz, id));
	}

	public void delete(Object obj) {
		getHibernateTemplate().delete(obj);
	}

	public void deleteAll(Collection entities) {
		getHibernateTemplate().deleteAll(entities);
	}

	public List findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResult) {
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
	}

	public List findByNamedQuery(String sql, Map params) {
		int len = params.size();
		String paramNames[] = new String[len];
		Object paramValues[] = new Object[len];
		int index = 0;
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			paramNames[index] = key;
			paramValues[index++] = params.get(key);
		}

		return getHibernateTemplate().findByNamedQueryAndNamedParam(sql, paramNames, paramValues);
	}

	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	public Object get(Class clazz, Serializable id, LockMode mode) {
		return getHibernateTemplate().get(clazz, id, mode);
	}

	public Integer getCount(Class clazz) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.setProjection(Projections.rowCount());
		Integer count = (Integer) ConvertUtils.convert(getHibernateTemplate().findByCriteria(dc).get(0), Integer.class);
		return count;
	}

	public List loadAll(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	public Object save(Object o) {
		return getHibernateTemplate().save(o);
	}

	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

//	public void saveOrUpdateAll(Collection collection) {
//		getHibernateTemplate().saveOrUpdateAll(collection);
//	}

	public void update(Object o) {
		getHibernateTemplate().update(o);
	}

	private void setParam(SQLQuery sqlQuery, int index, Object param) {
		if (param instanceof Integer) {
			sqlQuery.setInteger(index, (Integer) param);
		} else if (param instanceof Long) {
			sqlQuery.setLong(index, (Long) param);
		} else if (param instanceof Double) {
			sqlQuery.setDouble(index, (Double) param);
		} else if (param instanceof String) {
			sqlQuery.setString(index, (String) param);
		} else if (param instanceof Date) {
			sqlQuery.setDate(index, (Date) param);
		}
	}

	public Object excuteSqlForUniqueResult(String sql, List paramsList) {
		final String excuteSql = sql;
		final List params = paramsList;
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				if (params != null && params.size() > 0) {
					int length = params.size();
					for (int i = 0; i < length; i++) {
						setParam(query, i, params.get(i));
					}
				}
				return query.uniqueResult();
			}
		});
	}
	
	public Object excuteSqlForUniqueResult(String sql) {
		final String excuteSql = sql;
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				return query.uniqueResult();
			}
		});
	}
	

	public List excuteSqlForQuery(String sql, Class entity, List paramsList) {
		final String excuteSql = sql;
		final List params = paramsList;
		final Class cls = entity;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				query.addEntity(cls);
				if (params != null && params.size() > 0) {
					int length = params.size();
					for (int i = 0; i < length; i++) {
						setParam(query, i, params.get(i));
					}
				}
				return query.list();
			}
		});
	}

	public int excuteSqlForUpdate(String sql, List paramsList) {
		final String excuteSql = sql;
		final List params = paramsList;
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				if (params != null && params.size() > 0) {
					int length = params.size();
					for (int i = 0; i < length; i++) {
						setParam(query, i, params.get(i));
					}
				}
				return query.executeUpdate();
			}
		});
	}

	public List excuteSqlForQuery(String sql, List paramsList) {
		final String excuteSql = sql;
		final List params = paramsList;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				if (params != null && params.size() > 0) {
					int length = params.size();
					for (int i = 0; i < length; i++) {
						setParam(query, i, params.get(i));
					}
				}
				return query.list();
			}
		});
	}

	public List excuteSqlForQuery(String sql) {
		final String excuteSql = sql;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				return query.list();
			}
		});
	}

	public List excuteSqlForQuery(String sql, Class c1) {
		final String excuteSql = sql;
		final Class class1 = c1;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				String c1Name = class1.getSimpleName();
				query.addEntity(c1Name.substring(0, 1).toLowerCase(), class1);
				return query.list();
			}
		});
	}

	public List excuteuniteForQuery(Class c1, Class c2, List paramsList, String sql) {
		final String excuteSql = sql;
		final List params = paramsList;
		final Class class1 = c1;
		final Class class2 = c2;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				String c1Name = class1.getSimpleName();
				String c2Name = class2.getSimpleName();
				query.addEntity(c1Name.substring(0, 1).toLowerCase(), class1);
				query.addEntity(c2Name.substring(0, 1).toLowerCase(), class2);
				if (params != null && params.size() > 0) {
					int length = params.size();
					for (int i = 0; i < length; i++) {
						setParam(query, i, params.get(i));
					}
				}
				return query.list();
			}
		});
	}
	
	public List excuteuniteForQuery(Class c1, Class c2, String sql) {
		final String excuteSql = sql;
		final Class class1 = c1;
		final Class class2 = c2;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				String c1Name = class1.getSimpleName();
				String c2Name = class2.getSimpleName();
				query.addEntity(c1Name.substring(0, 1).toLowerCase(), class1);
				query.addEntity(c2Name.substring(0, 1).toLowerCase(), class2);
				return query.list();
			}
		});
	}

	public List excuteuniteForQuery(Class c1, Class c2, List paramsList, String sql, int firstResult, int maxResults) {
		final String excuteSql = sql;
		final List params = paramsList;
		final Class class1 = c1;
		final Class class2 = c2;
		final int pageindex = firstResult;
		final int pagesize = maxResults;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(excuteSql);
				if (pageindex != 0 && pagesize != 0) {
					int startIndex = (pageindex - 1) * pagesize;
					query.setFirstResult(startIndex);
					query.setMaxResults(pagesize);
				}
				String c1Name = class1.getSimpleName();
				String c2Name = class2.getSimpleName();
				query.addEntity(c1Name.substring(0, 1).toLowerCase(), class1);
				query.addEntity(c2Name.substring(0, 1).toLowerCase(), class2);
				if (params != null && params.size() > 0) {
					int length = params.size();
					for (int i = 0; i < length; i++) {
						setParam(query, i, params.get(i));
					}
				}
				return query.list();
			}
		});
	}

	public List findByName(Class clazz, String name, Object value) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(clazz);
		dCriteria = dCriteria.add(Restrictions.eq(name, value));
		return getHibernateTemplate().findByCriteria(dCriteria);
	}

	public List findByName(Class clazz, String name1, Object value1, String name2, Object value2) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(clazz);
		dCriteria = dCriteria.add(Restrictions.eq(name1, value1)).add(Restrictions.eq(name2, value2));
		return getHibernateTemplate().findByCriteria(dCriteria);
	}

	public List findByName(Class clazz, String name1, Object value1, String name2, Object value2, String name3, Object value3) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(clazz);
		dCriteria = dCriteria.add(Restrictions.eq(name1, value1)).add(Restrictions.eq(name2, value2)).add(Restrictions.eq(name3, value3));
		return getHibernateTemplate().findByCriteria(dCriteria);
	}
	
	protected Query createNamedQuery(String queryName){
		return getSessionFactory().getCurrentSession().getNamedQuery(queryName);
	}
	public Date queryDBDate() {
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery sqlQuery = session.createSQLQuery("select now()");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null)
			return date;
		return null;
	}
public Integer queryNumberOfMessage(DetachedCriteria dc){
	Integer totalResults = (Integer) ConvertUtils.convert(
 		   getHibernateTemplate().findByCriteria(dc.setProjection(Projections.rowCount())).get(0), Integer.class);
	dc.setProjection(null);
	return totalResults;
}
public List findByChannel(Integer channel,String product){
	 DetachedCriteria dc =DetachedCriteria.forClass(ph.sinonet.vg.live.model.sms.SmsConfig.class);
	 dc.add(Restrictions.eq("channel", channel));
	 dc.add(Restrictions.eq("platformIds", product));
	return getHibernateTemplate().findByCriteria(dc);
}

public Integer querySumNumberOfMessage(DetachedCriteria dc) {
	// TODO Auto-generated method stub
	Integer totalResults = (Integer) ConvertUtils.convert(
	 		   getHibernateTemplate().findByCriteria(dc.setProjection(Projections.rowCount())).get(0), Integer.class);
		dc.setProjection(null);
		return totalResults;
}
}
