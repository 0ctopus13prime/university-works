package fakeecommerce;

import fakeecommerce.launch.FakeproductSystem;
import fakespring.AnnotationFakeSpring;
import fakespring.FakeSpring;

import java.io.IOException;

public class Main {
	public static void main(String ... args) throws IOException {
		FakeSpring fakeSpring = new AnnotationFakeSpring();
		fakeSpring.assemble("fakeecommerce");

		FakeproductSystem fakeproductSystem = fakeSpring.getBean(FakeproductSystem.class);
		fakeproductSystem.launch();

		fakeSpring.shutdown();
	}
}
