package javaSubject3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class hangManClient {
	static int time = 20;
	static BufferedReader in = null;
	static BufferedWriter out = null;
	static Socket soc = null;
	static timerThread tt;

	public static void main(String[] args) {

		hangManClient hmc = new hangManClient();
		String outputMessage = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			hmc.soc = new Socket("localhost", 9900);// 소켓 생성.연결할 서버의 ip주소와 포트번호 입력
			hmc.in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			hmc.out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));

			while (true) {
				String tmp = null;
				while (!(tmp = hmc.in.readLine()).equals("*")) {// 문자의 마지막에 *이들어갈때까지 읽어옴
					System.out.println(tmp);

					if (tmp.equals("Next(y/n)?")) {// 게임이끝나면 서버로부터 이문자를 받아올시 더진행을할지를 결젖ㅇ

						while (true) {
							outputMessage = br.readLine();

							if (outputMessage.equalsIgnoreCase("n")) { // n을입력시 종료
								hmc.out.write(outputMessage + "\n");
								hmc.out.flush();
								return;
							} else if (outputMessage.equalsIgnoreCase("y")) { // y입력시 게임을 다시시작
								hmc.out.write(outputMessage + "\n");
								hmc.out.flush();
								tmp = hmc.in.readLine();
								outputMessage = null;
								break;
							} else {
								System.out.println("y/n만 입력하여주세요."); // 기타입력시이것을출력
							}
						}
					}
				}

				hmc.time = 20;
				tt = new timerThread(hmc); // 타이머시작
				tt.start();
				System.out.print("보내기>>");

				while (hmc.time != 0) {
					try {
						if (br.ready()) {
							outputMessage = br.readLine();
							tt.interrupt();
							break;
						}
					} catch (IOException e) {
					}
				}

				if (outputMessage == null) {
					System.out.println();
					hmc.out.write("\n");
					hmc.out.flush();
				} else {
					hmc.out.write(outputMessage + "\n");// 입력한것 전송
					hmc.out.flush();// out의 스트림 버퍼에있는 모든 문자열 전송
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				br.close();
				if (hmc.soc != null)
					hmc.soc.close();
			} catch (IOException e) {
				System.out.println("서버와 채팅 중 오류가 발생했습니다.");
			}
		}
	}

}
