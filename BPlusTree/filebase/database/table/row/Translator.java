package fakeecommerce.database.table.row;

/**
 * Created by buzz on 2016. 1. 2..
 */
public interface Translator {
	String translateToString(Object target);
	<T> T translateToObject(String string, Class<T> clazz);
}
