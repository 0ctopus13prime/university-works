package bplustree;

public interface BPlusTree<KEY extends Comparable, DATA> {
	public void insert(KEY key, DATA data);
	public void delete(KEY key);
	public DATA find(KEY key);
	public void printAll();
}
