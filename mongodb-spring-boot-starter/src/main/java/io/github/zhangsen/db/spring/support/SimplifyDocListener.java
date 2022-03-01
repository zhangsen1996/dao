package io.github.zhangsen.db.spring.support;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.Map;

public class SimplifyDocListener extends AbstractMongoEventListener {

    /**
     * 语言的特性，
     * 如果字段属于是基本数据类型中的[int,short,long,byte,boolean]，其值又是基本数据类型的默认值的话，
     * 其实是不用入库的，因为都是默认值，反序列化回来也是默认值
     * {1:2,3:0} 这样的map形式，key==3这个元素也会被移除，
     * 是否需要解决？需要的话就从document的class得到type数据，这样就可以精准移除，更倾向于在业务层面去迎合基本数据类型默认值这个约定，
     *  这样就不用处理这个问题，或者关闭这个选项（io.github.zhangsen.db.spring.config.SpringMongoProperties#simplifyDoc）
     * @param event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent event) {
        Document document = event.getDocument();
        checkDoc(document);
        super.onBeforeSave(event);
    }

    private void checkDoc(Document document){
        for (Map.Entry<String, Object> stringObjectEntry : document.entrySet()) {
            Object value = stringObjectEntry.getValue();
            if (stringObjectEntry.getKey().equals("_id")) {
                //_id 字段不用对比
                continue;
            }
            if (value instanceof Integer){
                if (((Integer) value).intValue() == 0){
                    document.remove(stringObjectEntry.getKey());
                }
            }else if (value instanceof Short){
                if (((Short) value).shortValue() == 0){
                    document.remove(stringObjectEntry.getKey());
                }
            } else if (value instanceof Long){
                if (((Long) value).longValue() == 0){
                    document.remove(stringObjectEntry.getKey());
                }
            } else if (value instanceof Byte){
                if (((Byte) value).byteValue() == 0){
                    document.remove(stringObjectEntry.getKey());
                }
            } else if (value instanceof Boolean){
                if (((Boolean) value).booleanValue() == false){
                    document.remove(stringObjectEntry.getKey());
                }
            } else if (value instanceof Document){
                //递归检查
                checkDoc((Document)value);
            }
        }
    }
}
