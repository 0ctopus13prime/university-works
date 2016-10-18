package fakeecommerce.database.resources;

/**
 * Created by buzz on 2016. 1. 3..
 */
public interface KeyGenerator {
	void load();
	long nextLongId();
	int nextIntId();
	String nextStringId();
	void flush();
}
