package bplustree;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		/*
		 * 과제		
		 */
		
		String data = "0 3/1 2/2 8/3 4/4 7/5 1/6 5/7 9/8 6/9 10/10 0";
		//4차 B+트리
		BPlusTree bpt = new BPlusTreeImpl<Integer, Integer>(4);
		
		for(String part1 : data.split("/") ) {
			String[] part2 = part1.split(" ");
			
			System.out.println("KEY " + part2[1] + "  DATA " + part2[0] + "   INSERT");
			//어드레스 키   <- 이런 구조임
			bpt.insert(Integer.valueOf(part2[1]), Integer.valueOf(part2[0]));
		}
		
		bpt.printAll();
	}

}
