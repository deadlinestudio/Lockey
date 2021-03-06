### 파이어베이스에 안드로이드 앱 등록
1. 파이어베이스에 프로젝트 추가
1. 프로젝트 설정
1. 안드로이드 앱 추가
1. 앱등록 - 구성파일 다운로드 - FIrebase SDK 추가 순서대로 이행 ( build.gradle dependency에는 항상 최신버전으로 등록 필수!)
  <br>* 구성파일에서 json 다운로드시 해시키를 등록후 다운로드 하기!

### 안드로이드 스튜디오에 파이어베이스 등록
1. android studio -> tools -> firebase -> Assistant -> Analytics -> Log an Analytics event 클릭
1. firebase 연결 클릭 

----------------------------------------------------------------------------------------------------------------------------------------

### 안드로이드 디버그 해시키
#### [keytool 사용법]
1. keytool의 위치는 C:\Program Files\Java\jdk-11.0.1\bin에 있다.
1. cmd에서 keytool의 위치로 간후
1.      keytool -exportcert -alias androiddebugkey -keystore "C:\Users\s_negaro\.android\debug.keystore" -storepass android -keypass android | openssl sha1 -binary | openssl base64 
1. 초기 비밀번호는 android
<br>ex) zSdJoEbFoz6suAQRNzDRPkT97vM=
<br>* 'openssl'은(는) 내부 또는 외부 명령, 실행할 수 있는 프로그램, 또는 배치 파일이 아닙니다.
<br>-> openssl을 다운후 bin폴더를 환경변수path에 추가한다.  다운로드주소 : https://code.google.com/archive/p/openssl-for-windows/downloads
<br>-> 다운로드 목록 중에 반드시 아래 4개에서 받아야 한다!

#### [안드로이드 키스토어 사용법]
1. C:\Users\s_negaro\\.android 로 이동
1.      keytool -list -v -keystore debug.keystore 
1. 초기 비밀번호는 android
<br>ex) 3F:23:00:98:D3:13:DD:34:D7:13:38:47:96:C2:B2:7E:F8:7F:48:78

#### [코드로 해시키를 구하는 법]
    public static String getKeyHash(final Context context) {
       PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
       if (packageInfo == null)
            return null;

       for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
             } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

#### [안드로이드 스튜디오에서 해시키 얻는 법]
1. 스튜디오 오른쪽변에 Gradle 클릭
2. app -> android -> signingReport 더블클릭

----------------------------------------------------------------------------------------------------------------------------------------

### 안드로이드 릴리즈 해시키 생성 및 사이닝 
#### 안드로이드 스튜디오 작업
1. Build -> Generate Signed Bundle/APK -> APK 선택 -> Create New
2. Key store path, Password, Alias는 꼭 외워두기
3. 완료시 jks 파일이 생성됨
4. Certificate는 Fisrt and Last Name만 적어도 무관
5. Build Type은 release로 선택, Signature Version은 V1으로 선택
6. 완료 시 apk가 생성
7. File -> Project Structure -> Signing에 +버튼 클릭
8. Name을 release로 적고 나머지 정보를 입력
9. Build Types 탭에서 release를 선택후 Signing Config에서 release 선택

#### Cmd창
1. jks파일 위치로 이동
2.      keytool -list -v -keystore "jks파일 이름"  // 구글용 해시키
3.      keytool -exportcert -alias keystore -keystore "jks파일 이름" -storepass android -keypass android | openssl sha1 -binary | openssl base64    // 카카오용 해시키

#### 실제로 Firebase와 카카오 디벨로퍼에 저장해야할 해시키는 출시해야 생성됨
1. Google Play Console -> 앱 출시 관리 -> 앱 서명
2. 앱 서명 인증서에 나온 해시키를 등록해야 함
3. Firebase는 그대로 등록 가능하지만 카카오는 해시키가 너무 길다고 나옴
4. 터미널(Cmd는 불가) 열기
5.      echo "해시키" | xxd -r -p | openssl base64 
6. 위 해시키를 카카오에 등록 (앱 서명 인증서, 앱 업로드 인증서 "둘 다" 등록해야함!!)

----------------------------------------------------------------------------------------------------------------------------------------

### 페이스북 연동 시 "invalid key hash..." 같은 에러가 뜰 경우
1. 에러메세지에 나온 해시키를 복사
1. 페이스북 개발자 페이지에 해시 키를 등록
<br> ex) prhPWEt+D1XCL06/siQjUedyNl4=

