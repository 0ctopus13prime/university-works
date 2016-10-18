package kdy.vm;

import kdy.vm.cpu.Cpu;
import kdy.vm.loader.Loader;
import kdy.vm.memory.Memory;

public interface VirtualMachine {
	public void setLoader(Loader loader);
	public void execute();
}
