package bb;

public class timerThread extends Thread {
	hangMan hm;
	public timerThread(hangMan hm) {
		this.hm=hm;
	}
	public void run() {
		
		while(hm.time!=0) {
			
			try {
				sleep(1000);
				
			}catch ( InterruptedException e) {
				// TODO: handle exception
				return;
			}
			//System.out.println(hm.time); //Ÿ�̸��ߵ��ư����� Ȯ���ϴ��ڵ�
			hm.time--;
			
		}
		System.out.println("�ð��ʰ�!");
		System.out.println(hm.word);
	}

}
