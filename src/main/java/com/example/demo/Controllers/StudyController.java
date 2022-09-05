package com.example.demo.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Controller
public class StudyController {
    private Logger log = LoggerFactory.getLogger(StudyController.class);

    @RequestMapping("/study")
    public ResponseEntity<Void> study() throws InterruptedException{
        log.info("start");
        Thread.sleep(3000);
        log.info("end");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/smash")
    @ResponseBody
    public ResponseEntity<Void> fiveRequests(){
        RestTemplate restTemplate = new RestTemplate();
        for(int i = 0; i < 5; i++){
            Thread thread = new Thread(() -> {
                log.info("Shoot");
                String result = restTemplate.getForObject("http://localhost:8080/study", String.class);
                log.info(result);
            });
            thread.start();
        }

        return ResponseEntity.ok().build();
    }

    public void readFile(Path filePath){
        final int bufferSize = 1024 * 1024;

        try(FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ)){
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void test1(){
//        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(SOURCE_FILE_PATH));
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(RESULT_FILE_PATH, true));
//        ){
//            int c;
//            while((c = bis.read()) != -1)
//                bos.write(c);
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void test2(){
//        final int bufferSize = 1024 * 1024;
//
//        Path sourceFilePath = Paths.get(SOURCE_FILE_PATH);
//        Path resultFilePath = Paths.get(RESULT_FILE_PATH);
//
//        try(FileChannel sourceChannel = FileChannel.open(sourceFilePath, StandardOpenOption.READ);
//            FileChannel resultChannel = FileChannel.open(resultFilePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//        ){
//            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
//
//            while(sourceChannel.read(buffer) >= 0){
//                buffer.flip();
//                resultChannel.write(buffer);
//                buffer.clear();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
