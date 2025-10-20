package DI2;

public class Program {

	public static void main(String[] args) {
		//NewRecordView view = new NewRecordView(100, 50, 40);
		//view.print();
		
		
		NewRecordView view = new NewRecordView();
		//놀다가
		
		//NewRecord 객체의 주소가 필요하다면
		NewRecord record = new NewRecord(100, 100, 100);
		view.setRecord(record); //의존 객체의 주소를 주입 받는다 ( DI )
		view.print();
		
	}

}
