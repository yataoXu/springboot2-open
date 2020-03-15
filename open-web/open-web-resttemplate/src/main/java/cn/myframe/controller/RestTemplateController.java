package cn.myframe.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class RestTemplateController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value="/rest",method = RequestMethod.GET)
    public String rest() throws URISyntaxException {
        String url = "http://127.0.0.1:8080/hello";
        // 1-getForObject()
        String response = this.restTemplate.getForObject(url , String.class);
        System.out.println(response);
        // 2-getForEntity()
        ResponseEntity<String> responseEntity1 = this.restTemplate.getForEntity(url, String.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        HttpHeaders header = responseEntity1.getHeaders();
        String response2 = responseEntity1.getBody();
        System.out.println(response2);
        // 3-exchange()
        RequestEntity requestEntity = RequestEntity.get(new URI(url)).build();
        ResponseEntity<String> responseEntity2 = this.restTemplate.exchange(requestEntity, String.class);
        String response3 = responseEntity2.getBody();
        System.out.println(response3);
        return response;
    }

    /**
     * 发送文件
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @RequestMapping(value="/sendFile")
    public String sendFile() throws URISyntaxException, IOException {
        MultiValueMap<String, Object> multiPartBody = new LinkedMultiValueMap<>();
        multiPartBody.add("file", new ClassPathResource("tmp/user.txt"));
        RequestEntity<MultiValueMap<String, Object>> requestEntity = RequestEntity
                .post(new URI("http://127.0.0.1:8080/upload1"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multiPartBody);
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        return "sueecess";
    }

    /**
     * 下载文件
     * @param response
     * @throws URISyntaxException
     * @throws IOException
     */
    @RequestMapping(value="/receiveFile")
    public void receiveFile(HttpServletResponse response) throws URISyntaxException, IOException {
        // 小文件
        RequestEntity requestEntity = RequestEntity.get(
                new URI("http://127.0.0.1:8080/downLoad.html")).build();
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);
        byte[] downloadContent = responseEntity.getBody();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"myframe.html\"");
        response.addHeader("Content-Length", "" + downloadContent.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(downloadContent, response.getOutputStream());
    }

    /**
     * 大文件
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value="/bigFile")
    public String bigFile(HttpServletResponse response) throws URISyntaxException {
        //大文件
        ResponseExtractor<ResponseEntity<File>> responseExtractor =
                new ResponseExtractor<ResponseEntity<File>>() {
            @Override
            public ResponseEntity<File> extractData(ClientHttpResponse response)
                    throws IOException {
                File rcvFile = File.createTempFile("rcvFile", "zip");
                FileCopyUtils.copy(response.getBody(), new FileOutputStream(rcvFile));
                return ResponseEntity.status(response.getStatusCode()).
                        headers(response.getHeaders()).body(rcvFile);
            }
        };
        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {

            }
        };
        ResponseEntity<File> fileBody = this.restTemplate.execute(
                new URI("http://127.0.0.1:8080/downLoad.html"),
                HttpMethod.GET,  requestCallback, responseExtractor);
        File file = fileBody.getBody();
        file.renameTo(new File("D:/Users/big.hmtl"));
        return "success";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "success";
    }

    @RequestMapping("/upload1")
    public Map<String, String> upload1(@RequestParam("file") MultipartFile file)
            throws IOException {
        log.info("[文件类型] - [{}]", file.getContentType());
        log.info("[文件名称] - [{}]", file.getOriginalFilename());
        log.info("[文件大小] - [{}]", file.getSize());
        file.transferTo(new File("D:/Users/" + file.getOriginalFilename()));
        Map<String, String> result = new HashMap<>(16);
        result.put("contentType", file.getContentType());
        result.put("fileSize", file.getSize() + "");
        result.put("fileName", file.getOriginalFilename());
        return result;
    }
}
