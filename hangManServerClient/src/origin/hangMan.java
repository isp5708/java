package origin;

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
	static Vector<String> wordVector; // words.txt���� �ܾ �о�� ����
	static String questionOfWord; // ������ ���� ����
	static char[] word;
	static char[] wordAnswer;
	static int changeWordAmount = 2; // ��� �ܾ �ٲܰ�����
	static int cnt;// ���� ī��Ʈ Ƚ��

	public static void makeWord() {
		questionOfWord = wordVector.get((int) (Math.random() * wordVector.size())); // ���� �������� �ܾ��ϳ� ����
		wordAnswer = new char[questionOfWord.length()];
		word = new char[questionOfWord.length()];

		for (int i = 0; i < questionOfWord.length(); i++) {
			word[i] = questionOfWord.charAt(i);
		}

		for (int i = 0; i < changeWordAmount; i++) {
			int n = (int) (Math.random() * word.length); //������ �վ�������� �������� �ε�������

			if (word[n] == '-') { //�̹� -�� �������� ����Ͽ� ����ó��
				i--;
			} else {
				wordAnswer[n] = word[n]; //������ �迭 �ε����� �����س�
				word[n] = '-';
			}
		}
	}

	public static void checkWord(char w) {
		for (int i = 0; i < changeWordAmount; i++) {
			for (int j = 0; j < wordAnswer.length; j++) {
				if (w == wordAnswer[j]) {// �´� �ܾ ������ �Լ� ����
					word[j] = wordAnswer[j];
					wordAnswer[j]=' '; //�ߺ��Ǵ� ������ ������ ������ �ٲٱⰡ��
					System.out.println(word);
					return;
				}
			}
		}
		System.out.println("Ʋ�Ƚ��ϴ�.");
		System.out.println(word);
		cnt--;

		if (cnt == 0) {
			System.out.println("GameOver!");
			System.out.println("������ :" + questionOfWord);
		} else {
			System.out.println("�ٽ� �Է��Ͽ��ּ���.(���� Ƚ��:" + cnt + ")");
		}

	}

	public static boolean isCollect() { //�ٲ���� ������ �´��� Ȯ��
		for (int i = 0; i < questionOfWord.length(); i++) {
			if (questionOfWord.charAt(i) != word[i])
				return false;
		}
		return true;
	}

	public static void hangMan_menu_print() { //��� ���� ���θ޴�
		makeWordBook();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("���ݺ��� ��� ������ �����մϴ�.");

		while (true) {
			
			makeWord(); //�������� �ܾ �������� ����
			System.out.println(word);

			cnt = 5; //Ƚ�� 5ȸ�� ī��Ʈ����

			while (cnt != 0) {
				String tmp;
				try {
					tmp = br.readLine();

					if (tmp.length() != 1) {
						System.out.println("�ѱ��ڸ� �Է��Ͽ��ּ���");
					} else {
						checkWord(tmp.charAt(0));

						if (isCollect()) {
							System.out.println("�����Դϴ�.");
							break;
						}
					}
				} catch (IOException e) {

				}
			}

			while (true) { //������ �� ������ while���� ���� ����
				System.out.println("Next(y/n)?");
				Scanner s = new Scanner(System.in);
				String input = s.nextLine();

				if (input.equals("n")) {
					return;
				} else if (input.equals("y")) {
					break;
				} else {
					System.out.println("n/y�����ϳ��� �Է��Ͽ��ּ���.");
				}
			}

		}
	}
	public static void makeWordBook() {
		BufferedReader br;
		File f = new File("E:\\�޸���\\words.txt"); // ���� �������
		try {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8")); // BufferedReader�� ���� �ܾ���� �о��

		String line = null;
		wordVector = new Vector<String>();

		while ((line = br.readLine()) != null) { // ���پ� �ܾ �о��
			wordVector.add(line);
		}
		}catch(IOException e) {
			System.out.println("�ܾ����� �ҷ����� ���߽��ϴ�.");
		}
	}

	public static void main(String[] args) throws IOException {
		

		hangMan_menu_print();

	}

}
