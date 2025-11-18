USE BOOKMARKETDB; 
CREATE TABLE IF NOT EXISTS book (
 b_bookId VARCHAR(10) NOT NULL,
 b_name VARCHAR(30),
 b_unitPrice INTEGER,
 b_author VARCHAR(50),
 b_description TEXT,
 b_publisher VARCHAR(20),
 b_category VARCHAR(20),
 b_unitsInStock LONG,
 b_releaseDate VARCHAR(20),
 b_condition VARCHAR(20),
 b_fileName VARCHAR(20),
 PRIMARY KEY (b_bookId)
) DEFAULT CHARSET=utf8;
DELETE FROM book; 
INSERT INTO book VALUES('ISBN1234', '자바스크립트 입문', 30000,'조현영', '자바스크립트
의 기초부터 심화까지 핵심 문법을 학습한 후 12가지 프로그램을 만들며 학습한 내용을
확인할 수 있습니다. 문법 학습과 실습이 적절히 섞여 있어 프로그램을 만드는 방법을 재
미있게 익힐 수 있습니다.','길벗','IT전문서', 1000,'2024/02/20','new','ISBN1234.jpg');
INSERT INTO book VALUES('ISBN1235', '파이썬의 정석', 29800, '조용주,임좌상', '4차 산업혁
명의 핵심인 머신러닝, 사물 인터넷(IoT), 데이터 분석 등 다양한 분야에 활용되는 직관
적이고 간결한 문법의 파이썬 프로그래밍 언어를 최신 트렌드에 맞게 예제 중심으로 학습
할 수 있습니다.','길벗','IT교육교재',1000, '2023/01/10','new', 'ISBN1235.jpg');
INSERT INTO book VALUES('ISBN1236', '안드로이드 프로그래밍', 25000, '송미영', '안드로
이드의 기본 개념을 체계적으로 익히고, 이를 실습 예제를 통해 익힙니다. 기본 개념
과 사용법을 스스로 실전에 적용하는 방법을 학습한 다음 실습 예제와 응용 예제를 통
해 실전 프로젝트 응용력을 키웁니다.', '길벗', 'IT교육교재', 1000, '2023/06/30',
'new','ISBN1236.jpg'); 
