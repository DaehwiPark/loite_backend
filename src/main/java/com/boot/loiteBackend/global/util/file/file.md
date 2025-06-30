## 1. 개요

파일 업로드 로직을 공통화하여 다양한 도메인(`상품`, `리뷰`, `기타`)에서 재사용할 수 있도록 하기 위한 설계입니다.

각 파일은 업로드 시 카테고리별 디렉토리에 자동 분류 저장되며, 정적 리소스로도 접근 가능하도록 구성합니다.

---

## 2. 디렉토리 구조

```
📁 uploads/
 ┣ 📁 product/
 ┣ 📁 review/
 ┗ 📁 etc/
```

- 파일은 `/uploads/{category}/{uuid_filename}` 경로에 저장됩니다.
- 브라우저는 `http://localhost:8080/uploads/{category}/...`로 접근 가능합니다.

---

## 3. 설정

### `application.properties`

```bash
# 업로드될 실제 디렉토리 경로 (예: ../uploads)
file.upload-dir=../uploads
# 클라이언트가 접근할 때 사용할 가상 URL prefix
file.upload-url-prefix=/uploads
```

- 설정을 **하드코딩하지 않고** 파일로 외부화하여 쉽게 변경할 수 있음
- 예: 파일 업로드 경로 및 URL prefix 설정

### `FileStorageProperties.java`

```java
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String uploadUrlPrefix;
}
```

- `application.properties` 값들을 필드로 바인딩
- 외부 설정값을 자바 코드에서 손쉽게 참조

✔️ **핵심 포인트:**  `application.properties` 의 경로를 `FileStorageProperties` 에서 읽어와서 코드에서 `fileProps.getUploadDir()` 처럼 사용할 수 있다.

---

## 4. 공통 인터페이스 및 결과 객체

### `FileService.java`

```java
public interface FileService {
    FileUploadResult save(MultipartFile file, String category);
}
```

- 다른 서비스 클래스들이 파일 저장 기능을 사용할 수 있도록 추상화
- 로컬 저장이든, AWS S3 저장이든 **구현체(카테고리)만 바꿔서 사용 가능하게 함**

### `FileUploadResult.java`

```java
@Getter
@AllArgsConstructor
public class FileUploadResult {
    private String urlPath;       // /uploads/category/filename.ext
    private String physicalPath;  // 실제 경로: C:/.../uploads/category/filename.ext
}
```

- 업로드 후 URL 경로와 실제 경로를 모두 포함해 전달

✔️ 사용 예시:

```java
FileUploadResult result = fileService.save(file, "review");
String fileUrl = result.getUrlPath(); // 파일 불러올 경로 
String filePath = result.getPhysicalPath(); // 파일이 저장된 경로 
```

---

## 5. 로컬 저장 구현체

### `FileServiceImpl.java`

```java
@Service
public class FileServiceImplimplements FileService {

    private final FileStorageProperties fileProps;

    public FileServiceImpl(FileStorageProperties fileProps) {
        this.fileProps = fileProps;
    }

    @Override
		// 클라이언트로부터 전달받은 파일과 카테고리(예: product, review, etc)를 기준으로 파일을 저장합니다.
    public FileUploadResult save(MultipartFile file, String category) {
        try {
		        // originalName: 업로드된 파일의 원래 이름을 가져옴.
            String originalName = file.getOriginalFilename();
            // String safeName = originalName != null ? originalName.replaceAll("\\s+", "_") : "unnamed";
            String safeName = originalName != null ? originalName.replaceAll("\\s+", "_") : "unnamed";
            // String fileName = UUID.randomUUID() + "_" + safeName;
            String fileName = UUID.randomUUID() + "_" + safeName;

						// 현재 실행 중인 프로젝트의 루트 디렉토리.
            File uploadDir = new File(System.getProperty("user.dir"),
            // ../uploads/product, ../uploads/review 등의 경로 생성.
            fileProps.getUploadDir() + "/" + category);
            // mkdirs(): 디렉토리가 없으면 자동 생성.
            if (!uploadDir.exists()) uploadDir.mkdirs();
						
						// dest: 실제로 저장될 파일 객체.
            File dest = new File(uploadDir, fileName);
            // file.transferTo(...): 업로드된 파일의 데이터를 디스크에 저장.
            file.transferTo(dest);

            String urlPath = fileProps.getUploadUrlPrefix() + "/" + category + "/" + fileName;
            return new FileUploadResult(urlPath, dest.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
```

---

## 6. 정적 리소스 핸들링

### `WebConfig.java`

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageProperties fileProps;

    public WebConfig(FileStorageProperties fileProps) {
        this.fileProps = fileProps;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File root = new File(System.getProperty("user.dir"));
        String fullPath = new File(root, fileProps.getUploadDir()).getAbsolutePath().replace("\\", "/");

        registry.addResourceHandler(fileProps.getUploadUrlPrefix() + "/**")
                .addResourceLocations("file:" + fullPath + "/");
    }
}
```

---

## 7. 서비스 단 사용 예

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final FileService fileService;

    public void saveProduct(MultipartFile file) {
		    // 모듈화 해둔 fileService 의 save 메서드 호출 (file 과 저장 폴더 명 전달)
        FileUploadResult result = fileService.save(file, "product");
        // /uploads/product/uuid_filename.png
        String fileUrl = result.getUrlPath();      
	      // C:/.../uploads/product/...   
        String fullPath = result.getPhysicalPath();

        // 이후 fileUrl을 DB에 저장 등 처리
    }
}
```

---

## 📊 전체 흐름 요약

1. 클라이언트가 파일 업로드 요청 (`MultipartFile`)
2. `FileService.save(file, category)` 호출
3. `LocalFileServiceImpl` 이 실제로 파일을 로컬에 저장
4. 저장 결과는 `FileUploadResult(url, path)` 로 반환
5. DB에는 `urlPath` 저장, 클라이언트는 `/uploads/...`로 접근

---

## ✅ 정리

| 구성 요소 | 역할 |
| --- | --- |
| `application.properties` | 업로드 디렉토리 및 URL prefix 설정 |
| `FileStorageProperties.java` | 설정값을 Java 객체로 매핑 |
| `FileService.java` | 저장 로직의 인터페이스 (확장 가능성 확보) |
| `FileUploadResult.java` | 저장 결과(URL, 실제 경로) 패키징 |
| `LocalFileServiceImpl.java` | 로컬에 실제로 파일 저장 (디스크 기반) |