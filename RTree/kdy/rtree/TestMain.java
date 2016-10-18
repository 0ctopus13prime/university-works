package kdy.rtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/*
 * 삽입,
 * 삭제, 
 * 검색을 테스트 및 시간 체크
 * 
 * 스플릿 방식에 따른 비교시 Node의 split을 일일이 바꾸면서 측정하였다.
 * 
 * 김두용 12090850
 * 2015 05 25
 */
public class TestMain {
	public static void main(String []args) throws IOException {
		//현재 패키지에서 소스를 얻음
		InputStream in = TestMain.class.getResourceAsStream("MG.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		
		long s = System.currentTimeMillis();
		Rtree<Integer> rtree = new Rtree<Integer>(6);
		int testCase = 40000;
		
		//삽입
		int i = 0;
		while( (line = reader.readLine()) != null ) {
			String[] points = line.split("\\s");
			//x1과 x2가 같은 데이터는 오류가 나서 버림
			if( points[1].trim().equals(points[3]) 
					|| points[2].trim().equals(points[4]) ) {
				continue;
			}
			
			i++;
			if( i > testCase ) {
				break;
			}
			
			//영역으로 만들어 데이터와 삽입
			Rectangle area = new Rectangle(
			new Point(Integer.parseInt(points[1]), Integer.parseInt(points[2])),
			new Point(Integer.parseInt(points[3]), Integer.parseInt(points[4]))
			);
			rtree.insert(area, Integer.parseInt(points[0]));
			//rtree.test();
		}
		reader.close();
		//rtree.test();
				
		long e = System.currentTimeMillis();
		System.out.println("insert result : " + (e-s) );
		
		s = System.currentTimeMillis();
		
		//삭제 테스트
		//System.setOut(new PrintStream(new File("C:\\Users\\13prime\\Desktop\\delete.txt")));
		in = TestMain.class.getResourceAsStream("MG.txt");
		reader = new BufferedReader(new InputStreamReader(in));
		
		i = 0;
		while( (line = reader.readLine()) != null ) {
			String[] points = line.split("\\s");
			if( points[1].trim().equals(points[3]) 
					|| points[2].trim().equals(points[4]) ) {
				continue;
			}
			i++;
			if( i > testCase ) {
				break;
			}
			
			testDelete(rtree, line.trim());
		}
		
		reader.close();
		rtree.test();
		
		e = System.currentTimeMillis();
		System.out.println("delete result : " + (e-s) );
		
		System.exit(0);
		
		//서치 테스트
		//System.setOut(new PrintStream(new File("C:\\Users\\13prime\\Desktop\\search.txt")));
		 
		in = TestMain.class.getResourceAsStream("MG.txt");
		reader = new BufferedReader(new InputStreamReader(in));
		
		i = 0;
		while( (line = reader.readLine()) != null ) {
			String[] points = line.split("\\s");
			if( points[1].trim().equals(points[3]) 
					|| points[2].trim().equals(points[4]) ) {
				continue;
			}
			
			i++;
			if( i > testCase ) {
				break;
			}
			
			testSearch(rtree, line.trim());
		}
		
		reader.close();
	}
	
	public static void testDelete(Rtree<Integer> rtree, 
			String hint) {
		String[] split = hint.split(" ");
		int x1 = Integer.parseInt(split[1]);
		int y1 = Integer.parseInt(split[2]);
		int x2 = Integer.parseInt(split[3]);
		int y2 = Integer.parseInt(split[4]);
		
		rtree.delete(new Rectangle(new Point(x1, y1), 
				new Point(x2 , y2)));
	}
	
	public static void testSearch(Rtree<Integer> rtree, 
						String hint) {
		String[] split = hint.split(" ");
		int expected = Integer.parseInt(split[0]);
		int x1 = Integer.parseInt(split[1]);
		int y1 = Integer.parseInt(split[2]);
		int x2 = Integer.parseInt(split[3]);
		int y2 = Integer.parseInt(split[4]);
		
		if( x1 == x2 || y1 == y2) {
			return;
		}
		
		Integer num = rtree.search(new Rectangle(new Point(x1, y1), 
				new Point(x2 , y2)));
		if( num != null ) {
			if( num == expected ) {
				//System.out.println("testSearch for " + expected + " success!");
			} else {
				//System.out.println("testSearch for " + expected + " failed! it was " + num);
			}
		} else {
			//System.out.println("testSearch for " + expected + " failed! it was NULL!");
		}
	}
}
