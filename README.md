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
-> openssl을 다운후 bin폴더를 환경변수path에 추가한다.  다운로드주소 : https://code.google.com/archive/p/openssl-for-windows/downloads
-> 다운로드 목록 중에 반드시 아래 4개에서 받아야 한다!

#### [안드로이드 키스토어 사용법]
1. C:\Users\s_negaro\.android\debug.keystore로 이동
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
1. app -> android -> signingReport 더블클릭

#### * 안드로이드 릴리스  해시키 (출시용)
      keytool -exportcert -alias <release_key_alias> -keystore <release_keystore_path> | openssl sha1 -binary | openssl base64

----------------------------------------------------------------------------------------------------------------------------------------

### 페이스북 연동 시 "invalid key hash..." 같은 에러가 뜰 경우
1. 에러메세지에 나온 해시키를 복사
1. 페이스북 개발자 페이지에 해시 키를 등록
<br> ex) prhPWEt+D1XCL06/siQjUedyNl4=

