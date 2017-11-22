package com.lion.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author DJ
 * @date 2017/11/1.
 */
@Controller
@RequestMapping(value = "/resource")
public class ResourceController {
    @RequestMapping("/showImage")
    public void showPicture(HttpServletRequest request,
                            HttpServletResponse response, String imagePath,int type) throws IOException {
        //如果没有图片显示默认图片
        if(imagePath == null || imagePath.equals("")){

            imagePath = request.getSession().getServletContext().getRealPath("/statics/images/default_"+(type==0?"0":"1")+".png");
        }
        FileInputStream in;
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            // 图片读取路径
            in = new FileInputStream(imagePath);
            int i = in.available();
            byte[] data = new byte[i];
            in.read(data);
            in.close();

            // 写图片
            OutputStream outputStream = new BufferedOutputStream(
                    response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request,
                                               String filePath) throws Exception{
        //指定要下载的文件所在路径
        //String path = request.getServletContext().getRealPath(filePath);
        File file = new File(filePath);
        HttpHeaders headers = new HttpHeaders();

        //通知浏览器以下载的方式打开文件
        headers.setContentDispositionFormData("attachment", filePath);

        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        //使用Spring MVC框架的ResponseEntity对象封装返回下载数据
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
    }
}
