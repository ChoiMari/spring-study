package DI3;

public class Program {

	public static void main(String[] args) {
		
		NewRecordView view = new NewRecordView();
		//놀다가 ....
		//나는 니가 필요해 (setter  함수를 통해서 주소를 주입)
		
		NewRecord record = new NewRecord(100, 100, 100); //Record 라는 인터페이스 구현 ...
		view.setRecord(record);//Record 구현하는 객체 (다형성)
		view.print();

		//의존객체를 주입 받는다 >> Spring 이작업을 자동화 (DI)
		
	}

}
