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

	static Vector<String> wordVector; // words.txt���� �ܾ �о�� ����
	static String questionOfWord; // ������ ���� ����
	static char[] word;
	static char[] wordAnswer;
	static int changeWordAmount = 2; // ��� �ܾ �ٲܰ�����
	static int time;
	static int cnt;// ���� ī��Ʈ Ƚ��

	// ������ �ʼ��� ������
	static BufferedReader in = null;
	static BufferedWriter out = null;
	static ServerSocket listener = null;
	static Socket soc = null;

	public static void makeWordBook() {
		BufferedReader br;
		File f = new File("E:\\�޸���\\words.txt"); // ���� �������
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8")); // BufferedReader�� ���� �ܾ����
																								// �о��

			String line = null;
			wordVector = new Vector<String>();

			while ((line = br.readLine()) != null) { // ���پ� �ܾ �о��
				wordVector.add(line);
			}
		} catch (IOException e) {
			System.out.println("�ܾ����� �ҷ����� ���߽��ϴ�.");
		}
	}

	public static void makeWord() {
		questionOfWord = wordVector.get((int) (Math.random() * wordVector.size())); // ���� �������� �ܾ��ϳ� ����
		wordAnswer = new char[questionOfWord.length()];
		word = new char[questionOfWord.length()];

		for (int i = 0; i < questionOfWord.length(); i++) {
			word[i] = questionOfWord.charAt(i);
		}

		for (int i = 0; i < changeWordAmount; i++) {
			int n = (int) (Math.random() * word.length); // ������ �վ�������� �������� �ε�������

			if (word[n] == '-') { // �̹� -�� �������� ����Ͽ� ����ó��
				i--;
			} else {
				wordAnswer[n] = word[n]; // ������ �迭 �ε����� �����س�
				word[n] = '-';
			}
		}
	}

	public static boolean isCollect() { // �ٲ���� ������ �´��� Ȯ��
		for (int i = 0; i < questionOfWord.length(); i++) {
			if (questionOfWord.charAt(i) != word[i])
				return false;
		}
		return true;
	}

	public static void sendMessage(String m, BufferedWriter out) {// Ŭ������ ������ �޼���
		try {
			out.write(m);
		} catch (IOException e) {
		}
	}

	public static void checkWord(char w, BufferedWriter out) {

			for (int i = 0; i < changeWordAmount; i++) {
				for (int j = 0; j < wordAnswer.length; j++) {           //������ ������
					if (w == wordAnswer[j]) {// �´� �ܾ ������ �Լ� ����  
						word[j] = wordAnswer[j];
						wordAnswer[j] = ' '; // �ߺ��Ǵ� ������ ������ ������ �ٲٱⰡ��
						return;
					}
				}
			}

			sendMessage("Ʋ�Ƚ��ϴ�.\n", out);  //Ʋ�����

			cnt--;

	}

	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);
		
		makeWordBook();

		try {

			listener = new ServerSocket(9900);
			System.out.println("������ ��ٸ��� �ֽ��ϴ�...");
			soc = listener.accept(); // Ŭ���̾�Ʈ�κ��� ���� ��û ���
			System.out.println("����Ǿ����ϴ�.");

			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));// Ŭ������ ������ �޾ƿ�
			out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));// ����
			while (true) {
				makeWord();
				//sendMessage(questionOfWord+"\n",out); //���䵵���̺����� �ڵ�
				sendMessage(String.valueOf(word) + "\n" + "*\n", out); // ���� ��� Ŭ�󿡰�����
				out.flush();

				cnt = 5;

				while (cnt != 0) {
					String tmp=null;
					tmp = in.readLine();
					
					if(tmp.equals("timeout")) {//�ð��ʰ��� �޾ƿ��� "\n" ó��
						sendMessage("�ð��ʰ� !\n",out);
						cnt--;
					}else if (tmp.length() != 1) {
						sendMessage("�ѱ��ڸ� �Է��Ͽ��ּ���\n*\n", out);
						out.flush();
						continue;
					} else {
						checkWord(tmp.charAt(0), out);  //������ �Է��� �´���Ȯ��
						if (isCollect()) {
							sendMessage("�����Դϴ�.\n"+word+"\n", out);
							break;
						}
					}
					
					if (cnt == 0) { //Ƚ�� 0�Ͻ� ���ӿ��� �޽��� ������ ��������� ���
						sendMessage("GameOver!\n", out);
						sendMessage("������ " + questionOfWord + "\n", out);
						break;

					} else {
						sendMessage("�ٽ� �Է��Ͽ��ּ���.(���� Ƚ��:" + cnt + ")\n", out);
					}

					sendMessage(String.valueOf(word) + "\n*\n", out); // �ٲ�ų� Ʋ�� ������ �ٽ�����
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
						out.write("y/n ���Է��Ͽ��ּ���.\n");
					}

				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				s.close(); // scanner �ݱ�
				soc.close(); // ��ſ� ���� �ݱ�
				listener.close();// ���� ���ϴݱ�
			} catch (IOException e) {
				System.out.println("Ŭ���̾�Ʈ�� ä�� �� ���� �߻�");
			}
		}
	}

}
