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
			hmc.soc = new Socket("localhost", 9900);// ���� ����.������ ������ ip�ּҿ� ��Ʈ��ȣ �Է�
			hmc.in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			hmc.out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));

			while (true) {
				String tmp = null;
				while (!(tmp = hmc.in.readLine()).equals("*")) {// ������ �������� *�̵������� �о��
					System.out.println(tmp);

					if (tmp.equals("Next(y/n)?")) {// �����̳����� �����κ��� �̹��ڸ� �޾ƿý� �������������� ������

						while (true) {
							outputMessage = br.readLine();

							if (outputMessage.equalsIgnoreCase("n")) { // n���Է½� ����
								hmc.out.write(outputMessage + "\n");
								hmc.out.flush();
								return;
							} else if (outputMessage.equalsIgnoreCase("y")) { // y�Է½� ������ �ٽý���
								hmc.out.write(outputMessage + "\n");
								hmc.out.flush();
								tmp = hmc.in.readLine();
								outputMessage = null;
								break;
							} else {
								System.out.println("y/n�� �Է��Ͽ��ּ���."); // ��Ÿ�Է½��̰������
							}
						}
					}
				}

				hmc.time = 20;
				tt = new timerThread(hmc); // Ÿ�̸ӽ���
				tt.start();
				System.out.print("������>>");

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
					hmc.out.write(outputMessage + "\n");// �Է��Ѱ� ����
					hmc.out.flush();// out�� ��Ʈ�� ���ۿ��ִ� ��� ���ڿ� ����
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
				System.out.println("������ ä�� �� ������ �߻��߽��ϴ�.");
			}
		}
	}

}
