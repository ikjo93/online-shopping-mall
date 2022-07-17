## 온라인 쇼핑몰 상품 카테고리 프로젝트

### 구현 내용
+ 온라인 쇼핑몰 상위 및 하위 상품 카테고리 관련 API

#### 조회 API
+ 상위 카테고리로 해당 카테고리의 모든 카테고리 조회 
+ 상위 카테고리 별도 지정하지 않을 시 전체 카테고리(상위 및 하위) 반환
  + 상위 카테고리에 속하지 않은 하위 카테고리는 별도 `unclassified`로 분류하여 반환

#### 등록 API
+ 상위 카테고리와 하위 카테고리 각각 등록 가능
+ 상위 카테고리 등록 기능 상세
  + 상위 카테고리 등록 시 이름을 필수 저징 요구
  + 상위 카테고리 등록 시 다른 상위 카테고리들과 이름이 중복되는지 검사
+ 하위 카테고리 등록 기능 상세
  + 하위 카테고리 등록 시 상위 카테고리와 이름을 필수 지정 요구
  + 등록하려는 상위 카테고리와 해당 카테고리의 하위 카테고리들 중 중복되는 이름이 있는지 검사

#### 수정 API
+ 상위 카테고리와 하위 카테고리 각각 수정 가능
+ 상위 카테고리 수정 기능 상세
  + 상위 카테고리의 이름 수정 가능
  + 해당 상위 카테고리에 속한 하위 카테고리들과 다른 상위 카테고리 간 이름 중복 검사 
+ 하위 카테고리 수정 기능 상세
  + 하위 카테고리의 이름과 상위 카테고리 수정 가능
  + 수정하려는 상위 카테고리의 이름과 이에 속한 하위 카테고리들 간 이름 중복 검사 

#### 삭제 API
+ 상위 카테고리와 하위 카테고리 각각 삭제 가능
  + 상위 카테고리 삭제 시 기존 하위 카테고리는 추후 전체 카테고리 반환 시 `unclassified`로 분류하여 반환

<br>

### API 명세서 링크(노션)
+ <a href="https://near-snipe-0de.notion.site/API-Description-02fd2a26759940e78330bf427a58cd36">API 명세서 확인하기(클릭)</a><br/>

<br>

### 리눅스 기준 웹 서버 실행 위한 설치 및 빌드 방법

#### 리눅스 사용 버전
+ Amazon Linux 2 AMI (HVM) - Kernel 5.10, SSD Volume Type (AWS EC2)
  + Amazon Linux 2 Kernel 5.10 AMI 2.0.20220606.1 x86_64 HVM gp2

#### 설치 방법
##### 1. Java 설치
```
* aws corretto 다운로드

sudo curl -L https://corretto.aws/downloads/latest/amazon-corretto-11-x64-linux-jdk.rpm -o jdk11.rpm

* jdk11 설치

sudo yum localinstall jdk11.rpm

* jdk version 선택

sudo /usr/sbin/alternatives --config java

/usr/lib/jvm/java-11-amazon-corretto/bin/java 선택

(example)
There is 1 program that provides 'java'.

  Selection    Command
-----------------------------------------------
*+ 1           /usr/lib/jvm/java-11-amazon-corretto/bin/java

Enter to keep the current selection[+], or type selection number: 1

* java 버전 확인

java --version

* 다운받은 설치 키트 제거

rm -rf jdk11.rpm

* 설치된 자바 찾기

which java

* 자바 환경변수 설정

sudo vi /etc/profile

아래 구문 입력 및 저장(:wq)

export JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto
export PATH=$JAVA_HOME/bin:$PATH
export CLASS_PATH=$JAVA_HOME/lib:$CLASS_PATH

* 환경변수 설정 업데이트

source /etc/profile

* 환경변수 설정 확인

echo $JAVA_HOME
```

##### 2. Git 설치
```
* Install git in your EC2 instance
sudo yum install git -y

* 깃 버전 확인
git version
```

##### 3. 서버 Time/Zone 설정
```
sudo rm /etc/localtime
sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
```

#### 빌드 및 실행 방법
##### 1. Git 원격 저장소 main 브랜치 클론
```
* 기본 경로(/home/ec2-user) 이동

cd ~

* Git 원격 저장소 main 브랜치 클론

git clone -b main --single-branch https://github.com/ikjo93/online-shopping-mall
```

##### 2. 스프링 부트 빌드 및 실행
```
* 프로젝트 디렉토리로 이동

cd ~/online-shopping-mall

* gradlew 파일 실행 권한 설정

chmod +x gradlew

* 빌드(jar 파일 생성)

./gradlew bootJar

※ 최초 실행 시 Gradle Daemon 설치 시 다소 오랜 시간 소요

* jar 파일이 있는 디렉토리로 이동

cd build/libs

* jar 파일 실행(데몬, aws ec2 로그아웃 시 종료)

java -jar online-shopping-mall-0.0.1-SNAPSHOT.jar &

* jar 파일 실행(데몬, aws ec2 로그아웃 시에도 실행)

sudo nohup java -jar todo-list-0.0.1-SNAPSHOT.jar &
```

<br>

### 프로젝트 사용 기술 개요
```
Project Build Tool : Gradle
Spring Boot : 2.7.1 Version
Language : Java 11
Packaging : Jar
Dependencies
 - Spring Web
 - Spring Data JPA
 - Lombok
 - Spring Boot DevTools
IDE : IntelliJ
Database : h2(Embedded DB)
```

<br>

### ERD
> ![image](https://user-images.githubusercontent.com/82401504/179352432-50f6098a-8cf4-4672-9fcc-42420a4ce92f.png)
