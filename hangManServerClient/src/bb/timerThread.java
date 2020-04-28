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
			//System.out.println(hm.time); //타이머잘돌아가는지 확인하는코드
			hm.time--;
			
		}
		System.out.println("시간초과!");
		System.out.println(hm.word);
	}

}
