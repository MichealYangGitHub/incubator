package com.michealyang.service.houseSpy;

/**
 * Created by michealyang on 17/3/19.
 */
public interface IConvertor<T, E> {

    /**
     * 将爬取的数据类型T转换成需要的数据类型E
     * @param data
     * @return
     */
    public E doAction(T data);
}
