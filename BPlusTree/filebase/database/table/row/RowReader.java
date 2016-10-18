package fakeecommerce.database.table.row;


import fakeecommerce.database.btree.Page;

import java.util.List;

/**
 * Created by buzz on 2016. 1. 2..
 */
public interface RowReader {
	<T> T readRow(Page page, Class<T> clazz);
	<T> List<T> readRows(Page page, Class<T> clazz);
	<T> List<T> readRows(Page page, int howmuch, Class<T> clazz);
}
