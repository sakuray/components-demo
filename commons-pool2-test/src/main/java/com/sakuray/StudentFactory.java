package com.sakuray;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Random;

public class StudentFactory extends BasePooledObjectFactory<Student> {

    private Random random = new Random();

    public Student create() throws Exception {

        int age = random.nextInt(100);
        Student student = new Student("commons", age);
        System.out.println("创建对象:" + student);
        return student;
    }

    public PooledObject<Student> wrap(Student obj) {
        return new DefaultPooledObject<Student>(obj);
    }

    @Override
    public void destroyObject(PooledObject<Student> p) throws Exception {
        System.out.println("销毁对象：" + p.getObject());
        super.destroyObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<Student> p) {
        System.out.println("校验对象是否可用：" + p.getObject());
        return super.validateObject(p);
    }

    @Override
    public void activateObject(PooledObject<Student> p) throws Exception {
        System.out.println("激活钝化的对象系列操作：" + p.getObject());
        super.activateObject(p);
    }

    @Override
    public void passivateObject(PooledObject<Student> p) throws Exception {
        System.out.println("钝化未使用的对象：" + p.getObject());
        super.passivateObject(p);
    }
}
