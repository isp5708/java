package javaSubject3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class hangManServer {

	static Vector<String> wordVector; // words.txt에서 단어를 읽어와 저장
	static String questionOfWord; // 문제를 담을 변수
	static char[] word;
	static char[] wordAnswer;
	static int changeWordAmount = 2; // 몇개의 단어를 바꿀것인지
	static int time;
	static int cnt;// 게임 카운트 횟수

	// 서버에 필수인 변수들
	static BufferedReader in = null;
	static BufferedWriter out = null;
	static ServerSocket listener = null;
	static Socket soc = null;

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

	public static boolean isCollect() { // 바뀐것이 정답이 맞는지 확인
		for (int i = 0; i < questionOfWord.length(); i++) {
			if (questionOfWord.charAt(i) != word[i])
				return false;
		}
		return true;
	}

	public static void sendMessage(String m, BufferedWriter out) {// 클라한테 보내는 메세지
		try {
			out.write(m);
		} catch (IOException e) {
		}
	}

	public static void checkWord(char w, BufferedWriter out) {

			for (int i = 0; i < changeWordAmount; i++) {
				for (int j = 0; j < wordAnswer.length; j++) {           //유저가 맞출경우
					if (w == wordAnswer[j]) {// 맞는 단어가 있을시 함수 종료  
						word[j] = wordAnswer[j];
						wordAnswer[j] = ' '; // 중복되는 정답이 있을시 비워줘야 바꾸기가능
						return;
					}
				}
			}

			sendMessage("틀렸습니다.\n", out);  //틀릴경우

			cnt--;

	}

	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);
		
		makeWordBook();

		try {

			listener = new ServerSocket(9900);
			System.out.println("연결을 기다리고 있습니다...");
			soc = listener.accept(); // 클라이언트로부터 연결 요청 대기
			System.out.println("연결되었습니다.");

			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));// 클라한테 데이터 받아옴
			out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));// 쓰기
			while (true) {
				makeWord();
				//sendMessage(questionOfWord+"\n",out); //정답도같이보내는 코드
				sendMessage(String.valueOf(word) + "\n" + "*\n", out); // 만든 퀴즈를 클라에게전송
				out.flush();

				cnt = 5;

				while (cnt != 0) {
					String tmp=null;
					tmp = in.readLine();
					
					if(tmp.equals("timeout")) {//시간초과시 받아오는 "\n" 처리
						sendMessage("시간초과 !\n",out);
						cnt--;
					}else if (tmp.length() != 1) {
						sendMessage("한글자를 입력하여주세요\n*\n", out);
						out.flush();
						continue;
					} else {
						checkWord(tmp.charAt(0), out);  //유저의 입력이 맞는지확인
						if (isCollect()) {
							sendMessage("정답입니다.\n"+word+"\n", out);
							break;
						}
					}
					
					if (cnt == 0) { //횟수 0일시 게임오버 메시지 전송후 재시작할지 물어봄
						sendMessage("GameOver!\n", out);
						sendMessage("정답은 " + questionOfWord + "\n", out);
						break;

					} else {
						sendMessage("다시 입력하여주세요.(남은 횟수:" + cnt + ")\n", out);
					}

					sendMessage(String.valueOf(word) + "\n*\n", out); // 바뀌거나 틀린 문제를 다시전송
					out.flush();

				}

				while (true) {
					out.write("Next(y/n)?\n*\n");
					out.flush();

					String tmp = in.readLine();

					if (tmp.equalsIgnoreCase("n")) {
						return;
					} else if (tmp.equalsIgnoreCase("y")) {
						break;
					} else {
						out.write("y/n 만입력하여주세요.\n");
					}

				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				s.close(); // scanner 닫기
				soc.close(); // 통신용 소켓 닫기
				listener.close();// 서버 소켓닫기
			} catch (IOException e) {
				System.out.println("클라이언트와 채팅 중 오류 발생");
			}
		}
	}

}
