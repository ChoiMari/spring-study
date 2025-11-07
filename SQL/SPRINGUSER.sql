CREATE TABLE board (
    id NUMBER PRIMARY KEY,
    title VARCHAR2(200),
    writer VARCHAR2(100),
    content CLOB,
    created_at DATE DEFAULT SYSDATE
);

CREATE SEQUENCE board_seq START WITH 1 INCREMENT BY 1;

BEGIN
  FOR i IN 1..100 LOOP
    INSERT INTO board (id, title, writer, content)
    VALUES (board_seq.NEXTVAL, '제목 ' || i, '작성자' || i, '내용입니다 ' || i);
  END LOOP;
  COMMIT;
END;

commit;

select * from board order by id asc;