package kdy.vm.loader;

import kdy.vm.Source;

/*
 * 로더 인터페이스
 */
public interface Loader {
	//소스를 얻는다.
	//소스를 얻는 방법은 구현에 맡김
	public Source getSource();
}
