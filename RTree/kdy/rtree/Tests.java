package kdy.rtree;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Tests {
	//@Test
	public void RectangleTest1() {
		/*
		 * 겹치는지 MBR확장 문제
		 */
		Rectangle rec1, rec2, rec3;
		rec1 = new Rectangle(new Point(5, 5), new Point(20, 20));
		rec2 = new Rectangle(new Point(10, 15), new Point(30, 40));
		rec3 = new Rectangle(new Point(45, 15), new Point(50, 15));
		
		assertThat(rec1.isOverlap(rec2), is(true));
		assertThat(rec2.isOverlap(rec1), is(true));
		
		assertThat(rec2.isOverlap(rec3), is(false));
		assertThat(rec3.isOverlap(rec2), is(false));
		
		assertThat(rec1.isOverlap(rec3), is(false));
		assertThat(rec3.isOverlap(rec1), is(false));
		
		assertThat(rec1.getEnlargeMBRArea(rec2), is(25*35 - 15*15));
		assertThat(rec2.getEnlargeMBRArea(rec1), is(25*35 - 20*25));
		   
		Rectangle rec4 = new Rectangle(new Point(844, 1341), new Point(2290, 3000));
		assertThat(rec4.isOverlap(new Rectangle(new Point(2273, 1341), new Point(2290, 3000))),
				is(true));
	}
	
	//@Test
	public void NodeTest1() {
		
	}
	
	@Test
	public void ETCTest() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(13);
		list.add(14);
		list.add(15);
		
		System.out.println(list.get(list.size()-1));
	}
	
}

