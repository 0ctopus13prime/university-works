package fakeecommerce.database.table;

import java.util.List;

/**
 * Created by buzz on 2016. 1. 2..
 */
public interface TableManager {
	void insert(Object object);
	void insertBulk(List list);
	Object getByKey(Object object);
	List getByCondition(String fieldName, Object fieldValue);
	void update(Object object, String fieldName, Object fieldValue);
	void flush();
}
