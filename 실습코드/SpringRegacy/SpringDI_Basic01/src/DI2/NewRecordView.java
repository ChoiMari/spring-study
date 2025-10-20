package DI2;

//점수를 출력하는 클래스
public class NewRecordView {
	//NewRecordView는 점수를 받기 위해서 NewRecord가 필요합니다
	//상속 : 포함 **
	
	private NewRecord record; //member field NewRecord 객체의 주소를 가지겠다
	//포함 (복합연관) 
	//public NewRecordView(int kor, int eng , int math) {
	//	record = new NewRecord(kor, eng, math);
	//}
	
	public void setRecord(NewRecord record) {
		this.record = record;
	}
	
	/*
	  나는 니가 필요해
	  나는 너의 객체 [주소] 가 필요해
	  
	  주소를 얻는 방법
	  1. [생성자를 통해서] 필요한 객체를 생성 또는 주입 > DI > 복합연관, 집합연관
	  2. 함수(특수한 목적의 함수 : setter ) 통해서 필용한 객체 주입 > DI > 집합 > 필요에 따라서 
	  
	 */

	public void print() {
		System.out.println(record.total() + " / " + record.avg());
	}



	
}
