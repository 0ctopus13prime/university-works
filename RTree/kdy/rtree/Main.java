package kdy.rtree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws IOException {
		InputStream in = Main.class.getResourceAsStream("MG.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		Rtree<Integer> rtree = new Rtree<Integer>(6);
		
		//데이터를 전체 삽입한다.
		while( (line = reader.readLine()) != null ) {
			//공백으로 나눔
			String[] points = line.split("\\s");
			
			if( points[1].trim().equals(points[3]) 
					|| points[2].trim().equals(points[4]) ) {
				// 1,1   1,5와 같은 데이터는 받지 않음
				continue;
			}
			
			Rectangle area = new Rectangle(
			new Point(Integer.parseInt(points[1]), Integer.parseInt(points[2])),
			new Point(Integer.parseInt(points[3]), Integer.parseInt(points[4]))
			);
			
			rtree.insert(area, Integer.parseInt(points[0]));
		}
		
		reader.close();
		
		//데이터를 전체 삭제 한다.
		in = Main.class.getResourceAsStream("MG.txt");
		reader = new BufferedReader(new InputStreamReader(in));
				
		while( (line = reader.readLine()) != null ) {
			//공백으로 나눔
			String[] points = line.split("\\s");
			
			if( points[1].trim().equals(points[3]) 
					|| points[2].trim().equals(points[4]) ) {
				// 1,1   1,5와 같은 데이터는 받지 않음
				continue;
			}
			
			Rectangle area = new Rectangle(
			new Point(Integer.parseInt(points[1]), Integer.parseInt(points[2])),
			new Point(Integer.parseInt(points[3]), Integer.parseInt(points[4]))
			);
			
			rtree.delete(area);
		}
		
		reader.close();
	}
}
