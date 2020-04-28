package bb;

import java.awt.desktop.PrintFilesEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class hangMan {
	static Vector<String> wordVector; // words.txt에서 단어를 읽어와 저장
	static String questionOfWord; // 문제를 담을 변수
	static char[] word;
	static char[] wordAnswer;
	static int changeWordAmount = 2; // 몇개의 단어를 바꿀것인지
	static int time;
	static int cnt;// 게임 카운트 횟수
	static timerThread tt;
	
	
	public static void makeWord() {
		questionOfWord = wordVector.get((int) (Math.random() * wordVector.size())); // 이중 랜덤으로 단어하나 선택
		wordAnswer = new char[questionOfWord.length()];
		word = new char[questionOfWord.length()];

		for (int i = 0; i < questionOfWord.length(); i++) {
			word[i] = questionOfWord.charAt(i);
		}

		for (int i = 0; i < changeWordAmount; i++) {
			int n = (int) (Math.random() * word.length); // 구멍을 뚫어놓을곳을 랜덤으로 인덱스생성

			if (word[n] == '-') { // 이미 -가 있을것을 대비하여 예외처리
				i--;
			} else {
				wordAnswer[n] = word[n]; // 정답을 배열 인덱스에 저장해놈
				word[n] = '-';
			}
		}
	}

	public static void checkWord(char w,hangMan hm) {
		for (int i = 0; i < changeWordAmount; i++) {
			for (int j = 0; j < wordAnswer.length; j++) {
				if (w == wordAnswer[j]) {// 맞는 단어가 있을시 함수 종료
					word[j] = wordAnswer[j];
					wordAnswer[j] = ' '; // 중복되는 정답이 있을시 비워줘야 바꾸기가능
					System.out.println(word);
					//인터럽트 걸어야하는부분 타이머 재가동
					hm.tt.interrupt();
					hm.makeTimer(hm);
					return;
				}
			}
		}
		System.out.println("틀렸습니다.");
		System.out.println(word);
		cnt--;
		//틀렸을시 인터럽트 거는부분
		
		
		
		if (cnt == 0) {
			System.out.println("GameOver!");
			System.out.println("정답은 :" + questionOfWord);
		} else {
			System.out.println("다시 입력하여주세요.(남은 횟수:" + cnt + ")");
			
			hm.tt.interrupt();
			hm.makeTimer(hm);
			//타이머다시가동
			
		}

	}

	public static boolean isCollect() { // 바뀐것이 정답이 맞는지 확인
		for (int i = 0; i < questionOfWord.length(); i++) {
			if (questionOfWord.charAt(i) != word[i])
				return false;
		}
		return true;
	}
	public static void makeTimer(hangMan hm) {
		hm.time=5;
		hm.tt=new timerThread(hm);
		hm.tt.start();
	}

	public static void hangMan_menu_print(hangMan hm) { // 행맨 게임 메인메뉴
		hm.makeWordBook();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("지금부터 행맨 게임을 시작합니다.");

		while (true) {

			hm.makeWord(); // 문제에낼 단어를 랜덤으로 생성
			System.out.println(hm.word);
			//System.out.println(hm.questionOfWord); //정답을 적어놔 테스트하는 코드
			hm.cnt = 5; // 횟수 5회로 카운트설정
			
			hm.makeTimer(hm); //타이머 가동
			
			while (hm.cnt != 0) {
				String tmp;
				
				try {
					
					
					if (br.ready()) {
						tmp = br.readLine();

						if (tmp.length() != 1) {
							System.out.println("한글자를 입력하여주세요");
						} else {
							hm.checkWord(tmp.charAt(0),hm);
							
							if (hm.isCollect()) {
								System.out.println("정답입니다.");
								break;
							}
						}
					}
				} catch (IOException e) {

				}	
				
				if(hm.time==0) {
					hm.cnt--;
					System.out.println("시간초과 남은횟수:"+hm.cnt);
					hm.makeTimer(hm);
				}
				
			}
			hm.tt.interrupt();

			while (true) { // 더진행 할 것인지 while문을 통해 질문
				System.out.println("Next(y/n)?");
				Scanner s = new Scanner(System.in);
				String input = s.nextLine();
				
				if (input.equals("n")) {
					return;
				} else if (input.equals("y")) {
					break;
				} else {
					System.out.println("n/y둘중하나만 입력하여주세요.");
				}
			}

		}
	}

	public static void makeWordBook() {
		BufferedReader br;
		File f = new File("E:\\메모장\\words.txt"); // 파일 경로지정
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8")); // BufferedReader를 통해 단어들을
																								// 읽어옴

			String line = null;
			wordVector = new Vector<String>();

			while ((line = br.readLine()) != null) { // 한줄씩 단어를 읽어옴
				wordVector.add(line);
			}
		} catch (IOException e) {
			System.out.println("단어장을 불러오지 못했습니다.");
		}
	}

	public static void main(String[] args) throws IOException {

		hangMan hm=new hangMan();
		
		hm.hangMan_menu_print(hm);

	}

}
