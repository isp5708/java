package javaSubject3;

public class timerThread extends Thread {
	hangManClient hmc;
	public timerThread(hangManClient hmc) {
		this.hmc=hmc;
	}
	public void run() {
		
		while(hmc.time!=0) {
			
			try {
				sleep(1000);
				
			}catch ( InterruptedException e) {
				// TODO: handle exception
				return;
			}
			//System.out.println(hm.time); //Ÿ�̸��ߵ��ư����� Ȯ���ϴ��ڵ�
			hmc.time--;
			
		}

	}

}
