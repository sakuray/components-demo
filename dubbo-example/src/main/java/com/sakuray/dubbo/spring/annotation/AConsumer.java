package com.sakuray.dubbo.spring.annotation;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sakuray.dubbo.spring.annotation.consumer.AnnotationComsumer;

public class AConsumer {

	public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"annotation-consumer.xml"});
        context.start();
        AnnotationComsumer demoService =  (AnnotationComsumer) context.getBean("annotationComsumer");
        demoService.print();
    }
}
