<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 
 OrderCommand command = new OrderCommand();
  
  List<OrderItem> itemList = new ArrayList<>();
  itemList.add(new OrderItem(1,10,...));
  itemList.add(new OrderItem(2,1,...));
  
  command.setOrderItem(itemList);
 -->
 	<form method="post">
 		상품_1<br>
 		상품ID: <input type="text" name="orderItem[0].itemid"> <!-- new OrderItem(1,10,...) -->
 		상품수량: <input type="text" name="orderItem[0].number">
 		상품주의사항: <input type="text" name="orderItem[0].remark">
 		<br>
 		상품_2<br>
 		상품ID: <input type="text" name="orderItem[1].itemid"><!-- new OrderItem(2,1,...)  -->
 		상품수량: <input type="text" name="orderItem[1].number">
 		상품주의사항: <input type="text" name="orderItem[1].remark">
 		<br>
 		상품_3<br>
 		상품ID: <input type="text" name="orderItem[2].itemid"> <!-- new OrderItem(2,1,...)  -->
 		상품수량: <input type="text" name="orderItem[2].number">
 		상품주의사항: <input type="text" name="orderItem[2].remark">
 		<br>
 		<hr>
 		<input type="submit" value="전송">
 	</form>
</body>
</html>